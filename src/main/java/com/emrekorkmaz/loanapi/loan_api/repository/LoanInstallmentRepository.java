package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoanId(Long loanId);

    List<LoanInstallment> findByLoanIdAndIsPaidFalse(Long loanId);

    List<LoanInstallment> findByDueDateBeforeAndIsPaidFalse(LocalDate dueDate);

    List<LoanInstallment> findByIsPaidTrue();

    Optional<LoanInstallment> findTopByLoanIdAndDueDateAfterOrderByDueDateAsc(Long loanId, LocalDate dueDate);

}
