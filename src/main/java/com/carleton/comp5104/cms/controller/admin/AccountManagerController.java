package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/Accounts")
public class AccountManagerController {

    @Autowired
    private AccountRepository accountRepository;

    @ResponseBody
    @GetMapping("/findAll/{pageNum}/{pageSize}")
    public Page<Account> findAll(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAll(pageable);
    }

    @ResponseBody
    @GetMapping("/findAccount/{id}")
    public Account findById(@PathVariable("id") Integer id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElse(null);
    }

    @ResponseBody
    @GetMapping("/addNewAccount")
    public String addNewAccount(@RequestBody Account newAccount) {
        Integer newAccountUserId = newAccount.getUserId();
        Optional<Account> byId = accountRepository.findById(newAccountUserId);
        if (byId.isEmpty()) {
            Account account = accountRepository.save(newAccount);
            return "success";
        } else {
            return "error";
        }
    }

    @ResponseBody
    @GetMapping("/deleteAccountById/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {

        //权限认证

        accountRepository.deleteById(id);
        return "OK";
    }

    @ResponseBody
    @GetMapping("/updateAccount")
    public String updateAccount(@RequestBody Account updateAccount) {

        //权限认证

        accountRepository.save(updateAccount);
        return "success";
    }


}
