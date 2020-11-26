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
        //TODO deletePermissionCheck
        //权限认证
        int status = adminAccountService.deleteAccountById(id);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
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
