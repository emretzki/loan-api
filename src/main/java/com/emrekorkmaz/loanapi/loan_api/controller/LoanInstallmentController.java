package com.emrekorkmaz.loanapi.loan_api.controller;

import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loan-installments")
public class LoanInstallmentController {

    private final LoanInstallmentService loanInstallmentService;

    @Autowired
    public LoanInstallmentController(LoanInstallmentService loanInstallmentService) {
        this.loanInstallmentService = loanInstallmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanInstallmentResponseDto>> getAllLoanInstallments() {
        List<LoanInstallmentResponseDto> loanInstallments = loanInstallmentService.getAllLoanInstallments();
        return ResponseEntity.ok(loanInstallments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanInstallmentResponseDto> getLoanInstallmentById(@PathVariable Long id) {
        LoanInstallmentResponseDto loanInstallmentResponse = loanInstallmentService.getLoanInstallmentById(id);
        return ResponseEntity.ok(loanInstallmentResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanInstallmentResponseDto> updateLoanInstallment(
            @PathVariable Long id, @Valid @RequestBody LoanInstallmentRequestDto loanInstallmentRequestDto) {
        LoanInstallmentResponseDto loanInstallmentResponse = loanInstallmentService.updateLoanInstallment(id, loanInstallmentRequestDto);
        return ResponseEntity.ok(loanInstallmentResponse);
    }

    //Get all installments of a loan
    @GetMapping("/by-loan/{loanId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanInstallmentResponseDto>> getLoanInstallmentsByLoanId(@PathVariable Long loanId) {
        List<LoanInstallmentResponseDto> loanInstallments = loanInstallmentService.getLoanInstallmentsByLoanId(loanId);
        return ResponseEntity.ok(loanInstallments);
    }
}
