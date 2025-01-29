package com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanInstallmentRequestDto {

    @NotNull
    private Long loanId;  // Kredi ID'si

    @NotNull
    @Positive
    private BigDecimal amount;  // Taksit miktarı

    @NotNull
    @Positive
    private BigDecimal paidAmount;  // Ödenen miktar

    @NotNull
    private LocalDate dueDate;  // Ödeme tarihi

    private LocalDate paymentDate;  // Gerçek ödeme tarihi

    private Boolean isPaid;  // Ödenip ödenmediği bilgisi
}
