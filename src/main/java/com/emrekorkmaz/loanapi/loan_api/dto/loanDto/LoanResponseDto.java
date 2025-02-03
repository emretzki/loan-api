package com.emrekorkmaz.loanapi.loan_api.dto.loanDto;

import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanResponseDto {

    private Long id;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private LocalDate createDate;
    private int numberOfInstallments;
    private boolean isPaid;
    private Long customerId;

    public LoanResponseDto(Loan loan) {
        this.id = loan.getId();
        this.loanAmount = loan.getLoanAmount();
        this.interestRate = loan.getInterestRate();
        this.createDate = loan.getCreateDate();
        this.numberOfInstallments = loan.getNumberOfInstallments();
        this.isPaid = loan.getIsPaid();

        if (loan.getCustomer() != null) {
            this.customerId = loan.getCustomer().getId();
        } else {
            this.customerId = null;
        }
    }
}

