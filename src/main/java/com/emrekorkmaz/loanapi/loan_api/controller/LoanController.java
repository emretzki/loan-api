package com.emrekorkmaz.loanapi.loan_api.controller;

import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanResponseDto;
import com.emrekorkmaz.loanapi.loan_api.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponseDto> createLoan(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        LoanResponseDto loanResponse = loanService.createLoan(loanRequestDto);
        return new ResponseEntity<>(loanResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> getAllLoans() {
        List<LoanResponseDto> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable Long id) {
        LoanResponseDto loanResponse = loanService.getLoanById(id);
        return ResponseEntity.ok(loanResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDto> updateLoan(@PathVariable Long id, @Valid @RequestBody LoanRequestDto loanRequestDto) {
        LoanResponseDto loanResponse = loanService.updateLoan(id, loanRequestDto);
        return ResponseEntity.ok(loanResponse);
    }
}

