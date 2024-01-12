package com.javastart.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DepositResponseDTO {

    private String email;

    private BigDecimal amount;

}
