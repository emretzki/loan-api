package com.emrekorkmaz.loanapi.loan_api.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
public class LoanServiceHelper {

    public LoanServiceHelper() {
    }

    public BigDecimal calculateTotalLoanAmount(BigDecimal loanAmount, BigDecimal interestRate) {
        return loanAmount.multiply(BigDecimal.ONE.add(interestRate));
    }

    public BigDecimal calculateRemainingCredit(BigDecimal creditLimit, BigDecimal usedCreditLimit) {
        return creditLimit.subtract(usedCreditLimit);
    }

    public BigDecimal getInterestRateForInstallments(int numberOfInstallments) {
        switch (numberOfInstallments) {
            case 6:
                return BigDecimal.valueOf(0.1); // 6 taksit için faiz oranı
            case 9:
                return BigDecimal.valueOf(0.20); // 9 taksit için faiz oranı
            case 12:
                return BigDecimal.valueOf(0.30); // 12 taksit için faiz oranı
            case 24:
                return BigDecimal.valueOf(0.40); // 24 taksit için faiz oranı
            default:
                throw new IllegalArgumentException("Invalid number of installments.");
        }
    }
}
