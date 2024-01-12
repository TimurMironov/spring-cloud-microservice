package com.javastart.transaction.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionRequestDTO {

    private Long accountId;

    private Long billId;

    @Min(value = 1, message = "amount should be more than 0")
    private BigDecimal amount;
}
