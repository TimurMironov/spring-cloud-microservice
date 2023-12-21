package com.javastart.bill.dto;


import com.javastart.account.dto.AccountResponseDTO;
import com.javastart.bill.entity.Bill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BillResponseDTO {

    private Long id;

    private BigDecimal amount;

    private Boolean isDefault;

    private OffsetDateTime creationDate;

    private Boolean overdraftEnabled;

    private AccountResponseDTO accountResponseDTO;

    public BillResponseDTO(Bill bill){
        id = bill.getId();
        amount = bill.getAmount();
        isDefault = bill.getIsDefault();
        creationDate = bill.getCreationDate();
        overdraftEnabled = bill.getOverdraftEnabled();
        accountResponseDTO = new AccountResponseDTO(bill.getAccount());
    }

}
