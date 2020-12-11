package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.AdminTodoList;
import com.carleton.comp5104.cms.enums.AdminTodoLevel;
import com.carleton.comp5104.cms.service.AdminAccountService;
import com.carleton.comp5104.cms.service.AdminIndexService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class AdminIndextepdefs {

    @Autowired
    private AdminIndexService adminIndexService;

    @Autowired
    private AdminAccountService adminAccountService;

    private AdminTodoList adminTodoList;

    private Account theNewAdmin;

    @Given("the admin press {string} button to write a new todo")
    public void the_admin_press_button_to_write_a_new_todo(String code) {
        System.out.println("admin choose the " + code + " a new todo");
        adminTodoList = new AdminTodoList();
        theNewAdmin = adminAccountService.getAccountById(300162168);
        adminTodoList.setAdminId(theNewAdmin.getUserId());
    }

    @Given("the admin press {string} button to edit the number {int} todo")
    public void the_admin_press_button_to_edit_the_number_todo(String code, Integer todoNum) {
        System.out.println("admin choose the " + code + " the No." + todoNum + " todo");
        List<AdminTodoList> adminTodos = adminIndexService.getAdminTodoList(300162168);
        adminTodoList = adminTodos.get(todoNum);
        theNewAdmin = adminAccountService.getAccountById(300162168);
    }

    @When("the admin input the new todo {string}")
    public void the_admin_input_the_new_todo(String notes) {
        adminTodoList.setNotes(notes);
    }

    @When("the admin choose the priority {string} of this new Todo")
    public void the_admin_choose_the_priority_of_this_new_todo(String level) {
        adminTodoList.setLevel(AdminTodoLevel.valueOf(level));
    }

    @When("the admin choose the start time {string} of the new Todo")
    public void the_admin_choose_the_start_time_of_the_new_todo(String startTime) {
        adminTodoList.setStartTime(formatString2Time(startTime));
    }

    @When("the admin choose the end time {string} of the new Todo")
    public void the_admin_choose_the_end_time_of_the_new_todo(String endTime) {
        adminTodoList.setEndTime(formatString2Time(endTime));
    }

    @Then("the admin press {string} button to save the todo")
    public void the_admin_press_button_to_save_the_todo(String string) {
        System.out.println(adminTodoList.toString());
        int status = adminIndexService.addAdminToDoList(adminTodoList);
        Assert.assertEquals(0, status);
    }

    @Then("the system show the new added todo")
    public void the_system_show_the_new_added_todo() {
        List<AdminTodoList> adminTodoList = adminIndexService.getAdminTodoList(theNewAdmin.getUserId());
        Assert.assertTrue(adminTodoList.size() > 0);
        System.out.println(Arrays.toString(adminTodoList.toArray()));

        adminAccountService.deleteAccountById(theNewAdmin.getUserId());
    }

    @Then("the admin press {string} button to save the edit")
    public void the_admin_press_button_to_save_the_edit(String string) {
        int status = adminIndexService.modifyAdminTodoList(adminTodoList);
        Assert.assertEquals(0, status);
    }

    @Then("the system show the new edit todo")
    public void the_system_show_the_new_edit_todo() {
        List<AdminTodoList> adminTodoList = adminIndexService.getAdminTodoList(theNewAdmin.getUserId());
        Assert.assertTrue(adminTodoList.size() > 0);
        System.out.println(Arrays.toString(adminTodoList.toArray()));

        adminAccountService.deleteAccountById(theNewAdmin.getUserId());
    }


    @Given("the todo items has been add to table")
    public void the_todo_items_has_been_add_to_table() {
        adminTodoList = new AdminTodoList();
        theNewAdmin = adminAccountService.getAccountById(300162168);
        adminTodoList.setNotes("a test admin todo");
        adminTodoList.setAdminId(theNewAdmin.getUserId());
        adminTodoList.setLevel(AdminTodoLevel.Prior);
        adminTodoList.setStartTime(formatString2Time("2021-2-5 20:30:30"));
        adminTodoList.setEndTime(formatString2Time("2021-3-5 20:30:30"));
        int integer = adminIndexService.addAdminToDoList(adminTodoList);
        Assert.assertEquals(0, integer);
    }

    @When("the admin press the checkbox at the todo item {int}")
    public void the_admin_press_the_checkbox_at_the_todo_item(Integer int1) {
        System.out.println("admin finish a todo item");
    }

    @Then("the system change the state of the todo item")
    public void the_system_change_the_state_of_the_todo_item() {
        int integer = adminIndexService.changeToDoStatus(adminTodoList.getId());
        Assert.assertEquals(0, integer);
    }

    @Then("the system show the todo has been finished")
    public void the_system_show_the_todo_has_been_finished() {

        List<AdminTodoList> adminTodoList = adminIndexService.getAdminTodoList(theNewAdmin.getUserId());
        Assert.assertEquals(0, adminTodoList.size());

        adminAccountService.deleteAccountById(theNewAdmin.getUserId());
    }

    private Time formatString2Time(String inputTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Time inputTimeFormatted = null;
        try {
            inputTimeFormatted = new Time(df.parse(inputTime).getTime());
            System.out.println(inputTimeFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputTimeFormatted;
    }

}
