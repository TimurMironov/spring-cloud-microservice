package com.javastart.account.dto;


import com.javastart.account.entity.Account;
import com.javastart.bill.dto.BillResponseDTO;
import com.javastart.bill.entity.Bill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponseDTO {

    private Long id;

    private String email;

    private String phone;


    public AccountResponseDTO(Account account){
        id = account.getId();
        email = account.getEmail();
        phone = account.getPhone();
    }
}
