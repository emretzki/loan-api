package com.emrekorkmaz.loanapi.loan_api.service;

import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerRequestDto;
import com.emrekorkmaz.loanapi.loan_api.dto.customerDto.CustomerResponseDto;
import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import com.emrekorkmaz.loanapi.loan_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setUsedCreditLimit(BigDecimal.ZERO);

        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponseDto(savedCustomer);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return new CustomerResponseDto(customer);
    }

    @Override
    public void updateCreditLimit(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        BigDecimal newUsedLimit = customer.getUsedCreditLimit().add(amount);
        if (newUsedLimit.compareTo(customer.getCreditLimit()) > 0) {
            throw new RuntimeException("Credit limit exceeded!");
        }

        customer.setUsedCreditLimit(newUsedLimit);
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customerRepository.delete(customer);
    }
}
