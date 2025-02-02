package com.emrekorkmaz.loanapi.loan_api.dto.paymentDto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    private Long loanId;  // Loan ID
    private BigDecimal amount;  // Ödenecek tutar
}