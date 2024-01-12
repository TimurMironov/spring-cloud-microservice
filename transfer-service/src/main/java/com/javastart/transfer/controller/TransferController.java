package com.javastart.transfer.controller;

import com.javastart.transfer.dto.TransferRequestDTO;
import com.javastart.transfer.exception.TransferServiceException;
import com.javastart.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(name = "/")
    public ResponseEntity<HttpStatus> makeTransfer(@RequestBody @Valid TransferRequestDTO transferRequestDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            String message = createErrorsMessage(bindingResult);
            throw new TransferServiceException(message);
        } else {
            transferService.makeTransfer(transferRequestDTO);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }


    private String createErrorsMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(" \n");
        }
        return message.toString();
    }
}
