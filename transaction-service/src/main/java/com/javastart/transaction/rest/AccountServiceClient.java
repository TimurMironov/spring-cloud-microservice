package com.javastart.transaction.rest;


import com.javastart.transaction.dto.BillRequestDTO;
import com.javastart.transaction.dto.BillResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountServiceClient {

    @RequestMapping(value = "/accounts/bills/{id}", method = RequestMethod.GET)
    BillResponseDTO getBillById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/accounts/bills/{id}", method = RequestMethod.PUT)
    void updateBill(@PathVariable("id") Long id, BillRequestDTO billRequestDTO);

    @RequestMapping(value = "/accounts/bills/findByAccount/{account_id}", method = RequestMethod.GET)
    List<BillResponseDTO> getBillsByAccountId(@PathVariable("account_id") Long accountId);
}
