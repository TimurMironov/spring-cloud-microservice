package com.javastart.account.entity;


import com.javastart.account.dto.AccountRequestDTO;
import com.javastart.bill.entity.Bill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Bill> bills;

    public Account(AccountRequestDTO accountRequestDTO){
        email=accountRequestDTO.getEmail();
        phone=accountRequestDTO.getPhone();
        creationDate=OffsetDateTime.now();
    }
}

