package com.javastart.notification.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionResponseDTO {

    private String email;

    private BigDecimal amount;
}
