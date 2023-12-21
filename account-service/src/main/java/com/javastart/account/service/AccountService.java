package com.javastart.account.service;


import com.javastart.account.entity.Account;
import com.javastart.account.exception.AccountNotFoundException;
import com.javastart.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByID(Long id){
        return accountRepository.findById(id)
                .orElseThrow(()->new AccountNotFoundException("Account with id " + id + " not found"));
    }

    public List<Account> getAllAccounts(){
        return (List<Account>) accountRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    @Transactional(readOnly = false)
    public void updateAccount(Account account){
        accountRepository.save(account);
    }

    @Transactional(readOnly = false)
    public void deleteAccount(Long id){
        accountRepository.deleteById(id);
    }
}
