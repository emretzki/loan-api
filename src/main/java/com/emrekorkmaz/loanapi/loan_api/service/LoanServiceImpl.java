package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.loanDto.LoanResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import com.emrekorkmaz.loanapi.loan_api.repository.CustomerRepository;
import com.emrekorkmaz.loanapi.loan_api.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) {
        // Müşteri doğrulaması yapalım
        Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));

        // Yeni Loan nesnesini oluştur
        Loan loan = new Loan();
        loan.setAmount(loanRequestDto.getAmount());
        loan.setInterestRate(loanRequestDto.getInterestRate());
        loan.setStartDate(loanRequestDto.getStartDate());
        loan.setCustomer(customer);  // Customer'ı ata

        // Loan'ı kaydedelim ve LoanResponseDto'yu döndürelim
        Loan savedLoan = loanRepository.save(loan);
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
    public LoanResponseDto updateLoan(Long id, LoanRequestDto loanRequestDto) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan with ID " + id + " not found"));

        // Güncelleme işlemleri
        loan.setAmount(loanRequestDto.getAmount());
        loan.setInterestRate(loanRequestDto.getInterestRate());
        loan.setStartDate(loanRequestDto.getStartDate());

        // Müşteri doğrulaması
        Customer customer = customerRepository.findById(loanRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + loanRequestDto.getCustomerId() + " not found"));
        loan.setCustomer(customer);

        // Güncellenmiş loan'ı kaydedelim ve döndürelim
        Loan updatedLoan = loanRepository.save(loan);
        return new LoanResponseDto(updatedLoan);
    }
}
