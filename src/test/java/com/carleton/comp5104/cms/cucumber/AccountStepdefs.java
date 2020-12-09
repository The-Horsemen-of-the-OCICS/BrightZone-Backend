package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.controller.account.AccountController;
import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.repository.RequestRepository;
import com.carleton.comp5104.cms.service.AccountService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class AccountStepdefs {

    private int userId = 0;
    private String password = "";
    private Map<String, Object> resultMap = new HashMap<>();
    private String requestType = "";
    private String requestMessage = "";
    private String email = "";
    private String verificationCode = "";
    private String oldVerificationCode = "";
    private Account account;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccountController accountController;

    @Autowired
    private RequestRepository requestRepository;

    private MockHttpServletRequest mockHttpServletRequest;

    @Given("User input userId {int} and password {string} in the login form")
    public void user_input_user_id_and_password_in_the_login_form(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Given("User does not have an account in Course Management System")
    public void user_does_not_have_an_account_in_course_management_system() {
        if (this.userId != 0) {
            Assert.assertFalse(accountRepository.existsById(this.userId));
        } else {
            Assert.assertFalse(accountRepository.existsAccountByEmail(this.email));
        }
    }

    @Given("Password {string} is not correct")
    public void password_is_not_correct(String password) {
        Assert.assertNotEquals(account.getPassword(), password);
    }

    @Given("Password {string} is correct")
    public void password_is_correct(String password) {
        Assert.assertEquals(account.getPassword(), password);
    }


    @When("User hit login button")
    public void user_hit_login_button() {
        this.resultMap = accountService.login(Integer.toString(userId), password);
    }

    @Then("Login fail")
    public void login_fail() {
        Assert.assertFalse((boolean) this.resultMap.get("success"));
    }

    @Then("Login success")
    public void login_success() {
        Assert.assertTrue((boolean) this.resultMap.get("success"));
        // reset account's information
        accountRepository.save(account);
    }

    @Given("User hit logout button")
    public void user_hit_logout_button() {
        HttpSession session = mockHttpServletRequest.getSession(false);
        if (session != null) {
            System.out.println(session.getAttribute("userId"));
        }
        this.resultMap = accountController.logout(mockHttpServletRequest);
        System.out.println(this.resultMap);
    }

    @When("User did not login to Course Management System before")
    public void user_did_not_login_to_course_management_system_before() {
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    @Then("Logout fail")
    public void logout_fail() {
        Assert.assertFalse((Boolean) this.resultMap.get("success"));
    }

    @Given("User login to Course Management System before")
    public void user_login_to_course_management_system_before() {
        mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "1000000");
        mockHttpServletRequest.setSession(session);
    }

    @Then("Logout success")
    public void logout_success() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
    }

    @Given("User input userId {int} in the register form")
    public void user_input_user_id_in_the_register_form(Integer userId) {
        this.userId = userId;
    }

    @When("User hit register button")
    public void user_hit_register_button() {
        this.resultMap = accountService.registerAccount(Integer.toString(this.userId));
    }

    @Given("User is not a member in this university")
    public void user_is_not_a_member_in_this_university() {
        Assert.assertFalse(personRepository.existsById(this.userId));
    }

    @Given("User has an account but not authorized yet")
    public void user_has_an_account_but_not_authorized_yet() {
        Optional<Account> optionalAccount;
        if (this.userId != 0) {
            optionalAccount = accountRepository.findById(this.userId);
        } else {
            optionalAccount = accountRepository.findByEmail(this.email);
        }
        Assert.assertTrue(optionalAccount.isPresent());
        account = optionalAccount.get();
        AccountStatus accountStatus = optionalAccount.get().getAccountStatus();
        Assert.assertEquals(AccountStatus.unauthorized, accountStatus);
    }

    @Then("Register fail")
    public void register_fail() {
        Assert.assertFalse((boolean) this.resultMap.get("success"));
    }

    @Given("User has an account and has already been authorized")
    public void user_has_an_account_and_has_already_been_authorized() {
        Optional<Account> optionalAccount;
        if (this.userId != 0) {
            optionalAccount = accountRepository.findById(this.userId);
        } else {
            optionalAccount = accountRepository.findByEmail(this.email);
        }
        Assert.assertTrue(optionalAccount.isPresent());
        account = optionalAccount.get();
        AccountStatus accountStatus = optionalAccount.get().getAccountStatus();
        Assert.assertNotEquals(AccountStatus.unauthorized, accountStatus);
    }

    @Given("User is a member in this university")
    public void user_is_a_member_in_this_university() {
        Assert.assertTrue(personRepository.existsById(this.userId));
    }

    @Then("Register success")
    public void register_success() {
        Assert.assertTrue((boolean) this.resultMap.get("success"));
        // delete newly added accounts
        accountRepository.deleteById(this.userId);
    }

    @Given("User with userId {int} choose request type {string}")
    public void user_with_user_id_choose_request_type(Integer userId, String requestType) {
        this.userId = userId;
        this.requestType = requestType;
    }

    @Given("Input request message {string} in the message box")
    public void input_request_message_in_the_message_box(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    @When("User hit send message button")
    public void user_hit_send_message_button() {
        this.resultMap = accountService.createRequest(this.userId, this.requestMessage, this.requestType);
    }

    @Then("Request message will be sent to admin")
    public void request_message_will_be_sent_to_admin() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
        // delete newly added requests
        Request request = (Request) this.resultMap.get("request");
        requestRepository.deleteById(request.getRequestId());
    }

    @Given("User input email {string} verification code {string} and new password {string} in password recovery form")
    public void user_input_email_verification_code_and_new_password_in_password_recovery_form(String email, String verificationCode, String newPassword) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.password = newPassword;
    }

    @When("User hit recovery button")
    public void user_hit_recovery_button() {
        this.resultMap = accountService.passwordRecovery(this.email, this.verificationCode, this.password);
    }

    @Then("Password recover fail")
    public void password_recover_fail() {
        Assert.assertFalse((Boolean) this.resultMap.get("success"));
    }

    @Given("User has an account but email has not been authorized yet")
    public void user_has_an_account_but_email_has_not_been_authorized_yet() {
        Optional<Account> optionalAccount = accountRepository.findByEmail(this.email);
        Assert.assertTrue(optionalAccount.isPresent());
        Account account = optionalAccount.get();
        Assert.assertEquals(AccountStatus.unauthorized, account.getAccountStatus());
    }

    @Given("User has an account and email has been authorized")
    public void user_has_an_account_and_email_has_been_authorized() {
        Optional<Account> optionalAccount = accountRepository.findByEmail(this.email);
        Assert.assertTrue(optionalAccount.isPresent());
        account = optionalAccount.get();
        Assert.assertNotEquals(AccountStatus.unauthorized, account.getAccountStatus());

        // Precondition: Set verification code to 234567
        oldVerificationCode = account.getVerificationCode();
        account.setVerificationCode("234567");
        accountRepository.save(account);
    }

    @Then("Password recover success")
    public void password_recover_success() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
        // reset account's information
        account.setVerificationCode(oldVerificationCode);
        accountRepository.save(account);
    }

    @Given("User input email {string} in verification code sending form")
    public void user_input_email_in_verification_code_sending_form(String email) {
        this.email = email;
    }

    @When("User hit send verification code button")
    public void user_hit_send_verification_code_button() {
        this.resultMap = accountService.sendVerificationCode(this.email);
    }

    @Then("Verification code send fail")
    public void verification_code_send_fail() {
        Assert.assertFalse((Boolean) this.resultMap.get("success"));
    }

    @Then("Verification code send success")
    public void verification_code_send_success() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
        // reset account's information
        accountRepository.save(account);
    }

    @Given("User with userId {int} input new email {string} in email update form")
    public void user_with_user_id_input_new_email_in_email_update_form(int userId, String newEmail) {
        this.userId = userId;
        Optional<Account> optionalAccount = accountRepository.findById(this.userId);
        Assert.assertTrue(optionalAccount.isPresent());
        account = optionalAccount.get();
        this.email = newEmail;
    }

    @Given("New email has already been occupied by other user")
    public void new_email_has_already_been_occupied_by_other_user() {
        List<Account> accounts = accountRepository.findAllByEmailAndUserIdNot(this.email, this.userId);
        Assertions.assertTrue(accounts.size() > 0);
    }

    @When("User hit update email button")
    public void user_hit_update_email_button() {
        this.resultMap = accountService.updateEmail(this.userId, this.email);
    }

    @Then("Update fail")
    public void update_fail() {
        Assert.assertFalse((Boolean) this.resultMap.get("success"));
    }

    @Given("New email has not been occupied by other user")
    public void new_email_has_not_been_occupied_by_other_user() {
        List<Account> accounts = accountRepository.findAllByEmailAndUserIdNot(this.email, this.userId);
        Assertions.assertFalse(accounts.size() > 0);
    }

    @Then("Email update success")
    public void email_update_success() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
        // reset account's information
        accountRepository.save(this.account);
    }

    @Given("User with userId {int} input new password {string} in password update form")
    public void user_with_user_id_input_new_password_in_password_update_form(int userId, String newPassword) {
        this.userId = userId;
        Optional<Account> optionalAccount = accountRepository.findById(this.userId);
        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Can't find account with userId " + this.userId + " in database.");
        }
        account = optionalAccount.get();
        this.password = newPassword;
    }

    @Then("User hit update password button")
    public void user_hit_update_password_button() {
        this.resultMap = accountService.updatePassword(this.userId, this.password);
    }

    @Then("Password update success")
    public void password_update_success() {
        Assert.assertTrue((Boolean) this.resultMap.get("success"));
        // reset account's information
        accountRepository.save(account);
    }

}