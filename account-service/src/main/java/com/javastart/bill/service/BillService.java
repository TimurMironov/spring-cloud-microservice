package com.javastart.bill.service;

import com.javastart.account.entity.Account;
import com.javastart.account.service.AccountService;
import com.javastart.bill.dto.BillRequestDTO;
import com.javastart.bill.entity.Bill;
import com.javastart.bill.exception.BillNotFoundException;
import com.javastart.bill.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BillService {

    private final BillRepository billRepository;

    private final AccountService accountService;

    @Autowired
    public BillService(BillRepository billRepository, AccountService accountService) {
        this.billRepository = billRepository;
        this.accountService = accountService;
    }

    public Bill getBillById(Long id){
        return billRepository.findById(id)
                .orElseThrow(()->new BillNotFoundException("Bill with id " + id + " not found"));
    }

    public List<Bill> getAllBills(){
        return billRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void saveBill(BillRequestDTO billRequestDTO){
        Account account = accountService.getAccountByID(billRequestDTO.getAccountId());
        billRepository.save(new Bill(billRequestDTO, account));
    }

    @Transactional(readOnly = false)
    public void updateBill(Long id, BillRequestDTO billRequestDTO){
        Account account = accountService.getAccountByID(billRequestDTO.getAccountId());
        Bill bill = new Bill(billRequestDTO, account);
        bill.setId(id);
        billRepository.save(bill);
    }

    @Transactional(readOnly = false)
    public void deleteBill(Long id){
        billRepository.deleteById(id);
    }

    public List<Bill> getBillsByAccountId(Long id){
        return billRepository.getBillsByAccountId(id);
    }
}
