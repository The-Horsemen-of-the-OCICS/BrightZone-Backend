package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.repository.RequestRepository;
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
    private RequestRepository requestRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Test
    void registerAccountTest() {
        // scenario 1: Register fail because user is not a member in this university
        int userId = 123;
        Optional<Person> optionalPerson = personRepository.findById(userId);
        Assertions.assertTrue(optionalPerson.isEmpty());
        Map<String, Object> resultMap = accountService.registerAccount(Integer.toString(userId));
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 2: Register fail because user already has an account but hasn't been authorized yet
        userId = 3000190;
        Optional<Account> optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        resultMap = accountService.registerAccount(Integer.toString(userId));
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 3: Register fail because user already has an account and has already been authorized
        userId = 1000000;
        optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertNotEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        resultMap = accountService.registerAccount(Integer.toString(userId));
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 4: Register success
        userId = 3000490;
        optionalPerson = personRepository.findById(userId);
        optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalPerson.isPresent());  // in table Person
        Assertions.assertFalse(optionalAccount.isPresent());  // not in table Account
        resultMap = accountService.registerAccount(Integer.toString(userId));
        Assertions.assertTrue((Boolean) resultMap.get("success"));
        accountRepository.deleteById(userId);
    }

    @Test
    void loginTest() {
        // scenario 1: Login fail because user doesn't have an account in CMS
        int userId = 123;
        String password = "123456";
        Optional<Account> optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isEmpty());
        Map<String, Object> resultMap = accountService.login(Integer.toString(userId), password);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 2: Login fail because userId is not authorized yet
        userId = 3000193;
        password = "123456";
        optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        resultMap = accountService.login(Integer.toString(userId), password);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 3: Login fail because password is wrong
        userId = 1000000;
        password = "123456789";
        optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertNotEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        Assertions.assertNotEquals(password, optionalAccount.get().getPassword());
        resultMap = accountService.login(Integer.toString(userId), password);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 4: Login success
        userId = 1000000;
        password = "123456";
        optionalAccount = accountRepository.findById(userId);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertNotEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        Assertions.assertEquals(password, optionalAccount.get().getPassword());
        resultMap = accountService.login(Integer.toString(userId), password);
        Assertions.assertTrue((Boolean) resultMap.get("success"));
    }

    @Test
    void createRequestTest() {
        int userId = 3000001;
        String requestMessage = "My name is Herma Harrow, please help me enrol in course CSI 5137A, thanks!";
        String requestType = "enroll";
        Map<String, Object> map = accountService.createRequest(userId, requestMessage, requestType);
        boolean success = (boolean) map.get("success");
        Assertions.assertTrue(success);

        Request saved = (Request) map.get("request");
        Integer requestId = saved.getRequestId();
        Assertions.assertTrue(requestRepository.existsById(requestId));
        System.out.println(map);
        requestRepository.deleteById(requestId);
    }

    @Test
    void passwordRecoveryTest() {
        // scenario 1: Password recovery fail because user doesn't have an account in CMS
        String email = "mock123@uottawa.ca";
        String verificationCode = "888";
        String newPassword = "1234567";
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isEmpty());
        Map<String, Object> resultMap = accountService.passwordRecovery(email, verificationCode, newPassword);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 2: Password recovery fail because email has not been authorized by admin yet
        email = "edwinaphillip@uottawa.ca";
        verificationCode = "888";
        newPassword = "1234567";
        optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        resultMap = accountService.passwordRecovery(email, verificationCode, newPassword);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // Precondition: Set verification code to 234567
        email = "niladevine@uottawa.ca";
        optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        Account account = optionalAccount.get();
        account.setVerificationCode("234567");
        accountRepository.save(account);

        // scenario 3: Password recovery fail because verification code is wrong
        verificationCode = "123456";
        newPassword = "1234567";
        resultMap = accountService.passwordRecovery(email, verificationCode, newPassword);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 4: Password recovery success
        verificationCode = "234567";
        newPassword = "1234567";
        resultMap = accountService.passwordRecovery(email, verificationCode, newPassword);
        Assertions.assertTrue((Boolean) resultMap.get("success"));

        // Reset verification code to NONE
        account.setVerificationCode("NONE");
        accountRepository.save(account);
    }

    @Test
    void sendVerificationCodeTest() {
//        String email = "shupercival@uottawa.ca";
//        Map<String, Object> map = accountService.sendVerificationCode(email);
//        boolean success = (boolean) map.get("success");
//        if (StringUtils.isEmpty(email) || !accountRepository.existsAccountByEmail(email)) {
//            Assertions.assertFalse(success);
//        } else {
//            String verificationCode = (String) map.get("verificationCode");
//            String saved = accountRepository.findByEmail(email).get().getVerificationCode();
//            Assertions.assertEquals(verificationCode, saved);
//        }
//        System.out.println(map);
        // scenario 1: Verification code send fail because user doesn't have an account in CMS
        String email = "mock123@uottawa.ca";
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isEmpty());
        Map<String, Object> resultMap = accountService.sendVerificationCode(email);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 2: Verification code send fail because email has not been authorized by admin yet
        email = "edwinaphillip@uottawa.ca";
        optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        Assertions.assertEquals(AccountStatus.unauthorized, optionalAccount.get().getAccountStatus());
        resultMap = accountService.sendVerificationCode(email);
        Assertions.assertFalse((Boolean) resultMap.get("success"));

        // scenario 3: Verification code send success
        email = "niladevine@uottawa.ca";
        optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        Account account = optionalAccount.get();
        Assertions.assertNotEquals(AccountStatus.unauthorized, account.getAccountStatus());
        resultMap = accountService.sendVerificationCode(email);
        Assertions.assertTrue((Boolean) resultMap.get("success"));
        account.setVerificationCode("NONE");
        accountRepository.save(account);
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
