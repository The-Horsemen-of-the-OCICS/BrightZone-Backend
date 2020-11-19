package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void registerAccountTest() {
        String email = "rolandewalkup@uottawa.ca";
        Person person = personRepository.findByEmail(email);
        Account account = accountRepository.findByEmail(email);
        Map<String, Object> map = accountService.registerAccount(email);
        Boolean success = (Boolean) map.get("success");

        if (person == null || account != null) {
            Assert.assertFalse(success);
        } else {  // account == null
            Assert.assertTrue(success);
        }
        System.out.println(map);
    }

    @Test
    void loginTest() {
        String email = "niladevine@uottawa.ca";
        String password = "1234567";
        Map<String, Object> map = accountService.login(email, password);
        Boolean success = (Boolean) map.get("success");
        Account account = accountRepository.findByEmail(email);
        if (account == null || AccountStatus.unauthorized.equals(account.getAccountStatus())
                || !password.equals(account.getPassword())) {
            Assert.assertFalse(success);
        } else {
            Assert.assertTrue(success);
        }
        System.out.println(map);
    }
}
