package com.carleton.comp5104.cms.controller.account;

import com.carleton.comp5104.cms.controller.BaseController;
import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.service.AccountService;
import com.carleton.comp5104.cms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonService personService;

    @PostMapping("/api/account/register")
    public Map<String, Object> register(@RequestParam("emailOrUserId") String emailOrUserId) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.registerAccount(emailOrUserId);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @PostMapping("/api/account/login")
    public Map<String, Object> login(@RequestParam("emailOrUserId") String emailOrUserId,
                                     @RequestParam("password") String password) {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> map = accountService.login(emailOrUserId, password);
        Boolean success = (Boolean) map.get("success");
        if (success) {
            Account account = (Account) map.get("account");
            setUserId(account);
            result.put("success", true);
            result.put("userId", personService.findById(account.getUserId()).getPersonId());
            result.put("name", account.getName());
            result.put("accountType", account.getType());
            result.put("email", account.getEmail());
            result.put("lastLogin", account.getLastLogin());
        } else {
            result.put("success", false);
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @GetMapping("/api/account/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();

        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("userId")!=null) {
            request.getSession().invalidate();
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("errMsg", "Please login first!");
        }
        return result;
    }

    @PostMapping("/api/account/createRequest")
    public Map<String, Object> createRequest(@RequestParam("message") String requestMessage,
                                             @RequestParam("type") String requestType) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.createRequest(getUserId(), requestMessage, requestType);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @PostMapping("/api/account/passwordRecovery")
    public Map<String, Object> passwordRecovery(@RequestParam("email") String email,
                                                @RequestParam("verificationCode") String verificationCode,
                                                @RequestParam("newPassword") String newPassword) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.passwordRecovery(email, verificationCode, newPassword);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @PostMapping("/api/account/sendVerificationCode")
    public Map<String, Object> sendVerificationCode(@RequestParam("email") String email) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.sendVerificationCode(email);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @PostMapping("/api/account/updateEmail")
    public Map<String, Object> updateEmail(@RequestParam("email") String email) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.updateEmail(getUserId(), email);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }

    @PostMapping("/api/account/updatePassword")
    public Map<String, Object> updatePassword(@RequestParam("password") String password) {
        HashMap<String, Object> result = new HashMap<>();

        Map<String, Object> map = accountService.updatePassword(getUserId(), password);
        boolean success = (boolean) map.get("success");
        result.put("success", success);
        if (!success) {
            result.put("errMsg", map.get("errMsg"));
        }
        return result;
    }
}
