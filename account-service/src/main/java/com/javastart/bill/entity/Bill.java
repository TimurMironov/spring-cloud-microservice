package com.javastart.bill.entity;

import com.javastart.account.entity.Account;
import com.javastart.bill.dto.BillRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long Id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "overdraft_enabled")
    private Boolean overdraftEnabled;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public Bill(BillRequestDTO billRequestDTO, Account account) {
        this.amount = billRequestDTO.getAmount();
        this.isDefault = billRequestDTO.getIsDefault();
        this.creationDate = OffsetDateTime.now();
        this.overdraftEnabled = billRequestDTO.getOverdraftEnabled();
        this.account = account;
    }
}
