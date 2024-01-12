package com.javastart.deposit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "deposit")
@Getter
@Setter
@NoArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "deposit_date")
    private OffsetDateTime depositDate;

    @Column(name = "email")
    private String email;

    public Deposit(BigDecimal amount, Long billId, String email) {
        this.amount = amount;
        this.billId = billId;
        depositDate = OffsetDateTime.now();
        this.email = email;

    }
}
