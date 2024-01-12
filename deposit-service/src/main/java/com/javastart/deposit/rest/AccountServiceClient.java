package com.javastart.deposit.rest;

import com.javastart.deposit.dto.AccountResponseDTO;
import com.javastart.deposit.dto.BillRequestDTO;
import com.javastart.deposit.dto.BillResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @RequestMapping(value = "/accounts/bills/{id}", method = RequestMethod.GET)
    BillResponseDTO getBillById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    AccountResponseDTO getAccountById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
    void updateBill(@PathVariable(name = "id") Long id, BillRequestDTO billRequestDTO);

    @RequestMapping(value = "/bills/findByAccount/{account_id}", method = RequestMethod.GET)
    List<BillResponseDTO> getBillsByAccountId(@PathVariable(name = "account_id") Long accountId);
}
