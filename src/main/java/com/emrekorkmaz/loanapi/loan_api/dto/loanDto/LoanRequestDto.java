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
    private Long customerId;  // Müşteri ID'si

    @NotNull
    @Positive
    private BigDecimal loanAmount;  // Kredi miktarı

    @NotNull
    private int numberOfInstallments;

    @NotNull
    private LocalDate createDate;  // Kredi başlangıç tarihi

    //@NotNull
    //private Boolean isPaid;

    //@NotNull
    //@Positive
    //private BigDecimal interestRate;  // Faiz oranı

}
