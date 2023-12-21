package com.javastart.deposit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponseDTO {

    private Long id;

    private String email;

    private String phone;
}
