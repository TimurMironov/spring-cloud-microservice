package com.javastart.account.repository;

import com.javastart.account.entity.Account;
import com.javastart.bill.entity.Bill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
