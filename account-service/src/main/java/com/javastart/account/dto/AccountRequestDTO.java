package com.javastart.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    @Email(message = "Invalid mail format. Mail should have format like example@mail.com")
    private String email;

    @Pattern(regexp = "^\\d{11}$", message = "Invalid number format")
    private String phone;


}
