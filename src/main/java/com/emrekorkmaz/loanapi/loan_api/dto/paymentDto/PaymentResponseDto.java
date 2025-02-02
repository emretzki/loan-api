package com.emrekorkmaz.loanapi.loan_api.dto.paymentDto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponseDto {
    private Long loanId;  // Loan ID
    private BigDecimal amountPaid;  // Ödenen toplam miktar
    private int paidInstallments;  // Ödenen taksit sayısı
    private int totalInstallments;  // Toplam taksit sayısı
    private boolean loanPaid;  // Loan tamamen ödendi mi?
}
