package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class AdminAccountServiceTest {

    @Autowired
    private AdminAccountService adminAccountService;

    @Test
    void getAllAccountByTypeTest() {
        String accountType = "professor";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByType(accountType, pageNum, pageSize);
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    void getAllAccountByNameTest() {
        String name = "Nila Devine";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByName(name, pageNum, pageSize);
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    void getAllAccountByTypeAndNameTest() {
        String accountType = "administrator";
        String name = "Nila Devine";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByTypeAndName(accountType, name, pageNum, pageSize);
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

}
