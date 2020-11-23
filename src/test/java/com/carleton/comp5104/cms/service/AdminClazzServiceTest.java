package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Clazz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminClazzServiceTest {

    @Autowired
    private AdminClazzService adminClazzService;

    @Test
    void getClassByCourseId() {
        //3797 has class.
        ArrayList<Clazz> classByCourseId1 = adminClazzService.getClassByCourseId(3797);
        assertEquals(1, classByCourseId1.size());
        //6167 do not have class.
        ArrayList<Clazz> classByCourseId2 = adminClazzService.getClassByCourseId(6167);
        assertEquals(0, classByCourseId2.size());
    }

    @Test
    void getProfessorById() {
        Account professorById = adminClazzService.getProfessorById(2000006);
        assertEquals("Floyd Heasley", professorById.getName());
    }

    @Test
    void getProfessorByEmail() {
        Account professorById = adminClazzService.getProfessorByEmail("floydheasley@uottawa.ca");
        assertEquals("Floyd Heasley", professorById.getName());
    }

    @Test
    void classroomSchedule() {

    }

    @Test
    void addNewClass() {
    }
}