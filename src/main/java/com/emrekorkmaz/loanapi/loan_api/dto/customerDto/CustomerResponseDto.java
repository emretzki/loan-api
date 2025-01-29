package com.emrekorkmaz.loanapi.loan_api.dto.customerDto;

import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String surname;
    private BigDecimal creditLimit;
    private BigDecimal usedCreditLimit;

    public CustomerResponseDto(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.creditLimit = customer.getCreditLimit();
        this.usedCreditLimit = customer.getUsedCreditLimit();
    }
}

