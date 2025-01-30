package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanResponseDto;
import com.emrekorkmaz.loanapi.loan_api.dto.paymentDto.PaymentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.paymentDto.PaymentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import com.emrekorkmaz.loanapi.loan_api.repository.CustomerRepository;
import com.emrekorkmaz.loanapi.loan_api.repository.LoanInstallmentRepository;
import com.emrekorkmaz.loanapi.loan_api.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository, LoanInstallmentRepository loanInstallmentRepository) {
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) {
        // Müşteri doğrulaması
        Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));

        // Credit limit check
        // Eğer loanRequestDto.getAmount() ve loanRequestDto.getInterestRate() double ise, BigDecimal.valueOf() kullanmalısınız
        BigDecimal loanAmount = loanRequestDto.getLoanAmount(); // Burada valueOf kullanıyoruz
        BigDecimal interestRate = loanRequestDto.getInterestRate();; // interestRate için de aynı şekilde

        // Total loan amount hesaplama: loanAmount * (1 + interestRate)
        BigDecimal totalLoanAmount = loanAmount.multiply(BigDecimal.ONE.add(interestRate));

        // Credit limit kontrolü: customer.getCreditLimit() - customer.getUsedCreditLimit() < totalLoanAmount
        BigDecimal remainingCredit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());

        System.out.println("Customer Credit Limit: " + customer.getCreditLimit());
        System.out.println("Customer Used Credit Limit: " + customer.getUsedCreditLimit());
        System.out.println("Remaining Credit: " + remainingCredit);
        System.out.println("Total Loan Amount: " + totalLoanAmount);

        if (remainingCredit.compareTo(totalLoanAmount) < 0) {
            throw new IllegalArgumentException("Customer does not have enough credit limit for this loan.");
        }

        // Installment number validation
        if (!List.of(6, 9, 12, 24).contains(loanRequestDto.getNumberOfInstallments())) {
            throw new IllegalArgumentException("Installment number must be 6, 9, 12, or 24.");
        }

        // Interest rate validation
        if (loanRequestDto.getInterestRate().compareTo(BigDecimal.valueOf(0.1)) < 0 ||
                loanRequestDto.getInterestRate().compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5.");
        }


        // Create the loan
        Loan loan = new Loan();
        loan.setLoanAmount(totalLoanAmount); // Toplam kredi tutarını set ediyoruz
        loan.setInterestRate(interestRate);
        loan.setCreateDate(loanRequestDto.getCreateDate());
        loan.setNumberOfInstallments(loanRequestDto.getNumberOfInstallments());
        loan.setCustomer(customer);
        loan.setIsPaid(false);

        // Save the loan
        Loan savedLoan = loanRepository.save(loan);

        // Create installments
        createInstallments(savedLoan.getId(), loanRequestDto.getNumberOfInstallments());

        // Update customer's used credit limit
        BigDecimal usedCreditLimit = customer.getUsedCreditLimit().add(totalLoanAmount);
        customer.setUsedCreditLimit(usedCreditLimit);
        customerRepository.save(customer);

        return new LoanResponseDto(savedLoan);
    }

    @Override
    public List<LoanResponseDto> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream()
                .map(LoanResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public LoanResponseDto getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + id + " not found"));
        return new LoanResponseDto(loan);
    }

    @Override
    public List<LoanResponseDto> getLoansByCustomer(Long customerId) {
        List<Loan> loans = loanRepository.findByCustomerId(customerId);
        return loans.stream().map(LoanResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<LoanResponseDto> getLoansByInstallments(Integer numberOfInstallments) {
        List<Loan> loans = loanRepository.findByNumberOfInstallments(numberOfInstallments);
        return loans.stream().map(LoanResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<LoanResponseDto> getLoansByStatus(Boolean isPaid) {
        List<Loan> loans = loanRepository.findByIsPaid(isPaid);
        return loans.stream().map(LoanResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public LoanResponseDto updateLoan(Long id, LoanRequestDto loanRequestDto) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + id + " not found"));

        // Güncelleme işlemleri
        loan.setLoanAmount(loanRequestDto.getLoanAmount());
        loan.setInterestRate(loanRequestDto.getInterestRate());
        loan.setCreateDate(loanRequestDto.getCreateDate());

        // Müşteri doğrulaması
        Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));
        loan.setCustomer(customer);

        // Güncellenmiş loan'ı kaydedelim ve döndürelim
        Loan updatedLoan = loanRepository.save(loan);
        return new LoanResponseDto(updatedLoan);
    }

    @Override
    public void createInstallments(Long loanId, int numberOfInstallments) {
        // Kredi ve taksit sayısını alıyoruz
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + loanId + " not found"));

        BigDecimal installmentAmount = loan.getLoanAmount().divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

        // İlk taksit tarihi, loan createDate'in 1. günü olacak şekilde ayarlanır
        LocalDate dueDate = loan.getCreateDate().withDayOfMonth(1).plusMonths(1); // İlk taksit 1. gün

        for (int i = 1; i <= numberOfInstallments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setDueDate(dueDate);
            installment.setIsPaid(false);  // Başlangıçta ödenmemiş olarak
            installment.setPaidAmount(BigDecimal.ZERO);  // Ödenen tutar sıfır
            installment.setPaymentDate(dueDate);  // Ödeme tarihi, taksitin vade tarihi olmalı

            loanInstallmentRepository.save(installment);

            // Taksit tarihini bir sonraki ayın 1. günü olarak güncelle
            dueDate = dueDate.plusMonths(1);
        }
    }

    @Override
    public PaymentResponseDto payLoan(PaymentRequestDto paymentRequest) {
        return null;
    }
}
