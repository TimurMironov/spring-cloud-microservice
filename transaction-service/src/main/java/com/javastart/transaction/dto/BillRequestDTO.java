package com.javastart.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class BillRequestDTO {

    private BigDecimal amount;

    private Boolean isDefault;

    private OffsetDateTime creationDate;

    private Boolean overdraftEnabled;

    private Long accountId;

    public BillRequestDTO(BillResponseDTO billResponseDTO) {
        amount = billResponseDTO.getAmount();
        isDefault = billResponseDTO.getIsDefault();
        creationDate = billResponseDTO.getCreationDate();
        overdraftEnabled = billResponseDTO.getOverdraftEnabled();
        accountId = billResponseDTO.getId();
    }
}
