package com.javastart.transfer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransferRequestDTO {

    @NotNull(message = "bill_from should not be null")
    private Long billFrom;

    @NotNull(message = "bill_to should not be null")
    private Long billTo;

    @Min(value = 1, message = "amount should be more then 0")
    private BigDecimal amount;

}
