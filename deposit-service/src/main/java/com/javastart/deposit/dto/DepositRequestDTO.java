package com.javastart.deposit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DepositRequestDTO {

    private Long accountId;

    private Long billId;

    private BigDecimal amount;
}
