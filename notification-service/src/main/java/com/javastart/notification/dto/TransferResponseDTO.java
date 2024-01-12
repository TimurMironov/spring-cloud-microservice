package com.javastart.notification.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferResponseDTO {

    private BigDecimal amount;

    private String emailFrom;

    private String emailTo;
}
