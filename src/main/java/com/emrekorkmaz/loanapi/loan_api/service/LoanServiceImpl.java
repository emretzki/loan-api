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
    private final LoanInstallmentService loanInstallmentService;
    private final LoanServiceHelper loanServiceHelper;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository, LoanInstallmentRepository loanInstallmentRepository, LoanInstallmentService loanInstallmentService, LoanServiceHelper loanServiceHelper) {
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.customerRepository = customerRepository;
        this.loanInstallmentService = loanInstallmentService;
        this.loanServiceHelper = loanServiceHelper;
    }

    @Override
    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) {
        try {
            Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));

            BigDecimal loanAmount = loanRequestDto.getLoanAmount();

            BigDecimal interestRate = loanServiceHelper.getInterestRateForInstallments(loanRequestDto.getNumberOfInstallments());

            BigDecimal totalLoanAmount = loanServiceHelper.calculateTotalLoanAmount(loanAmount, interestRate);

            BigDecimal remainingCredit = loanServiceHelper.calculateRemainingCredit(customer.getCreditLimit(), customer.getUsedCreditLimit());

            if (remainingCredit.compareTo(totalLoanAmount) < 0) {
                throw new IllegalArgumentException("Customer does not have enough credit limit for this loan.");
            }

            System.out.println("Customer Credit Limit: " + customer.getCreditLimit());
            System.out.println("Customer Used Credit Limit: " + customer.getUsedCreditLimit());
            System.out.println("Remaining Credit: " + remainingCredit);
            System.out.println("Total Loan Amount: " + totalLoanAmount);

            Loan loan = new Loan();
            loan.setLoanAmount(totalLoanAmount);
            loan.setInterestRate(interestRate);
            loan.setCreateDate(loanRequestDto.getCreateDate());
            loan.setNumberOfInstallments(loanRequestDto.getNumberOfInstallments());
            loan.setCustomer(customer);
            loan.setIsPaid(false);

            Loan savedLoan = loanRepository.save(loan);

            createInstallments(savedLoan.getId(), loanRequestDto.getNumberOfInstallments());

            BigDecimal usedCreditLimit = customer.getUsedCreditLimit().add(totalLoanAmount);
            customer.setUsedCreditLimit(usedCreditLimit);
            customerRepository.save(customer);

            return new LoanResponseDto(savedLoan);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while processing the loan request.");
        }
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

        loan.setLoanAmount(loanRequestDto.getLoanAmount());
        loan.setCreateDate(loanRequestDto.getCreateDate());

        Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));
        loan.setCustomer(customer);

        Loan updatedLoan = loanRepository.save(loan);
        return new LoanResponseDto(updatedLoan);
    }

    @Override
    public void createInstallments(Long loanId, int numberOfInstallments) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + loanId + " not found"));

        loanInstallmentService.createInstallments(loan, numberOfInstallments);
    }

    public PaymentResponseDto payLoan(PaymentRequestDto paymentRequest) {
        try {
            Loan loan = loanRepository.findById(paymentRequest.getLoanId())
                    .orElseThrow(() -> new RuntimeException("Loan not found"));

            List<LoanInstallment> installments = loanInstallmentRepository.findByLoanIdAndIsPaidFalse(loan.getId());

            BigDecimal amountToPay = paymentRequest.getAmount();
            int paidInstallmentsCount = 0;

            installments.sort(Comparator.comparing(LoanInstallment::getDueDate));

            LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);
            List<LoanInstallment> payableInstallments = installments.stream()
                    .filter(installment -> !installment.getDueDate().isBefore(LocalDate.now())
                            && installment.getDueDate().isBefore(threeMonthsLater))
                    .collect(Collectors.toList());

            for (LoanInstallment installment : payableInstallments) {
                if (installment.getIsPaid()) {
                    continue;
                }

                BigDecimal remainingAmount = installment.getAmount();

                // Check if the installment is paid before the due date
                long daysBeforeDue = ChronoUnit.DAYS.between(LocalDate.now(), installment.getDueDate());
                if (daysBeforeDue > 0) {
                    // Discount for paying early
                    BigDecimal discount = installment.getAmount().multiply(BigDecimal.valueOf(0.001)).multiply(BigDecimal.valueOf(daysBeforeDue));
                    remainingAmount = remainingAmount.subtract(discount);
                } else {
                    // Check if the installment is paid after the due date
                    long daysAfterDue = ChronoUnit.DAYS.between(installment.getDueDate(), LocalDate.now());
                    if (daysAfterDue > 0) {
                        // Penalty for paying late
                        BigDecimal penalty = installment.getAmount().multiply(BigDecimal.valueOf(0.001)).multiply(BigDecimal.valueOf(daysAfterDue));
                        remainingAmount = remainingAmount.add(penalty);
                    }
                }

                if (amountToPay.compareTo(remainingAmount) >= 0) {
                    installment.setPaidAmount(remainingAmount);
                    amountToPay = amountToPay.subtract(remainingAmount);
                    installment.setIsPaid(true);
                    installment.setPaymentDate(LocalDate.now());
                    loanInstallmentRepository.save(installment);
                    paidInstallmentsCount++;
                } else {
                    break;
                }

                if (amountToPay.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }

            boolean allInstallmentsPaid = installments.stream().allMatch(LoanInstallment::getIsPaid);
            if (allInstallmentsPaid) {
                loan.setIsPaid(true);
                loanRepository.save(loan);
            }

            PaymentResponseDto response = new PaymentResponseDto();
            response.setLoanId(loan.getId());
            response.setAmountPaid(paymentRequest.getAmount().subtract(amountToPay));
            response.setPaidInstallments(paidInstallmentsCount);
            response.setTotalInstallments(installments.size());
            response.setLoanPaid(loan.getIsPaid());

            return response;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred during the payment process.");
        }
    }
}
