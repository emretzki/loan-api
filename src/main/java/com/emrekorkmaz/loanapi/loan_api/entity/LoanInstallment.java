package com.emrekorkmaz.loanapi.loan_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_installments")
@Getter
@Setter
@NoArgsConstructor
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary Key

    @ManyToOne
    @JoinColumn(name = "loan_id")  // Foreign Key
    private Loan loan;

    @Column(nullable = false)
    private BigDecimal amount;  // Taksit miktarı

    @Column(nullable = false)
    private BigDecimal paidAmount;  // Ödenen miktar

    @Column(nullable = false)
    private LocalDate dueDate;  // Ödeme tarihi

    @Column(nullable = false)
    private LocalDate paymentDate;  // Gerçek ödeme tarihi

    @Column(nullable = false)
    private Boolean isPaid;  // Ödenip ödenmediği bilgisi
}
