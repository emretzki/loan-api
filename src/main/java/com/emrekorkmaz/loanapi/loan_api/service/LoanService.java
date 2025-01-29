package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanResponseDto;

import java.util.List;

public interface LoanService {

    LoanResponseDto createLoan(LoanRequestDto loanRequestDto);
    List<LoanResponseDto> getAllLoans();
    LoanResponseDto getLoanById(Long id);
    LoanResponseDto updateLoan(Long id, LoanRequestDto loanRequestDto);
}
