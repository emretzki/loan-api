package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanResponseDto;
import com.emrekorkmaz.loanapi.loan_api.dto.paymentDto.PaymentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.paymentDto.PaymentResponseDto;

import java.util.List;

public interface LoanService {

    LoanResponseDto createLoan(LoanRequestDto loanRequestDto);

    List<LoanResponseDto> getAllLoans();
    LoanResponseDto getLoanById(Long id);
    List<LoanResponseDto> getLoansByCustomer(Long customerId);
    List<LoanResponseDto> getLoansByInstallments(Integer numberOfInstallments);
    List<LoanResponseDto> getLoansByStatus(Boolean isPaid);

    LoanResponseDto updateLoan(Long id, LoanRequestDto loanRequestDto);

    void createInstallments(Long loanId, int numberOfInstallments);

    PaymentResponseDto payLoan(PaymentRequestDto paymentRequest);
}
