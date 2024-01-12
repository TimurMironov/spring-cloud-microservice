package com.javastart.transfer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_transfer_date")
    private OffsetDateTime creationTransferDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "bill_id_from")
    private Long billIdFrom;

    @Column(name = "bill_id_to")
    private Long billIdTo;

    @Column(name = "email_from")
    private String emailFrom;

    @Column(name = "email_to")
    private String emailTo;

    public Transfer(BigDecimal amount, Long billIdFrom, Long billIdTo, String emailFrom, String emailTo) {
        this.creationTransferDate = OffsetDateTime.now();
        this.amount = amount;
        this.billIdFrom = billIdFrom;
        this.billIdTo = billIdTo;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
    }
}
