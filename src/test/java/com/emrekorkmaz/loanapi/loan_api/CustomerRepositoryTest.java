package com.emrekorkmaz.loanapi.loan_api;

import com.emrekorkmaz.loanapi.loan_api.entity.Customer;
import com.emrekorkmaz.loanapi.loan_api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testInsertAndFetchCustomer() {
        // Veritabanındaki mevcut kayıtları kontrol et
        Optional<Customer> existingCustomer = customerRepository.findById(1L);

        if (existingCustomer.isPresent()) {
            System.out.println("Customer already exists: " + existingCustomer.get());
        } else {
            // Eğer yoksa, yeni müşteri ekle
            Customer customer = new Customer();
            customer.setName("Emre");
            customer.setSurname("Korkmaz");
            //customer.setCreditLimit(10000);
            //customer.setUsedCreditLimit(2500);

            customerRepository.save(customer);
            System.out.println("Customer saved: " + customer);
        }

        // Veritabanında müşterinin olduğunu kontrol et
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(customer -> System.out.println("Fetched customer: " + customer));
    }
}
