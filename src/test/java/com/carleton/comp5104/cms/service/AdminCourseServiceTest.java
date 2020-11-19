package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class AdminCourseServiceTest {
    @Autowired
    private AdminCourseService adminCourseService;


    @Test
    void getAllCourse() {
        int pageNum = 1;
        int pageSize = 10;
        Page<Course> allCourse = adminCourseService.getAllCourse(pageNum, pageSize);
        System.out.println(allCourse.getSize());
        assertEquals(pageSize, allCourse.getSize());
    }

    @Test
    void getCourseById() {
        int courseId = 1000;
        String name = "History and Orientation";
        Course courseById = adminCourseService.getCourseById(courseId);
        assertEquals(courseId, courseById.getCourseId());
        assertEquals(name, courseById.getCourseName());
    }

    @Test
    void addNewCourse() {
        Course course = new Course();
        course.setCourseId(6157);
        course.setCourseName("Nanoengineered Materials Project II");
        course.setCourseSubject("NE");
        course.setCourseNumber("496");
        course.setCourseDesc("xxx");
        course.setCredit(3);
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(6157);
        assertEquals(courseById, course);
    }


    @Test
    void updateACourse() {
        Course courseById = adminCourseService.getCourseById(6157);
        courseById.setCourseNumber("500");
        int status = adminCourseService.updateACourse(courseById);
        assertEquals(0, status);
        Course courseById1 = adminCourseService.getCourseById(6157);
        assertEquals(courseById, courseById1);
    }

    @Test
    void newCourseNumberValidCheck1() { //[]
        //situation 1: Already exist;
        String courseSubject = "NE";
        String newCourseNumber = "500";
        Integer status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(-1, status);
    }

    @Test
    void newCourseNumberValidCheck2() {
        //situation 2: none exist;
        String courseSubject = "NE";
        String newCourseNumber = "501";
        int status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(0, status);
    }

    @Test
    void newCourseNameValidCheck1() {
        //valid new course name;
        String courseName = "Nanoengineered Materials Project III";
        Integer status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(0, status);
    }


    @Test
    void newCourseNameValidCheck2() {
        //invalid new course name;
        String courseName = "Nanoengineered Materials Project II";
        Integer status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(-1, status);
    }

    @Test
    void deleteACourse() {
        Course course = new Course();
        course.setCourseId(6157);
        course.setCourseName("Nanoengineered Materials Project II");
        course.setCourseSubject("NE");
        course.setCourseNumber("496");
        course.setCourseDesc("xxx");
        course.setCredit(3);
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(6157);
        assertEquals(courseById, course);
        //valid delete
        int courseId = 6157;
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        //invalid delete
        courseId = 6159;
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(-1, status);
    }

    @Test
    void getSubjects() {
    }
}