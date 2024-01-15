package com.javastart.account.controller;

import com.javastart.account.dto.AccountRequestDTO;
import com.javastart.account.dto.AccountResponseDTO;
import com.javastart.account.entity.Account;
import com.javastart.account.exception.AccountServiceException;
import com.javastart.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public AccountResponseDTO getAccount(@PathVariable(name = "id") Long id) {
        return new AccountResponseDTO(accountService.getAccountByID(id));
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> saveAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String message = createExceptionMessage(bindingResult);
            throw new AccountServiceException(message);
        } else {
            accountService.saveAccount(new Account(accountRequestDTO));
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateAccount(@RequestBody AccountRequestDTO accountRequestDTO,
                                                    @PathVariable(name = "id") Long id) {
        Account account = new Account(accountRequestDTO);
        account.setId(id);
        accountService.updateAccount(account);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String createExceptionMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();

        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; \n");
        }
        return message.toString();
    }
}
