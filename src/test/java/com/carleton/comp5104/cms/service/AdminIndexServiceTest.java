package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.AdminTodoList;
import com.carleton.comp5104.cms.enums.AdminTodoLevel;
import com.carleton.comp5104.cms.repository.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Before;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminIndexServiceTest {

    @Autowired
    AdminIndexService adminIndexService;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ClazzRepository clazzRepository;
    @Autowired
    ClassroomRepository classroomRepository;
    @Autowired
    AdminTodoListRepository adminTodoListRepository;


    private int accountNum;
    private int courseNum;
    private int clazzNum;
    private int classroomNum;
    private int adminAccountId = 1000000;

    @BeforeEach
    public void initialize() {
        accountNum = accountRepository.findAll().size();
        courseNum = courseRepository.findAll().size();
        clazzNum = clazzRepository.findAll().size();
        classroomNum = classroomRepository.findAll().size();
    }

    @Test
    void testGetAccountTableSize() {
        Integer accountTableSize = adminIndexService.getAccountTableSize();
        assertEquals(accountNum, accountTableSize);
    }

    @Test
    void testGetCourseTableSize() {
        Integer courseTableSize = adminIndexService.getCourseTableSize();
        assertEquals(courseNum, courseTableSize);
    }

    @Test
    void testGetClazzTableSize() {
        Integer clazzTableSize = adminIndexService.getClazzTableSize();
        assertEquals(clazzNum, clazzTableSize);
    }

    @Test
    void testGetClazzRoomTableSize() {
        Integer clazzRoomTableSize = adminIndexService.getClazzRoomTableSize();
        assertEquals(classroomNum, clazzRoomTableSize);
    }

    @Test
    void testAddAdminToDoList() {
        adminTodoListRepository.deleteAll();
        AdminTodoList adminTodoList = new AdminTodoList();
        adminTodoList.setAdminId(adminAccountId);
        adminTodoList.setNotes("This is a teat To Do");
        adminTodoList.setStatus(false);
        adminTodoList.setLevel(AdminTodoLevel.Normal);
        adminTodoList.setStartTime(new Date());
        adminTodoList.setEndTime(new Date());
        adminIndexService.addAdminToDoList(adminTodoList);
        Optional<AdminTodoList> byId = adminTodoListRepository.findById(adminAccountId);
        byId.ifPresent(todoList -> Assert.assertSame(adminTodoList, todoList));
    }

    @Test
    void testGetAdminTodoList() {
        AdminTodoList adminTodoList = addANewToDoFirst();
        List<AdminTodoList> adminTodos = adminIndexService.getAdminTodoList(adminAccountId);
        assertEquals(adminTodoList.getId(), adminTodos.get(0).getId());
    }

    @Test
    void testGetTodoListById() {
        AdminTodoList adminTodoList = addANewToDoFirst();
        AdminTodoList todoListById = adminIndexService.getTodoListById(adminTodoList.getId());
        assertEquals(adminTodoList, todoListById);
    }

    @Test
    void testChangeToDoStatus() {
        AdminTodoList adminTodoList = addANewToDoFirst();
        System.out.println(adminTodoList);
        Integer status = adminIndexService.changeToDoStatus(adminTodoList.getId());
        assertEquals(0, status);
        AdminTodoList todoListById = adminIndexService.getTodoListById(adminTodoList.getId());
        assertTrue(todoListById.isStatus());
    }

    @Test
    void testModifyAdminTodoList() {
        AdminTodoList adminTodoList = addANewToDoFirst();
        adminTodoList.setNotes("Modified");
        adminIndexService.modifyAdminTodoList(adminTodoList);
        adminIndexService.getTodoListById(adminTodoList.getId());
        assertEquals("Modified", adminTodoList.getNotes());

    }


    private AdminTodoList addANewToDoFirst() {
        adminTodoListRepository.deleteAll();
        AdminTodoList adminTodoList = new AdminTodoList();
        adminTodoList.setAdminId(adminAccountId);
        adminTodoList.setNotes("This is a teat To Do");
        adminTodoList.setStatus(false);
        adminTodoList.setLevel(AdminTodoLevel.Normal);
        adminTodoList.setStartTime(new Date());
        adminTodoList.setEndTime(new Date());
        adminIndexService.addAdminToDoList(adminTodoList);
        return adminTodoList;
    }


}