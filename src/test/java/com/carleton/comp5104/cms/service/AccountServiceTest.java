package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Test
    void registerAccountTest() {
        String email = "rolandewalkup@uottawa.ca";
        Optional<Person> optionalPerson = personRepository.findByEmail(email);
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Map<String, Object> map = accountService.registerAccount(email);
        Boolean success = (Boolean) map.get("success");

        if (optionalPerson.isEmpty() || optionalAccount.isPresent()) {
            Assertions.assertFalse(success);
        } else {  // account == null
            Assertions.assertTrue(success);
        }
        System.out.println(map);
    }

    @Test
    void loginTest() {
        String email = "niladevine@uottawa.ca";
        String password = "1234567";
        Map<String, Object> map = accountService.login(email, password);
        Boolean success = (Boolean) map.get("success");
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty() || AccountStatus.unauthorized.equals(optionalAccount.get().getAccountStatus())
                || !password.equals(optionalAccount.get().getPassword())) {
            Assertions.assertFalse(success);
        } else {
            Assertions.assertTrue(success);
        }
        System.out.println(map);
    }

    @Test
    void createRequestTest() {
        int accountId = 1000000;
        String requestMessage = "My name is John, please help me enrol in course CSI 5138, thanks!";
        String requestType = "enroll";
        Map<String, Object> map = accountService.createRequest(accountId, requestMessage, requestType);
        boolean success = (boolean) map.get("success");
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (!optionalAccount.isPresent()) {
            Assert.assertFalse(success);
        } else {
            Account account = optionalAccount.get();
            if (AccountStatus.unauthorized.equals(account.getAccountStatus())) {
                Assert.assertFalse(success);
            } else {
                Assert.assertTrue(success);
            }
        }
        System.out.println(map);
    }

    @Test
    void passwordRecoveryTest() {
        String email = "niladevine@uottawa.ca";
        String verificationCode = "888";
        String newPassword = "1234567";
        Map<String, Object> map = accountService.passwordRecovery(email, verificationCode, newPassword);
        boolean success = (boolean) map.get("success");
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(verificationCode) || StringUtils.isEmpty(newPassword)) {
            Assertions.assertFalse(success);
        } else {
            Optional<Account> optionalAccount = accountRepository.findByEmail(email);
            if (optionalAccount.isEmpty()
                    || AccountStatus.unauthorized.equals(optionalAccount.get().getAccountStatus())
                    || !verificationCode.equals(optionalAccount.get().getVerificationCode())) {
                Assertions.assertFalse(success);
            } else {
                Assertions.assertTrue(success);
            }
        }
        System.out.println(map);
    }

    @Test
    void sendVerificationCodeTest() {
        String email = "shupercival@uottawa.ca";
        Map<String, Object> map = accountService.sendVerificationCode(email);
        boolean success = (boolean) map.get("success");
        if (StringUtils.isEmpty(email) || !accountRepository.existsAccountByEmail(email)) {
            Assertions.assertFalse(success);
        } else {
            String verificationCode = (String) map.get("verificationCode");
            String saved = accountRepository.findByEmail(email).get().getVerificationCode();
            Assertions.assertEquals(verificationCode, saved);
        }
        System.out.println(map);
    }

    @Test
    void updateEmailTest() {
        String email = "niladevine@uottawa.ca";
        int accountId = 1000000;
        Map<String, Object> map = accountService.updateEmail(accountId, email);
        boolean success = (boolean) map.get("success");
        if (StringUtils.isEmpty(email) || !accountRepository.existsById(accountId) ||
                AccountStatus.unauthorized.equals(accountRepository.findById(accountId).get().getAccountStatus())) {
            Assertions.assertFalse(success);
        } else {
            Assertions.assertTrue(success);
        }
        System.out.println(map);
    }

    @Test
    void updatePasswordTest() {
        String password = "1234567";
        int accountId = 1000000;
        Map<String, Object> map = accountService.updatePassword(accountId, password);
        boolean success = (boolean) map.get("success");
        if (StringUtils.isEmpty(password) || !accountRepository.existsById(accountId) ||
                AccountStatus.unauthorized.equals(accountRepository.findById(accountId).get().getAccountStatus())) {
            Assert.assertFalse(success);
        } else {
            Assert.assertTrue(success);
        }
        System.out.println(map);
    }
}
