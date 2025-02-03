package com.emrekorkmaz.loanapi.loan_api;


import com.emrekorkmaz.loanapi.loan_api.service.LoanServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceHelper loanServiceHelper;

    @Test
    void calculateTotalLoan_WithValidInputs_ReturnsCorrectValue() {
        BigDecimal loanAmount = new BigDecimal("10000.00");
        BigDecimal interestRate = new BigDecimal("0.1");

        BigDecimal result = loanServiceHelper.calculateTotalLoanAmount(loanAmount, interestRate);

        assertThat(result).isEqualTo(new BigDecimal("11000.000"));
    }

    @Test
    void calculateRemainingCredit_WithNormalValues_ReturnsCorrectValue() {
        BigDecimal totalLimit = new BigDecimal("5000.00");
        BigDecimal usedLimit = new BigDecimal("2750.50");

        BigDecimal result = loanServiceHelper.calculateRemainingCredit(totalLimit, usedLimit);

        assertThat(result).isEqualTo(new BigDecimal("2249.50"));
    }

    @Test
    void calculateRemainingCredit_WithFullUsage_ReturnsZero() {
        BigDecimal totalLimit = new BigDecimal("3000.00");
        BigDecimal usedLimit = new BigDecimal("3000.00");

        BigDecimal result = loanServiceHelper.calculateRemainingCredit(totalLimit, usedLimit);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
