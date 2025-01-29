package com.emrekorkmaz.loanapi.loan_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary Key

    @Column(nullable = false)
    private BigDecimal amount;  // Kredi miktarı

    @Column(nullable = false)
    private LocalDate startDate;  // Kredi başlangıç tarihi

    @Column(nullable = false)
    private BigDecimal interestRate;  // Faiz oranı

    @ManyToOne
    @JoinColumn(name = "customer_id")  // Foreign Key
    private Customer customer;

}
