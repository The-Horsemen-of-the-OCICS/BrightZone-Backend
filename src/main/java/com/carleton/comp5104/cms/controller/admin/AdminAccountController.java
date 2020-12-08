package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Faculty;
import com.carleton.comp5104.cms.service.AdminAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    @Autowired
    private AdminAccountService adminAccountService;

    @GetMapping("/getAll/{pageNum}/{pageSize}")
    public Page<Account> getAllAccount(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return adminAccountService.getAllAccount(pageNum, pageSize);
    }

    @GetMapping("/getAllByType/{type}/{pageNum}/{pageSize}")
    public Page<Account> getAllAccountByType(@PathVariable("type") String type,
                                             @PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize) {
        return adminAccountService.getAllAccountByType(type, pageNum, pageSize);
    }

    @GetMapping("/getAllByName/{name}/{pageNum}/{pageSize}")
    public Page<Account> getAllAccountByName(@PathVariable("name") String name,
                                             @PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize) {
        return adminAccountService.getAllAccountByName(name, pageNum, pageSize);
    }

    @GetMapping("/getAllByTypeAndName/{type}/{name}/{pageNum}/{pageSize}")
    public Page<Account> getAllAccountByTypeAndName(@PathVariable("type") String type,
                                                    @PathVariable("name") String name,
                                                    @PathVariable("pageNum") Integer pageNum,
                                                    @PathVariable("pageSize") Integer pageSize) {
        return adminAccountService.getAllAccountByTypeAndName(type, name, pageNum, pageSize);
    }

    @GetMapping("/getAllFaculties")
    public List<Faculty> getAllFaculties() {
        return adminAccountService.getAllFaculties();
    }

    @GetMapping("/getAccount/{id}")
    public Account getAccountById(@PathVariable("id") Integer id) {
        return adminAccountService.getAccountById(id);
    }

    @PostMapping("/addNewAccount")
    public String addNewAccount(@RequestBody Account newAccount) {
        int status = adminAccountService.addNewAccount(newAccount);
        if (status == 1) {
            return "success";
        } else {
            return "error";
        }
    }

    @DeleteMapping("/deleteAccount/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {
        int status = adminAccountService.deleteAccountById(id);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PutMapping("/updateAccount")
    public String updateAccount(@RequestBody Account updateAccount) {
        int status = adminAccountService.updateAccount(updateAccount);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @GetMapping("/check/email/{Email}")
    public String newAccountEmailValidCheck(@PathVariable("Email") String newEmailAddress) {
        return adminAccountService.newAccountEmailValidCheck(newEmailAddress);
    }

}
