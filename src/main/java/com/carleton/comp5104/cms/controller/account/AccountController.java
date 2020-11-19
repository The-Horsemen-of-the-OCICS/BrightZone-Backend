package com.carleton.comp5104.cms.controller.account;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/api/account/register")
    public Map<String, Object> register(@RequestParam("email") String email) {
        Map<String, Object> map = accountService.registerAccount(email);
        return map;
    }

    @PostMapping("/api/account/login")
    public Map<String, Object> login(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     HttpSession session) {
        email = email.trim();
        password = password.trim();
        Map<String, Object> map = accountService.login(email, password);
        Boolean success = (Boolean) map.get("success");
        if (success) {
            Account account = (Account) map.get("account");
            session.setAttribute("account", account);
        }
        return map;
    }
}
