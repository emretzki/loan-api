package com.emrekorkmaz.loanapi.loan_api.controller;

import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerResponseDto;
import com.emrekorkmaz.loanapi.loan_api.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Yeni müşteri oluşturma
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto request) {
        CustomerResponseDto response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Tüm müşterileri listeleme
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // ID ile müşteri getirme
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Müşteri kredi limitini güncelleme
    @PutMapping("/{id}/update-limit")
    public ResponseEntity<Void> updateCreditLimit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        customerService.updateCreditLimit(id, amount);
        return ResponseEntity.noContent().build();
    }

    // Müşteri silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
