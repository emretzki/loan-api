package com.emrekorkmaz.loanapi.loan_api.controller;

import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.service.LoanInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Yeni bir taksit oluşturma
    @PostMapping
    public ResponseEntity<LoanInstallmentResponseDto> createLoanInstallment(
            @Valid @RequestBody LoanInstallmentRequestDto loanInstallmentRequestDto) {
        LoanInstallmentResponseDto loanInstallmentResponse = loanInstallmentService.createLoanInstallment(loanInstallmentRequestDto);
        return new ResponseEntity<>(loanInstallmentResponse, HttpStatus.CREATED);
    }

    // Bütün taksitleri listeleme
    @GetMapping
    public ResponseEntity<List<LoanInstallmentResponseDto>> getAllLoanInstallments() {
        List<LoanInstallmentResponseDto> loanInstallments = loanInstallmentService.getAllLoanInstallments();
        return ResponseEntity.ok(loanInstallments);
    }

    // ID'ye göre taksiti getirme
    @GetMapping("/{id}")
    public ResponseEntity<LoanInstallmentResponseDto> getLoanInstallmentById(@PathVariable Long id) {
        LoanInstallmentResponseDto loanInstallmentResponse = loanInstallmentService.getLoanInstallmentById(id);
        return ResponseEntity.ok(loanInstallmentResponse);
    }

    // Taksit güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<LoanInstallmentResponseDto> updateLoanInstallment(
            @PathVariable Long id, @Valid @RequestBody LoanInstallmentRequestDto loanInstallmentRequestDto) {
        LoanInstallmentResponseDto loanInstallmentResponse = loanInstallmentService.updateLoanInstallment(id, loanInstallmentRequestDto);
        return ResponseEntity.ok(loanInstallmentResponse);
    }
}
