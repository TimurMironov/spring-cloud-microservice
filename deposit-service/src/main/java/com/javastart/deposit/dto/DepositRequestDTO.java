package com.javastart.deposit.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestDTO {

    private Long accountId;

    private Long billId;

    @Min(value = 1, message = "amount should be more then 0")
    private BigDecimal amount;
}
