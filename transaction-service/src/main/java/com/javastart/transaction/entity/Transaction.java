package com.javastart.transaction.entity;

import com.javastart.transaction.dto.TransactionRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "transaction_date")
    private OffsetDateTime transactionDate;

    @Column(name = "email")
    private String email;

    public Transaction(BigDecimal amount, Long billId, String email){
        this.amount = amount;
        this.billId = billId;
        this.transactionDate = OffsetDateTime.now();
        this.email = email;
    }

}
