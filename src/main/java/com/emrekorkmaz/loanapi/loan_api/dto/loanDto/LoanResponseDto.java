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
    private BigDecimal loanAmount;  // Kredi miktarı
    private BigDecimal interestRate;  // Faiz oranı
    private LocalDate createDate;  // Kredi başlangıç tarihi
    private int numberOfInstallments;
    private Long customerId;  // Müşteri ID'si

    public LoanResponseDto(Loan loan) {
        this.id = loan.getId();
        this.loanAmount = loan.getLoanAmount();
        this.interestRate = loan.getInterestRate();
        this.createDate = loan.getCreateDate();
        this.numberOfInstallments = loan.getNumberOfInstallments();

        // Eğer customer null ise, customerId'yi null yapıyoruz
        if (loan.getCustomer() != null) {
            this.customerId = loan.getCustomer().getId();
        } else {
            this.customerId = null;  // Müşteri yoksa customerId null
        }
    }
}

