package com.emrekorkmaz.loanapi.loan_api.repository;

import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Ad ve soyada göre müşteri arama
    Optional<Customer> findByNameAndSurname(String name, String surname);

    // Belirli bir kredi limitine sahip müşterileri bulma
    List<Customer> findByCreditLimitGreaterThan(Double creditLimit);

    // ID ve limit kontrolü
    Optional<Customer> findByIdAndCreditLimitGreaterThanEqual(Long id, Double creditLimit);
}
