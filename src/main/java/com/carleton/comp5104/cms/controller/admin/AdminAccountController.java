package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.service.AdminAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    @Autowired
    private AdminAccountService adminAccountService;

    @GetMapping("/getAll/{pageNum}/{pageSize}")
    public Page<Account> getAllAccount(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return adminAccountService.getAllAccount(pageNum, pageSize);
    }

    @GetMapping("/getAccount/{id}")
    public Account getAccountById(@PathVariable("id") Integer id) {
        return adminAccountService.getAccountById(id);
    }

    @PostMapping("/addNewAccount")
    public String addNewAccount(@RequestBody Account newAccount) {
        return adminAccountService.addNewAccount(newAccount);
    }

    @DeleteMapping("/deleteAccount/{id}")
    public String deleteAccountById(@PathVariable("id") Integer id) {
        //TODO deletePermissionCheck
        //权限认证
        return adminAccountService.deleteAccountById(id);
    }

    @PutMapping("/updateAccount")
    public String updateAccount(@RequestBody Account updateAccount) {
        //TODO updatePermissionCheck
        //权限认证
        return adminAccountService.updateAccount(updateAccount);
    }

    //font-end check box data source
    @GetMapping("/addPage/getFaculty")
    public Map<Integer, String> getFaculties() {
        return adminAccountService.getFaculties();
    }

    //font-end check box data source
    @GetMapping("/addPage/getProgram")
    public Map<Integer, String> getPrograms() {
        return adminAccountService.getPrograms();
    }

    @GetMapping("/check/email/{Email}")
    public String newAccountEmailValidCheck(@PathVariable("Email") String newEmailAddress) {
        return adminAccountService.newAccountEmailValidCheck(newEmailAddress);
    }

}
