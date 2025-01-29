package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Müşteri ID'sine göre tüm kredileri listeleme
    List<Loan> findByCustomerId(Long customerId);

    // Ödenmemiş kredileri listeleme (isPaid veya benzeri bir alan varsa)
    // Boolean isPaid: List<Loan> findByIsPaid(Boolean isPaid);

    // Başlangıç tarihi belirli bir aralıkta olan kredileri listeleme
    List<Loan> findByCreateDateBetween(LocalDate startDate, LocalDate endDate);

    // Faiz oranına göre kredileri listeleme
    List<Loan> findByInterestRateLessThanEqual(BigDecimal interestRate);

    // Miktarına göre kredileri listeleme
    List<Loan> findByLoanAmountGreaterThan(BigDecimal amount);
}
