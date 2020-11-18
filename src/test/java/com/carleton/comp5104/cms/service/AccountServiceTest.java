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
    void registerAccountTest1() throws Exception {
        // email is not in table Person
        String email = "fake@uottawa.ca";
        Person person = personRepository.findByEmail(email);
        if (person != null) {
            throw new Exception("Precondition for test case: email is not in table Person.");
        }
        Map<String, Object> result = accountService.registerAccount(email);
        System.out.println(result);
        Boolean success = (Boolean) result.get("success");
        Assert.assertFalse(success);
    }


    @Test
    void registerAccountTest2() throws Exception {
        // email is already in table Account and email is authorized
        String email = "niladevine@uottawa.ca";
        Account account = accountRepository.findByEmail(email);
        if (account == null || account.getAccountStatus() == AccountStatus.unauthorized) {
            throw new Exception("Precondition for test case: email is already in table Account and email is authorized.");
        }
        Map<String, Object> result = accountService.registerAccount(email);
        System.out.println(result);
        Boolean success = (Boolean) result.get("success");
        Assert.assertFalse(success);
    }

    @Test
    void registerAccountTest3() throws Exception {
        // email is already in table Account but email is not authorized
        String email = "evelinebizier@uottawa.ca";
        Account account = accountRepository.findByEmail(email);
        if (account == null || account.getAccountStatus().equals(AccountStatus.unauthorized)) {
            throw new Exception("Precondition for test case: email is already in table Account, but email is not authorized.");
        }
        Map<String, Object> result = accountService.registerAccount(email);
        System.out.println(result);
        Boolean success = (Boolean) result.get("success");
        Assert.assertFalse(success);
    }

    @Test
    void registerAccountTest4() throws Exception {
        // email is in table Person, but not in table Account
        String email = "johnmillar@uottawa.ca";
        Person person = personRepository.findByEmail(email);
        Account account = accountRepository.findByEmail(email);
        if (person == null || account != null) {
            throw new Exception("Precondition for test case: email is in table Person, but not in table Account");
        }
        Map<String, Object> result = accountService.registerAccount(email);
        Boolean success = (Boolean) result.get("success");
        Assert.assertTrue(success);
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
