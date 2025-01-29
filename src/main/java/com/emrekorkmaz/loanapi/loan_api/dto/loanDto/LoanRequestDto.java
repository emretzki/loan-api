package com.emrekorkmaz.loanapi.loan_api.dto.loanDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanRequestDto {

    @NotNull
    @Positive
    private BigDecimal amount;  // Kredi miktarı

    @NotNull
    @Positive
    private BigDecimal interestRate;  // Faiz oranı

    @NotNull
    private LocalDate startDate;  // Kredi başlangıç tarihi

    @NotNull
    private Long customerId;  // Müşteri ID'si
}
