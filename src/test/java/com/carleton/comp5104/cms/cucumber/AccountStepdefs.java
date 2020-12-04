package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.service.AccountService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class AccountStepdefs {

    private String email = "";
    private String password = "";
    private Account account = new Account();
    private Map<String, Object> resultMap = new HashMap<>();

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Given("User input email {string} and password {string} in the login form")
    public void user_input_email_and_password_in_the_login_form(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Given("Email {string} or password {string} is empty")
    public void email_or_password_is_empty(String email, String password) {
        Assert.assertTrue(StringUtils.isEmpty(email) || StringUtils.isEmpty(password));
    }

    @Given("Email {string} is not in table Account")
    public void email_is_not_in_table_account(String email) {
        Assert.assertFalse(accountRepository.existsAccountByEmail(email));
    }

    @Given("Email {string} is in table Account")
    public void email_is_in_table_account(String email) {
        Assert.assertTrue(accountRepository.existsAccountByEmail(email));
    }

    @Given("Email {string} is not authorized yet")
    public void email_is_not_authorized_yet(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assert.assertTrue(optionalAccount.isPresent());
        this.account = optionalAccount.get();
        Assert.assertEquals(AccountStatus.unauthorized, this.account.getAccountStatus());
    }

    @Given("Email {string} is already authorized")
    public void email_is_already_authorized(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assert.assertTrue(optionalAccount.isPresent());
        this.account = optionalAccount.get();
        Assert.assertNotEquals(AccountStatus.unauthorized, this.account.getAccountStatus());
    }

    @Given("Password {string} is not correct")
    public void password_is_not_correct(String password) {
        Assert.assertNotEquals(this.account.getPassword(), password);
    }

    @Given("Password {string} is correct")
    public void password_is_correct(String password) {
        Assert.assertEquals(this.account.getPassword(), password);
    }


    @When("User hit login button")
    public void user_hit_login_button() {
        this.resultMap = accountService.login(email, password);
        System.out.println("result map: " + this.resultMap);
    }

    @Then("Login fail")
    public void login_fail() {
        Assert.assertFalse((boolean) this.resultMap.get("success"));
    }

    @Then("Login success")
    public void login_success() {
        Assert.assertTrue((boolean) this.resultMap.get("success"));
    }

}
