package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.pojo.Account;
import com.carleton.comp5104.cms.repository.AccountManagerRepository;
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
    private AccountManagerRepository accountManagerRepository;

    @ResponseBody
    @GetMapping("/findAll/{pageNum}/{pageSize}")
    public Page<Account> findAll(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountManagerRepository.findAll(pageable);
    }

    @ResponseBody
    @GetMapping("/findAccount/{id}")
    public Account findById(@PathVariable("id") Integer id) {
        Optional<Account> byId = accountManagerRepository.findById(id);
        return byId.orElse(null);
    }

    @ResponseBody
    @GetMapping("/addNewAccount")
    public String addNewAccount(@RequestBody Account newAccount) {
        Integer newAccountUserId = newAccount.getUserId();
        Optional<Account> byId = accountManagerRepository.findById(newAccountUserId);
        if (byId.isEmpty()) {
            Account account = accountManagerRepository.save(newAccount);
            return "success";
        } else {
            return "error";
        }
    }

    @ResponseBody
    @GetMapping("/deleteAccountById/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {

        //权限认证

        accountManagerRepository.deleteById(id);
        return "OK";
    }

    @ResponseBody
    @GetMapping("/updateAccount")
    public String updateAccount(@RequestBody Account updateAccount) {

        //权限认证

        accountManagerRepository.save(updateAccount);
        return "success";
    }


}
