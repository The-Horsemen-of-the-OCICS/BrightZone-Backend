package com.carleton.comp5104.cms.controller.account;

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
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonService personService;

    @PostMapping("/api/account/register")
    public Map<String, Object> register(@RequestParam("email") String email) {
        Map<String, Object> map = accountService.registerAccount(email);
        return map;
    }

    @PostMapping("/api/account/login")
    public Map<String, Object> login(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> map = accountService.login(email, password);
        Boolean success = (Boolean) map.get("success");
        if (success) {
            Account account = (Account) map.get("account");
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("userId", account.getUserId());
            result.put("success", true);
            result.put("userId", personService.findById(account.getUserId()).getPersonId());
            result.put("accountType", account.getType());
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
    public Map<String, Object> createRequest(@RequestParam("requestMessage") String requestMessage,
                                             @RequestParam("requestType") String requestType,
                                             HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("errMsg", "Please login first");
            return map;
        }

        int accountId = account.getUserId();
        Map<String, Object> map = accountService.createRequest(accountId, requestMessage, requestType);
        return map;
    }

    @PostMapping("/api/account/passwordRecovery")
    public Map<String, Object> passwordRecovery(@RequestParam("email") String email,
                                                @RequestParam("verificationCode") String verificationCode,
                                                @RequestParam("newPassword") String newPassword) {
        email = email.trim();
        verificationCode = verificationCode.trim();
        newPassword = newPassword.trim();
        return accountService.passwordRecovery(email, verificationCode, newPassword);
    }

    @PostMapping("/api/account/sendVerificationCode")
    public Map<String, Object> sendVerificationCode(@RequestParam("email") String email) {
        return accountService.sendVerificationCode(email);
    }
}
