package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.Clazz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminClazzServiceTest {

    @Autowired
    private AdminClazzService adminClazzService;

    @Test
    void getClassByCourseId() {
        //3797 has class.
        ArrayList<HashMap<String, String>> classByCourseId1 = adminClazzService.getClassByCourseId(3797);
        assertEquals(1, classByCourseId1.size());
        //6167 do not have class.
        ArrayList<HashMap<String, String>> classByCourseId2 = adminClazzService.getClassByCourseId(6167);
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
        HashMap<String, String> classroomScheduleInfo = new HashMap<>();
        classroomScheduleInfo.put("weekDay", "Tues");
        classroomScheduleInfo.put("startTime", "10:00:00");
        classroomScheduleInfo.put("endTime", "12:00:00");
        classroomScheduleInfo.put("roomCapacityAsked", "100");
        ArrayList<Classroom> classroomAvailable = null;
        try {
            classroomAvailable = adminClazzService.classroomSchedule(classroomScheduleInfo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(100, classroomAvailable.get(0).getRoomCapacity());
    }

    @Test
    void addNewClass() {
        HashMap<String, String> addNewClassInfo = new HashMap<>();
        ArrayList<HashMap<String, String>> classByCourseId = adminClazzService.getClassByCourseId(6132);
        addNewClassInfo.put("courseId", "6132");
        addNewClassInfo.put("classDesc", "a new class added to course 6132");
        addNewClassInfo.put("classStatus", "open");
        int newSectionNum = classByCourseId.size();
        addNewClassInfo.put("section", String.valueOf(newSectionNum));
        addNewClassInfo.put("enrolled", "0");
        addNewClassInfo.put("enrollCapacity", "100");
        addNewClassInfo.put("professorId", "2000006");
        addNewClassInfo.put("roomId", "23");
        addNewClassInfo.put("enrollDeadline", "2021-2-5 20:30:30");
        addNewClassInfo.put("dropNoPenaltyDeadline", "2021-3-5 20:30:30");
        addNewClassInfo.put("dropNoFailDeadline", "2021-4-5 20:30:30");
        addNewClassInfo.put("weekDay", "Tues");
        addNewClassInfo.put("startTime", "10:00:00");
        addNewClassInfo.put("endTime", "12:00:00");
        Integer status = adminClazzService.addNewClass(addNewClassInfo);
        assertEquals(0, status);
        classByCourseId = adminClazzService.getClassByCourseId(6132);
        assertEquals(newSectionNum, Integer.parseInt(classByCourseId.get(classByCourseId.size() - 1).get("section")));
    }
}