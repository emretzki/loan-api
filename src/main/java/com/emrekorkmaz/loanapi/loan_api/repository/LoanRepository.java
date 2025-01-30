package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByCustomerId(Long customerId);

    List<Loan> findByCreateDateBetween(LocalDate startDate, LocalDate endDate);

    List<Loan> findByInterestRateLessThanEqual(BigDecimal interestRate);


    List<Loan> findByNumberOfInstallments(Integer numberOfInstallments);

    List<Loan> findByIsPaid(Boolean isPaid);

    List<Loan> findByLoanAmountGreaterThan(BigDecimal amount);
}
