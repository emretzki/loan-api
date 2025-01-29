package com.emrekorkmaz.loanapi.loan_api.dto.loanDto;

import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanResponseDto {

    private Long id;  // Kredi ID'si
    private BigDecimal amount;  // Kredi miktarı
    private BigDecimal interestRate;  // Faiz oranı
    private LocalDate startDate;  // Kredi başlangıç tarihi
    private Long customerId;  // Müşteri ID'si

    public LoanResponseDto(Loan loan) {
        this.id = loan.getId();
        this.amount = loan.getAmount();
        this.interestRate = loan.getInterestRate();
        this.startDate = loan.getStartDate();

        // Eğer customer null ise, customerId'yi null yapıyoruz
        if (loan.getCustomer() != null) {
            this.customerId = loan.getCustomer().getId();
        } else {
            this.customerId = null;  // Müşteri yoksa customerId null
        }
    }
}

