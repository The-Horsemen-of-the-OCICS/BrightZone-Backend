package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Faculty;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.GenderType;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.service.AdminAccountService;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminAccountStepDef {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AdminAccountService adminAccountService;

    private Account testAccount;

    private Page<Account> allAccountByName;

    private int accountId = 300162168;

    private String operator;

    private int state;

    @Given("the new test user was added to account table")
    public void theNewTestUserWasAddedToAccountTable() {
        // test add a new student account to table.
        Person person = ANewPerson("student");
        personRepository.save(person);
        Account account = ANewAccount("student");
        Integer status = adminAccountService.addNewAccount(account);
        assertEquals(0, status);
        Assert.assertNotNull(adminAccountService.getAccountById(accountId));
    }

    @Given("the new professor test user was added to account table")
    public void the_new_professor_test_user_was_added_to_account_table() {
        // test add a new student account to table.
        Person person = ANewPerson("professor");
        personRepository.save(person);
        Account account = ANewAccount("professor");
        Integer status = adminAccountService.addNewAccount(account);
        assertEquals(0, status);
        Assert.assertNotNull(adminAccountService.getAccountById(accountId));
    }

    @When("the admin search the account with the {string}")
    public void theAdminSearchTheAccountWithThe(String userName) {
        allAccountByName = adminAccountService.getAllAccountByName(userName, 0, 10);
    }

    @And("the system output no such user")
    public void theSystemOutputNoSuchUser() {
        if (allAccountByName.getContent().size() == 0) {
            System.out.println("No such user");
        }
    }

    @And("the system output the userInfo")
    public void theSystemOutputTheUserInfo() {
        testAccount = allAccountByName.getContent().get(0);
        System.out.println(testAccount.toString());
        System.out.println("find the user.");
    }

    @When("the admin press the {string} button")
    public void the_admin_press_the_button(String string) {
        System.out.println("Admin choose to " + string + " the user.");
        operator = string;
    }

    @Then("the system delete the account")
    public void theSystemDeleteTheAccount() {
        int status = adminAccountService.deleteAccountById(accountId);
        Assert.assertEquals(0, status);
        Account accountById = adminAccountService.getAccountById(accountId);
        Assert.assertNull(accountById);
        personRepository.deleteById(accountId);
    }

    @When("the admin set the account to {string}")
    public void the_admin_set_the_account_to(String accountType) {
        AccountType accountType1 = AccountType.valueOf(accountType);
        testAccount.setType(accountType1);
    }

    @When("the admin set the account to a new {string}")
    public void the_admin_set_the_account_to_a_new(String string) {
        testAccount.setName(string);
    }

    @When("the System check if the input edit is valid")
    public void the_system_check_if_the_input_edit_is_valid() {
        if (testAccount.getName().equals("")) {
            state = 1;
        } else if (testAccount.getName().trim().length() == 0) {
            state = 2;
        } else {
            state = 0;

        }
    }

    @When("the System output the {string} should not be {string}")
    public void the_system_output_the_should_not_be(String string, String notes) {
        if (notes.equals("none")) {
            Assert.assertEquals(1, state);
            System.out.println(string + " should not be none");
        } else if (notes.equals("all blanks")) {
            Assert.assertEquals(2, state);
            System.out.println(string + " should not be all blanks");
        } else {
            Assert.assertEquals(0, state);
            System.out.println("valid input");
        }
    }

    @When("the System output the {string} is valid")
    public void the_system_output_the_is_valid(String string) {
        Assert.assertEquals(0, state);
        System.out.println("valid :" + string);
    }

    @Then("the system update the account with the new info")
    public void the_system_update_the_account_with_the_new_info() {
        int status = adminAccountService.updateAccount(testAccount);
        Assert.assertEquals(0, status);
    }

    @Then("the system shows the updated account info")
    public void the_system_shows_the_updated_account_info() {
        Account accountById = adminAccountService.getAccountById(testAccount.getUserId());
        Assert.assertEquals(testAccount.getName(), accountById.getName());
        Assert.assertEquals(testAccount.getType(), accountById.getType());
        System.out.println(accountById.toString());
        int status = adminAccountService.deleteAccountById(accountId);
        Assert.assertEquals(0, status);
        accountById = adminAccountService.getAccountById(accountId);
        Assert.assertNull(accountById);
    }

    private Person ANewPerson(String accountType) {
        Person person = new Person();
        person.setPersonId(accountId);
        person.setName("Tom Hanks ");
        if (accountType.equals("student")) {
            person.setType(AccountType.student);
        } else if (accountType.equals("professor")) {
            person.setType(AccountType.professor);
        }
        person.setFacultyId(1);
        person.setProgram("Kinesiology");
        person.setGender(GenderType.male);
        return person;
    }

    private Account ANewAccount(String accountType) {
        Account account = new Account();
        account.setUserId(accountId);
        account.setName("Tom hanks");
        if (accountType.equals("student")) {
            account.setType(AccountType.student);
        } else if (accountType.equals("professor")) {
            account.setType(AccountType.professor);
        }
        account.setAccountStatus(AccountStatus.current);
        List<Faculty> allFaculties = adminAccountService.getAllFaculties();
        account.setFacultyId(allFaculties.get(0).getFacultyId());
        account.setPassword("TomHanks");
        String s = adminAccountService.newAccountEmailValidCheck("tomhanks@uottawa.ca");
        if (s.equals("Valid")) {
            account.setEmail("tomhanks@uottawa.ca");
        }
        account.setProgram("Kinesiology");
        account.setLastLogin(new Timestamp(System.currentTimeMillis()));
        account.setVerificationCode("");
        return account;
    }

}
