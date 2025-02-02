package com.emrekorkmaz.loanapi.loan_api.dto.loanInstallmentDto;

import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanInstallmentResponseDto {

    private Long id;  // Taksit ID'si
    private Long loanId;  // Kredi ID'si
    private BigDecimal amount;  // Taksit miktarı
    private BigDecimal paidAmount;  // Ödenen miktar
    private LocalDate dueDate;  // Ödeme tarihi
    private LocalDate paymentDate;  // Gerçek ödeme tarihi
    private Boolean isPaid;  // Ödenip ödenmediği bilgisi

    public LoanInstallmentResponseDto(LoanInstallment loanInstallment) {
        this.id = loanInstallment.getId();
        this.amount = loanInstallment.getAmount();
        this.paidAmount = loanInstallment.getPaidAmount();
        this.dueDate = loanInstallment.getDueDate();
        this.paymentDate = loanInstallment.getPaymentDate();
        this.isPaid = loanInstallment.getIsPaid();

        if (loanInstallment.getLoan() != null) {
            this.loanId = loanInstallment.getLoan().getId();
        } else {
            this.loanId = null;  // Müşteri yoksa customerId null
        }
    }
}