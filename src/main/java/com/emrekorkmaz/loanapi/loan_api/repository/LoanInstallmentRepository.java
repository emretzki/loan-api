package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    // Belirli bir loanId'ye sahip tüm taksitleri al
    List<LoanInstallment> findByLoanId(Long loanId);

    // Belirli bir loanId'ye sahip ve ödeme yapılmamış taksitleri al
    List<LoanInstallment> findByLoanIdAndIsPaidFalse(Long loanId);

    // Ödeme tarihi geçmiş ve ödeme yapılmamış taksitleri al
    List<LoanInstallment> findByDueDateBeforeAndIsPaidFalse(LocalDate dueDate);

    // Ödeme yapılmış tüm taksitleri al
    List<LoanInstallment> findByIsPaidTrue();

    // Belirli bir loanId'ye sahip ve ödeme tarihi belirli bir tarihe yakın taksiti al
    Optional<LoanInstallment> findTopByLoanIdAndDueDateAfterOrderByDueDateAsc(Long loanId, LocalDate dueDate);

}
