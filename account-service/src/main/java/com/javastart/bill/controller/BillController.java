package com.javastart.bill.controller;

import com.javastart.bill.dto.BillRequestDTO;
import com.javastart.bill.dto.BillResponseDTO;
import com.javastart.bill.entity.Bill;
import com.javastart.bill.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills/{id}")
    public BillResponseDTO getBillById(@PathVariable(name = "id") Long id){
        return new BillResponseDTO(billService.getBillById(id));
    }

    @GetMapping("/bills/findByAccount/{account_id}")
    public List<BillResponseDTO> getBillsByAccountId(@PathVariable(name = "account_id") Long accountId){
        return billService.getBillsByAccountId(accountId).stream()
                .map(BillResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/bills")
    public ResponseEntity<HttpStatus> saveBill(@RequestBody BillRequestDTO billRequestDTO){
        billService.saveBill(billRequestDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/bills/{id}")
    public ResponseEntity<HttpStatus> updateBill(@PathVariable(name = "id") Long id,
                                                 @RequestBody BillRequestDTO billRequestDTO){
        billService.updateBill(id, billRequestDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/bills/{id}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable(name = "id") Long id){
        billService.deleteBill(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
