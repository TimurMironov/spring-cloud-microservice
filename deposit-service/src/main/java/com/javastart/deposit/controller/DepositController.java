package com.javastart.deposit.controller;

import com.javastart.deposit.dto.DepositRequestDTO;
import com.javastart.deposit.exception.DepositServiceException;
import com.javastart.deposit.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> makeDeposit(@RequestBody @Valid DepositRequestDTO depositRequestDTO
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorsMessage = createErrorsMessage(bindingResult);
            throw new DepositServiceException(errorsMessage);
        } else {
            depositService.makeDeposit(depositRequestDTO);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    private String createErrorsMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(" \n");
        }
        return message.toString();
    }
}
