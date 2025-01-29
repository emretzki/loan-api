package com.emrekorkmaz.loanapi.loan_api.service;


import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto request);
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto getCustomerById(Long id);
    void updateCreditLimit(Long customerId, BigDecimal amount);
    void deleteCustomer(Long id);
}
