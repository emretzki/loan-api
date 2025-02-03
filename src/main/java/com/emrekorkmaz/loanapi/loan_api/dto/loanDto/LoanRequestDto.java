package com.emrekorkmaz.loanapi.loan_api.dto.loanDto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long customerId;

    @NotNull
    @Positive
    private BigDecimal loanAmount;

    @NotNull
    private int numberOfInstallments;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;
}
