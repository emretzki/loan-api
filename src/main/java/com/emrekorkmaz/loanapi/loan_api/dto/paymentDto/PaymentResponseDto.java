package com.emrekorkmaz.loanapi.loan_api.dto.paymentDto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponseDto {
    private Long loanId;
    private BigDecimal amountPaid;
    private int paidInstallments;
    private int totalInstallments;
    private boolean loanPaid;
}
