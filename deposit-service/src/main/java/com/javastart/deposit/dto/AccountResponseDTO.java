package com.javastart.deposit.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {

    private Long id;

    private String email;

    private String phone;
}
