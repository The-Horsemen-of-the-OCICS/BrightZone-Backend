package com.carleton.comp5104.cms.controller.account;

import com.carleton.comp5104.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/account/register")
    public Map<String, Object> register(@RequestParam("email") String email) {
        Map<String, Object> result = accountService.registerAccount(email);
        return result;
    }
}
