package com.emrekorkmaz.loanapi.loan_api.dto.customerDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerRequestDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    private String surname;

    @NotNull(message = "Credit limit is required")
    @Min(value = 1, message = "Credit limit must be greater than zero")
    private BigDecimal creditLimit;
}
