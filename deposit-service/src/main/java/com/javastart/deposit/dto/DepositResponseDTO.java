package com.javastart.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class DepositResponseDTO {

    private String email;

    private BigDecimal amount;

}
