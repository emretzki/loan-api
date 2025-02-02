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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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

        // Loan amount alınıyor
        BigDecimal loanAmount = loanRequestDto.getLoanAmount();

        // Installment number validation
        if (!List.of(6, 9, 12, 24).contains(loanRequestDto.getNumberOfInstallments())) {
            throw new IllegalArgumentException("Installment number must be 6, 9, 12, or 24.");
        }

        // Faiz oranını taksite göre güncelleme
        BigDecimal interestRate;
        switch (loanRequestDto.getNumberOfInstallments()) {
            case 6:
                interestRate = BigDecimal.valueOf(0.1); // 6 taksit için faiz oranı
                break;
            case 9:
                interestRate = BigDecimal.valueOf(0.2); // 9 taksit için faiz oranı
                break;
            case 12:
                interestRate = BigDecimal.valueOf(0.3); // 12 taksit için faiz oranı
                break;
            case 24:
                interestRate = BigDecimal.valueOf(0.4); // 24 taksit için faiz oranı
                break;
            default:
                throw new IllegalArgumentException("Invalid number of installments.");
        }

        // Total loan amount hesaplama
        BigDecimal totalLoanAmount = loanAmount.multiply(BigDecimal.ONE.add(interestRate));

        // Credit limit kontrolü
        BigDecimal remainingCredit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());

        if (remainingCredit.compareTo(totalLoanAmount) < 0) {
            throw new IllegalArgumentException("Customer does not have enough credit limit for this loan.");
        }

        System.out.println("Customer Credit Limit: " + customer.getCreditLimit());
        System.out.println("Customer Used Credit Limit: " + customer.getUsedCreditLimit());
        System.out.println("Remaining Credit: " + remainingCredit);
        System.out.println("Total Loan Amount: " + totalLoanAmount);

        // Create the loan
        Loan loan = new Loan();
        loan.setLoanAmount(totalLoanAmount);
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
    @Transactional
    public PaymentResponseDto payLoan(PaymentRequestDto paymentRequest) {
        System.out.println(">>> Ödeme işlemi başlatıldı...");

        Loan loan = loanRepository.findById(paymentRequest.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        List<LoanInstallment> installments = loanInstallmentRepository.findByLoanId(loan.getId());

        System.out.println(">>> Loan ID: " + loan.getId() + " için " + installments.size() + " taksit bulundu.");

        BigDecimal amountToPay = paymentRequest.getAmount();
        int paidInstallmentsCount = 0;

        System.out.println(">>> Ödenecek Tutar: " + amountToPay);

        installments.sort(Comparator.comparing(LoanInstallment::getDueDate));

        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
        List<LoanInstallment> payableInstallments = installments.stream()
                .filter(installment -> !installment.getDueDate().isBefore(LocalDate.now())
                        && installment.getDueDate().isBefore(threeMonthsLater)).collect(Collectors.toList());

        System.out.println(">>> 3 ay içinde vadesi gelen taksitler: " + payableInstallments.size());

        LocalDate paymentDate = LocalDate.now(); // Ödeme tarihi

        for (LoanInstallment installment : payableInstallments) {
            System.out.println(">>> Kontrol Edilen Taksit ID: " + installment.getId());
            System.out.println("    - Due Date: " + installment.getDueDate());
            System.out.println("    - Is Paid: " + installment.getIsPaid());

            if (installment.getIsPaid()) {
                System.out.println("    - Taksit zaten ödenmiş, geçiliyor.");
                continue;
            }

            BigDecimal installmentAmount = installment.getAmount();
            BigDecimal discount = BigDecimal.ZERO;
            BigDecimal penalty = BigDecimal.ZERO;

            long daysDifference = ChronoUnit.DAYS.between(installment.getDueDate(), paymentDate);

            if (daysDifference < 0) {
                // Erken ödeme indirimi
                long daysEarly = Math.abs(daysDifference);
                discount = installmentAmount.multiply(BigDecimal.valueOf(0.001))
                        .multiply(BigDecimal.valueOf(daysEarly));

                System.out.println("    - Erken Ödeme: " + daysEarly + " gün önce. İndirim: " + discount);
            } else if (daysDifference > 0) {
                // Geç ödeme cezası
                long daysLate = daysDifference;
                penalty = installmentAmount.multiply(BigDecimal.valueOf(0.001))
                        .multiply(BigDecimal.valueOf(daysLate));

                System.out.println("    - Geç Ödeme: " + daysLate + " gün sonra. Ceza: " + penalty);
            }

            // Ödenecek miktarı hesapla
            BigDecimal adjustedInstallmentAmount = installmentAmount.subtract(discount).add(penalty);
            BigDecimal remainingAmount = adjustedInstallmentAmount.subtract(installment.getPaidAmount());

            System.out.println("    - Güncellenmiş Borç: " + remainingAmount);

            if (amountToPay.compareTo(remainingAmount) >= 0) {
                installment.setPaidAmount(adjustedInstallmentAmount);
                amountToPay = amountToPay.subtract(remainingAmount);
                installment.setIsPaid(true);
                System.out.println("    - Taksit tamamen ödendi.");
            } else {
                System.out.println("    - Yeterli ödeme miktarı yok, bu taksit ödenemedi.");
                break;
            }

            loanInstallmentRepository.save(installment);
            paidInstallmentsCount++;

            System.out.println(">>> Güncellenen Taksit ID: " + installment.getId() + " için kalan ödeme: " + amountToPay);

            if (amountToPay.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println(">>> Tüm ödeme tamamlandı, döngüden çıkılıyor.");
                break;
            }
        }

        boolean allInstallmentsPaid = installments.stream().allMatch(LoanInstallment::getIsPaid);
        if (allInstallmentsPaid) {
            loan.setIsPaid(true);
            loanRepository.save(loan);
            System.out.println(">>> Tüm taksitler ödendi, loan kapandı.");
        }

        PaymentResponseDto response = new PaymentResponseDto();
        response.setLoanId(loan.getId());
        response.setAmountPaid(paymentRequest.getAmount().subtract(amountToPay));
        response.setPaidInstallments(paidInstallmentsCount);
        response.setTotalInstallments(installments.size());
        response.setLoanPaid(loan.getIsPaid());

        System.out.println(">>> Ödeme İşlemi Tamamlandı: " + response);

        return response;
    }
}
