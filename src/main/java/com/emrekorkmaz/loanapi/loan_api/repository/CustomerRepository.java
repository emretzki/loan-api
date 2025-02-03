package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByNameAndSurname(String name, String surname);

    List<Customer> findByCreditLimitGreaterThan(BigDecimal creditLimit);

    Optional<Customer> findByIdAndCreditLimitGreaterThanEqual(Long id, BigDecimal creditLimit);
}
