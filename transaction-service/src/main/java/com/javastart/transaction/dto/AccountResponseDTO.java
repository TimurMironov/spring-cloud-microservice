package com.javastart.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;

    private String email;

    private String phone;
}
