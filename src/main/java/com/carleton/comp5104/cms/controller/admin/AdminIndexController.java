package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.AdminTodoList;
import com.carleton.comp5104.cms.service.*;
import io.cucumber.gherkin.internal.com.eclipsesource.json.Json;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/index")
public class AdminIndexController {

    @Autowired
    private AdminIndexService adminIndexService;

    @GetMapping("/getAccountsNum")
    public Integer getAccountsNum() {
        return adminIndexService.getAccountTableSize();
    }

    @GetMapping("/getCoursesNum")
    public Integer getCoursesNum() {
        return adminIndexService.getCourseTableSize();
    }

    @GetMapping("/getClazzNum")
    public Integer getClazzNum() {
        return adminIndexService.getClazzTableSize();
    }

    @GetMapping("/getAdminToDoList/{adminId}")
    public List<AdminTodoList> getAdminToDoList(@PathVariable int adminId) {
        return adminIndexService.getAdminTodoList(adminId);
    }

    @PostMapping("/addAdminToDoList")
    public String addAdminToDoList(@RequestBody JSONObject addForm) {
        Integer status = adminIndexService.addAdminToDoList(addForm);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/modifyAdminToDoList")
    public String modifyAdminToDoList(@RequestBody JSONObject addForm) {
        Integer status = adminIndexService.modifyAdminTodoList(addForm);
        System.out.println(status);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @GetMapping("/changeAdminTodoListStatus/{todoListId}")
    public String changeAdminTodoListStatus(@PathVariable int todoListId) {
        Integer status = adminIndexService.changeToDoStatus(todoListId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }
}
