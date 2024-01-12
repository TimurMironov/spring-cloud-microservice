package com.javastart.transfer.rest;

import com.javastart.transfer.dto.BillRequestDTO;
import com.javastart.transfer.dto.BillResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient
public interface AccountServiceClient {

    @RequestMapping(value = "/accounts/bills/{id}", method = RequestMethod.GET)
    BillResponseDTO getBillById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/accounts/bills/{id}", method = RequestMethod.PUT)
    void updateBill(@PathVariable(name = "id") Long id, BillRequestDTO billRequestDTO);
}
