package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import com.emrekorkmaz.loanapi.loan_api.repository.LoanInstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanInstallmentServiceImpl implements LoanInstallmentService {

    private final LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    public LoanInstallmentServiceImpl(LoanInstallmentRepository loanInstallmentRepository) {
        this.loanInstallmentRepository = loanInstallmentRepository;
    }

    @Override
    public List<LoanInstallmentResponseDto> getAllLoanInstallments() {
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findAll();

        return loanInstallments.stream()
                .map(LoanInstallmentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public LoanInstallmentResponseDto getLoanInstallmentById(Long id) {
        try {
            LoanInstallment loanInstallment = loanInstallmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("LoanInstallment with ID " + id + " not found"));

            return new LoanInstallmentResponseDto(loanInstallment);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching the loan installment.");
        }
    }


    @Override
    public LoanInstallmentResponseDto updateLoanInstallment(Long id, LoanInstallmentRequestDto loanInstallmentRequestDto) {
        try {
            LoanInstallment loanInstallment = loanInstallmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("LoanInstallment with ID " + id + " not found"));

            loanInstallment.setAmount(loanInstallmentRequestDto.getAmount());
            loanInstallment.setDueDate(loanInstallmentRequestDto.getDueDate());
            loanInstallment.setPaymentDate(loanInstallmentRequestDto.getPaymentDate());
            loanInstallment.setIsPaid(loanInstallmentRequestDto.getIsPaid());

            loanInstallment = loanInstallmentRepository.save(loanInstallment);

            return new LoanInstallmentResponseDto(loanInstallment);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while updating the loan installment.", e);
        }
    }

    @Override
    public List<LoanInstallmentResponseDto> getLoanInstallmentsByLoanId(Long loanId) {
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findByLoanId(loanId);

        return loanInstallments.stream()
                .map(LoanInstallmentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void createInstallments(Loan loan, int numberOfInstallments) {
        BigDecimal installmentAmount = loan.getLoanAmount().divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
        LocalDate dueDate = loan.getCreateDate().withDayOfMonth(1).plusMonths(1);

        for (int i = 1; i <= numberOfInstallments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setDueDate(dueDate);
            installment.setIsPaid(false);
            installment.setPaidAmount(BigDecimal.ZERO);
            installment.setPaymentDate(null);

            loanInstallmentRepository.save(installment);
            dueDate = dueDate.plusMonths(1);
        }
    }
}
