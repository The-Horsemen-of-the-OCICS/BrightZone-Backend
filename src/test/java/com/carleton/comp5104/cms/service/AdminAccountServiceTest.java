package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.GenderType;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.FacultyRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import org.junit.Assert;
import com.carleton.comp5104.cms.entity.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminAccountServiceTest {

    @Autowired
    private AdminAccountService adminAccountService;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PersonRepository personRepository;

    private int facultyNum = 0;

    @BeforeEach
    void setUp() {
        facultyNum = facultyRepository.findAll().size();
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


    @Test
    void testGetAllAccount() {
        Page<Account> accounts = adminAccountService.getAllAccount(0, 10);
        assertEquals(10, accounts.getSize());
    }

    @Test
    void testGetAllAccountByType() {
        String accountType = "professor";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByType(accountType, pageNum, pageSize);
        assertEquals(10, accounts.getSize());
        assertEquals(AccountType.professor, accounts.getContent().get(0).getType());
    }

    @Test
    void testGetAllAccountByName() {
        String name = "Nila Devine";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByName(name, pageNum, pageSize);
        assertEquals(name, accounts.getContent().get(0).getName());
    }

    @Test
    void testGetAllAccountByTypeAndName() {
        String accountType = "administrator";
        String name = "Nila Devine";
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page<Account> accounts = adminAccountService.getAllAccountByTypeAndName(accountType, name, pageNum, pageSize);
        assertEquals(name, accounts.getContent().get(0).getName());
        assertEquals(AccountType.administrator, accounts.getContent().get(0).getType());
    }

    @Test
    void testGetAllFaculties() {
        int size = adminAccountService.getAllFaculties().size();
        assertEquals(facultyNum, size);
    }

    @Test
    void testGetAccountById() {
        String name = "Nila Devine";
        int accountId = 1000000;
        Account accountById = adminAccountService.getAccountById(accountId);
        assertEquals(name, accountById.getName());
    }

    @Test
    void testAdd_Update_DeleteAccount() {
        // test add a new account to table.
        Person person = new Person();
        person.setPersonId(300166166);
        person.setName("Tom hanks");
        person.setType(AccountType.student);
        person.setFacultyId(1);
        person.setProgram("Kinesiology");
        person.setGender(GenderType.male);
        personRepository.save(person);
        Account account = new Account();
        account.setUserId(300166166);
        account.setName("Tom hanks");
        account.setType(AccountType.student);
        account.setAccountStatus(AccountStatus.current);
        account.setFacultyId(1);
        account.setPassword("TomHanks");
        account.setEmail("tomhanks@uottawa.ca");
        account.setProgram("Kinesiology");
        account.setLastLogin(new Timestamp(System.currentTimeMillis()));
        account.setVerificationCode("");
        Integer status = adminAccountService.addNewAccount(account);
        assertEquals(0, status);
        Account accountById = adminAccountService.getAccountById(account.getUserId());
        Assert.assertSame(account.getUserId(), accountById.getUserId());

        //test update the new added account.
        // 1.change the new and status.
        // 2. change the state back to current to test the email system.
        accountById = adminAccountService.getAccountById(account.getUserId());
        String newName = "Jack Ryan";
        accountById.setName(newName);
        accountById.setAccountStatus(AccountStatus.unauthorized);
        status = adminAccountService.updateAccount(accountById);
        assertEquals(0, status);
        Account accountByIdNameUpdated = adminAccountService.getAccountById(accountById.getUserId());
        assertEquals(newName, accountByIdNameUpdated.getName());
        accountByIdNameUpdated.setAccountStatus(AccountStatus.current);
        status = adminAccountService.updateAccount(accountByIdNameUpdated);
        assertEquals(0, status);
        Account accountByIdStatusUpdated = adminAccountService.getAccountById(accountById.getUserId());
        assertEquals(AccountStatus.current, accountByIdStatusUpdated.getAccountStatus());


        //test delete the new added account
        accountById = adminAccountService.getAccountById(account.getUserId());
        status = adminAccountService.deleteAccountById(accountById.getUserId());
        assertEquals(0, status);
        Account accountByIdEqualsNone = adminAccountService.getAccountById(accountById.getUserId());
        Assert.assertNull(accountByIdEqualsNone);
        personRepository.deleteById(person.getPersonId());
    }

    @Test
    void newAccountEmailValidCheck() {
        String newEmailAddress = "niladevine@uottawa.ca";
        String s = adminAccountService.newAccountEmailValidCheck(newEmailAddress);
        assertEquals("Repeat", s);
        newEmailAddress = "niladevine123@uottawa.ca";
        s = adminAccountService.newAccountEmailValidCheck(newEmailAddress);
        assertEquals("Valid", s);
    }

}
