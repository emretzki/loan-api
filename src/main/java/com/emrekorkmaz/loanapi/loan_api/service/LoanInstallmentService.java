package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.Loan;

import java.util.List;

public interface LoanInstallmentService {


    List<LoanInstallmentResponseDto> getAllLoanInstallments();

    LoanInstallmentResponseDto getLoanInstallmentById(Long id);

    LoanInstallmentResponseDto updateLoanInstallment(Long id, LoanInstallmentRequestDto loanInstallmentRequestDto);

    List<LoanInstallmentResponseDto> getLoanInstallmentsByLoanId(Long loanId);

    void createInstallments(Loan loan, int numberOfInstallments);

}
