package com.javastart.deposit.controller;

import com.javastart.deposit.dto.DepositRequestDTO;
import com.javastart.deposit.service.DepositService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HttpStatus> makeDeposit(@RequestBody DepositRequestDTO depositRequestDTO){
        depositService.makeDeposit(depositRequestDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
