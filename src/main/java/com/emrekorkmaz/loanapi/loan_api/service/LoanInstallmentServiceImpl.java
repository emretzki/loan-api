package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto.LoanInstallmentResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import com.emrekorkmaz.loanapi.loan_api.repository.LoanInstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public LoanInstallmentResponseDto createLoanInstallment(LoanInstallmentRequestDto loanInstallmentRequestDto) {
        // LoanInstallment nesnesini DTO'dan oluştur
        LoanInstallment loanInstallment = new LoanInstallment();
        loanInstallment.setAmount(loanInstallmentRequestDto.getAmount());
        loanInstallment.setDueDate(loanInstallmentRequestDto.getDueDate());
        loanInstallment.setPaymentDate(loanInstallmentRequestDto.getPaymentDate());
        loanInstallment.setIsPaid(loanInstallmentRequestDto.getIsPaid());

        // LoanInstallment nesnesini veritabanına kaydet
        loanInstallment = loanInstallmentRepository.save(loanInstallment);

        // Kaydedilen LoanInstallment nesnesini Response DTO'ya çevir
        return new LoanInstallmentResponseDto(loanInstallment);
    }

    @Override
    public List<LoanInstallmentResponseDto> getAllLoanInstallments() {
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findAll();

        // LoanInstallment listesini Response DTO listesine dönüştür
        return loanInstallments.stream()
                .map(LoanInstallmentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public LoanInstallmentResponseDto getLoanInstallmentById(Long id) {
        LoanInstallment loanInstallment = loanInstallmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LoanInstallment with ID " + id + " not found"));

        // LoanInstallment nesnesini Response DTO'ya çevir
        return new LoanInstallmentResponseDto(loanInstallment);
    }

    @Override
    public LoanInstallmentResponseDto updateLoanInstallment(Long id, LoanInstallmentRequestDto loanInstallmentRequestDto) {
        LoanInstallment loanInstallment = loanInstallmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LoanInstallment with ID " + id + " not found"));

        // Taksiti güncelle
        loanInstallment.setAmount(loanInstallmentRequestDto.getAmount());
        loanInstallment.setDueDate(loanInstallmentRequestDto.getDueDate());
        loanInstallment.setPaymentDate(loanInstallmentRequestDto.getPaymentDate());
        loanInstallment.setIsPaid(loanInstallmentRequestDto.getIsPaid());

        // Güncellenmiş LoanInstallment nesnesini kaydet
        loanInstallment = loanInstallmentRepository.save(loanInstallment);

        // Güncellenmiş LoanInstallment nesnesini Response DTO'ya çevir
        return new LoanInstallmentResponseDto(loanInstallment);
    }

    @Override
    public List<LoanInstallmentResponseDto> getLoanInstallmentsByLoanId(Long loanId) {
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findByLoanId(loanId);

        return loanInstallments.stream()
                .map(LoanInstallmentResponseDto::new)
                .collect(Collectors.toList());
    }
}
