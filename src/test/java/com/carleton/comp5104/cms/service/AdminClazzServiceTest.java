package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.ClassStatus;
import com.carleton.comp5104.cms.enums.WeekDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminClazzServiceTest {

    @Autowired
    private AdminClazzService adminClazzService;

    @Test
    void testGetClassInfoByCourseId() {
        //3797 has class.
        int courseId = 3797;
        ArrayList<Clazz> classByCourseId1 = adminClazzService.getClassInfoByCourseId(courseId);
        assertEquals(1, classByCourseId1.size());
        //6167 do not have class.
        int courseId1 = 6167;
        ArrayList<Clazz> classByCourseId2 = adminClazzService.getClassInfoByCourseId(courseId1);
        assertEquals(0, classByCourseId2.size());
    }


    @Test
    void testGetProfessorById() {
        Account professorById = adminClazzService.getProfessorById(2000006);
        assertEquals("Floyd Heasley", professorById.getName());
    }

    @Test
    void testGetProfessorByEmail() {
        Account professorById = adminClazzService.getProfessorByEmail("floydheasley@uottawa.ca");
        assertEquals("Floyd Heasley", professorById.getName());
    }

    @Test
    void getProfessorList() {
        ArrayList<Account> professorList = adminClazzService.getProfessorList();
        for (Account account : professorList) {
            assertEquals(AccountType.professor, account.getType());
        }
    }

    @Test
    void getClassroomSizeList() {
        TreeSet<Integer> classroomSizeList = adminClazzService.getClassroomSizeList();
        assertEquals(5, classroomSizeList.size());
    }

    @Test
    void testAddNew_Update_DeleteClassInfo() {
        int courseId = 6132;
        Clazz newClass = new Clazz();
        ArrayList<Clazz> classInfoByCourseId = adminClazzService.getClassInfoByCourseId(courseId);
        newClass.setCourseId(courseId);
        newClass.setClassDesc("a new class added to course");
        newClass.setClassStatus(ClassStatus.open);
        newClass.setSection(classInfoByCourseId.size());
        newClass.setEnrolled(0);
        newClass.setEnrollCapacity(100);
        newClass.setProfId(2000006);
        newClass.setEnrollDeadline(formatString2Timestamp("2021-2-5 20:30:30"));
        newClass.setDropNoPenaltyDeadline(formatString2Timestamp("2021-3-5 20:30:30"));
        newClass.setDropNoFailDeadline(formatString2Timestamp("2021-4-5 20:30:30"));
        Clazz clazz = adminClazzService.addNewClassInfo(newClass);
        Assert.assertSame(newClass, clazz);

        newClass.setEnrollCapacity(200);
        Clazz clazz1 = adminClazzService.updateClassInfo(newClass);
        assertEquals(newClass.getEnrollCapacity(), clazz1.getEnrollCapacity());
        Integer status = adminClazzService.deleteClassByClassId(newClass.getClassId());
        assertEquals(0, status);
        assertEquals(0, adminClazzService.getClassInfoByCourseId(courseId).size());
    }


    @Test
    void addNew_update_deleteClassSchedules() {
        int courseId = 6135;
        //add a new class first.
        Clazz newClass = new Clazz();
        ArrayList<Clazz> classInfoByCourseId = adminClazzService.getClassInfoByCourseId(courseId);
        newClass.setCourseId(courseId);
        newClass.setClassDesc("a new class added to course");
        newClass.setClassStatus(ClassStatus.open);
        newClass.setSection(classInfoByCourseId.size());
        newClass.setEnrolled(0);
        newClass.setEnrollCapacity(100);
        newClass.setProfId(2000006);
        newClass.setEnrollDeadline(formatString2Timestamp("2021-2-5 20:30:30"));
        newClass.setDropNoPenaltyDeadline(formatString2Timestamp("2021-3-5 20:30:30"));
        newClass.setDropNoFailDeadline(formatString2Timestamp("2021-4-5 20:30:30"));
        Clazz clazz = adminClazzService.addNewClassInfo(newClass);
        Assert.assertSame(newClass, clazz);

        //add a new schedule.
        ArrayList<HashMap<String, String>> newClassroomSchedules = new ArrayList<>();
        HashMap<String, String> newSchedule = new HashMap<>();
        newSchedule.put("weekday", "Tues");
        newSchedule.put("startTime", "10:00:00");
        newSchedule.put("endTime", "12:00:00");
        newSchedule.put("roomCapacityAsked", "100");

        ArrayList<Classroom> classroomAvailable = null;
        try {
            classroomAvailable = adminClazzService.classroomSchedule(newSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newSchedule.put("roomId", classroomAvailable.get(0).getRoomId().toString());
        newClassroomSchedules.add(newSchedule);
        HashMap<String, String> classInfo = new HashMap<>();
        classInfo.put("profId", String.valueOf(clazz.getProfId()));
        classInfo.put("classId", String.valueOf(clazz.getClassId()));
        newClassroomSchedules.add(classInfo);
        Integer status = adminClazzService.addNewClassSchedules(newClassroomSchedules);
        assertEquals(0, status);
        ArrayList<ClassroomSchedule> classSchedulesByClassId = adminClazzService.getClassSchedulesByClassId(clazz.getClassId());
        assertEquals(WeekDay.valueOf(newSchedule.get("weekday")), classSchedulesByClassId.get(0).getWeekday());
        assertEquals(Integer.parseInt(newSchedule.get("roomId")), classSchedulesByClassId.get(0).getRoomId());
        assertEquals(clazz.getProfId(), classSchedulesByClassId.get(0).getProfessorId());

        //update the new schedule.
        ClassroomSchedule classroomSchedule = classSchedulesByClassId.get(0);
        ArrayList<HashMap<String, String>> updateClassroomSchedules = new ArrayList<>();
        HashMap<String, String> updateSchedule = new HashMap<>();
        updateSchedule.put("weekday", classroomSchedule.getWeekday().toString());
        updateSchedule.put("startTime", classroomSchedule.getStartTime().toString());
        updateSchedule.put("endTime", classroomSchedule.getEndTime().toString());
        updateSchedule.put("roomCapacityAsked", String.valueOf(classroomSchedule.getRoomCapacity() + 100));
        try {
            classroomAvailable = adminClazzService.classroomSchedule(updateSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        updateSchedule.put("roomId", classroomAvailable.get(0).getRoomId().toString());
        updateSchedule.put("scheduleId", String.valueOf(classroomSchedule.getScheduleId()));
        updateClassroomSchedules.add(updateSchedule);
        updateClassroomSchedules.add(classInfo);
        status = adminClazzService.updateClassSchedules(updateClassroomSchedules);
        assertEquals(0, status);

        ArrayList<ClassroomSchedule> classSchedulesByClassId1 = adminClazzService.getClassSchedulesByClassId(clazz.getClassId());
        assertEquals(Integer.valueOf(updateSchedule.get("roomCapacityAsked")), classSchedulesByClassId1.get(0).getRoomCapacity());
        assertEquals(Integer.parseInt(updateSchedule.get("roomId")), classSchedulesByClassId1.get(0).getRoomId());
        assertEquals(clazz.getProfId(), classSchedulesByClassId1.get(0).getProfessorId());

        //delete the new added class. and the new added schedule will automatic delete.
        status = adminClazzService.deleteClassByClassId(newClass.getClassId());
        assertEquals(0, status);
        assertEquals(0, adminClazzService.getClassInfoByCourseId(courseId).size());
        assertEquals(0, adminClazzService.getClassSchedulesByClassId(newClass.getClassId()).size());
    }


    private Timestamp formatString2Timestamp(String tsStr) {
        Timestamp ts = null;
        try {
            ts = Timestamp.valueOf(tsStr);
            System.out.println(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

}