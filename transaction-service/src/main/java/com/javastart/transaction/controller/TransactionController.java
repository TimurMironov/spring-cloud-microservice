package com.javastart.transaction.controller;

import com.javastart.transaction.dto.TransactionRequestDTO;
import com.javastart.transaction.exception.TransactionServiceException;
import com.javastart.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> makeTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorsMessage = createErrorsMessage(bindingResult);
            throw new TransactionServiceException(errorsMessage);
        } else {
            transactionService.makeTransaction(transactionRequestDTO);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    private String createErrorsMessage(BindingResult bindingResult) {
        StringBuilder errorsMessage = new StringBuilder();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errorsMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(" \n");
        }
        return errorsMessage.toString();
    }
}
