package com.javastart.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransferResponseDTO {

    private BigDecimal amount;

    private String emailFrom;

    private String emailTo;
}
