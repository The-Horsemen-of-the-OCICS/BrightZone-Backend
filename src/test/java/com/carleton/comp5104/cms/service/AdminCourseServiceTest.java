package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

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
        Course course = adminCourseService.getCourseById(6157);
        if (course != null) {
            adminCourseService.deleteACourse(course.getCourseId());
        }
        course = new Course();
        course.setCourseId(6157);
        course.setCourseName("Nanoengineered Materials Project III");
        course.setCourseSubject("NE");
        course.setCourseNumber("499");
        course.setCourseDesc("xxx");
        course.setCredit(3);
        System.out.println(course.getCourseId());
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(6157);
        assertEquals(courseById, course);

        //add course prerequisite
        ArrayList<Integer> prerequisite = new ArrayList<>();
        prerequisite.add(6156);
        prerequisite.add(6155);
        prerequisite.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePrerequisite(prerequisite, courseId);
        //assertEquals(-1, status);
        ArrayList<Integer> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6154, coursePrerequisite.get(0));
        assertEquals(6155, coursePrerequisite.get(1));
        assertEquals(6156, coursePrerequisite.get(2));

        //add course preclusion
        ArrayList<Integer> preclusion = new ArrayList<>();
        preclusion.add(6156);
        preclusion.add(6155);
        preclusion.add(6154);
        courseId = course.getCourseId();
        status = adminCourseService.addCoursePreclusion(preclusion, courseId);
        //assertEquals(-1, status);
        ArrayList<Integer> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6154, coursePreclusion.get(0));
        assertEquals(6155, coursePreclusion.get(1));
        assertEquals(6156, coursePreclusion.get(2));
    }


    /*
     * try to update a course and it`s related pre requisite and pre conclusion course.
     * */
    @Test
    void updateACourse() {
        //The course 6180 do not exist. So this will return null.
        Course courseById = adminCourseService.getCourseById(6180);
        assertNull(courseById);
        //Search the course 6157 to modify it.
        courseById = adminCourseService.getCourseById(6157);
        //try to change the course number to an already exist number. so the status code should be -1.
        courseById.setCourseNumber("491");
        int status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);
        //try to change the course name to an already exist name. so the status code should be -1.
        courseById.setCourseName("Nanoengineered Polymers");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);
        //try to change the course number to an valid number and no-exist name. The status code should be 0.
        courseById.setCourseNumber("500");
        courseById.setCourseName("Nanoengineered Polymers II");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(0, status);
        //check if the info wrote to database is same with the modified course object.
        Course courseByIdNew = adminCourseService.getCourseById(6157);
        assertEquals(courseById, courseByIdNew);

        //try to update the prerequisite
        ArrayList<Integer> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseById.getCourseId());
        coursePrerequisite.remove(coursePrerequisite.size() - 1);
        coursePrerequisite.add(6139);
        adminCourseService.updateCoursePrerequisite(coursePrerequisite, courseById.getCourseId());

        //try to update the preclusion
        coursePrerequisite = adminCourseService.getCoursePreclusion(courseById.getCourseId());
        coursePrerequisite.remove(coursePrerequisite.size() - 1);
        coursePrerequisite.add(6137);
        adminCourseService.updateCoursePreclusion(coursePrerequisite, courseById.getCourseId());

    }

    @Test
    void newCourseNumberValidCheck1() { //[]
        //situation 1: Already exist;
        String courseSubject = "NE";
        String newCourseNumber = "500";
        Integer status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(-1, status);
        //situation 2: none exist;
        courseSubject = "NE";
        newCourseNumber = "501";
        status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(0, status);
    }

    @Test
    void newCourseNameValidCheck() {
        //valid new course name;
        String courseName = "Nanoengineered Materials Project III";
        Integer status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(0, status);
        //invalid new course name;
        courseName = "Nanoengineered Materials Project II";
        status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(-1, status);
    }


    @Test
    void newCourseNameValidCheck2() {

    }

    @Test
    void deleteACourse() {
        //try to create a new course first
        int status;
        Course course = adminCourseService.getCourseById(6157);
        if (course == null) {
            //add a new course first
            Course newCourse = new Course();
            newCourse.setCourseId(6157);
            newCourse.setCourseName("Nanoengineered Materials Project II");
            newCourse.setCourseSubject("NE");
            newCourse.setCourseNumber("496");
            newCourse.setCourseDesc("xxx");
            newCourse.setCredit(3);
            status = adminCourseService.addNewCourse(newCourse);
            assertEquals(0, status);
            Course courseById = adminCourseService.getCourseById(6157);
            assertEquals(courseById, newCourse);
        }

        //valid delete
        int courseId = course.getCourseId();
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        //invalid delete
        courseId = course.getCourseId();
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(-1, status);
    }

    @Test
    void getSubjects() {
    }

    @Test
    void add_Delete_UpdateCoursePrerequisite() {
        int status;
        Course course = adminCourseService.getCourseById(6157);
        if (course != null) {
            adminCourseService.deleteACourse(course.getCourseId());
        }
        //add a new course first
        Course newCourse = new Course();
        newCourse.setCourseId(6157);
        newCourse.setCourseName("Nanoengineered Materials Project II");
        newCourse.setCourseSubject("NE");
        newCourse.setCourseNumber("496");
        newCourse.setCourseDesc("xxx");
        newCourse.setCredit(3);
        status = adminCourseService.addNewCourse(newCourse);
        assertEquals(0, status);
        course = adminCourseService.getCourseById(6157);
        assertEquals(course, newCourse);


        //add course prerequisite
        ArrayList<Integer> prerequisite = new ArrayList<>();
        prerequisite.add(6156);
        prerequisite.add(6155);
        prerequisite.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePrerequisite(prerequisite, courseId);
        //assertEquals(-1, status);
        ArrayList<Integer> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6154, coursePrerequisite.get(0));
        assertEquals(6155, coursePrerequisite.get(1));
        assertEquals(6156, coursePrerequisite.get(2));

        //try to delete course prerequisite. 6154 is no longer a prerequisite to course 6157
        ArrayList<Integer> deleteCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        deleteCoursePrerequisite.remove(2);
        deleteCoursePrerequisite.remove(1);
        status = adminCourseService.deleteCoursePrerequisite(deleteCoursePrerequisite, courseId);
        assertEquals(0, status);
        ArrayList<Integer> newCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6155, newCoursePrerequisite.get(0));
        assertEquals(6156, newCoursePrerequisite.get(1));

        //try to update course prerequisite.
        //first. try to update an un exist course.
        ArrayList<Integer> updatedCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        updatedCoursePrerequisite.remove(1);
        updatedCoursePrerequisite.add(6180); //6155,6180
        status = adminCourseService.updateCoursePrerequisite(updatedCoursePrerequisite, courseId);
        assertEquals(-1, status);
        //then. try to update valid course.
        updatedCoursePrerequisite.remove(1);
        updatedCoursePrerequisite.add(6139);
        status = adminCourseService.updateCoursePrerequisite(updatedCoursePrerequisite, courseId);
        assertEquals(0, status);
    }

    @Test
    void add_Delete_UpdateCoursePreclusion() {

        int status;
        Course course = adminCourseService.getCourseById(6157);
        adminCourseService.deleteACourse(course.getCourseId());

        //add a new course first
        Course newCourse = new Course();
        newCourse.setCourseId(6157);
        newCourse.setCourseName("Nanoengineered Materials Project II");
        newCourse.setCourseSubject("NE");
        newCourse.setCourseNumber("496");
        newCourse.setCourseDesc("xxx");
        newCourse.setCredit(3);
        status = adminCourseService.addNewCourse(newCourse);
        assertEquals(0, status);
        course = adminCourseService.getCourseById(6157);
        assertEquals(course, newCourse);


        //add course preclusion
        ArrayList<Integer> preclusion = new ArrayList<>();
        preclusion.add(6156);
        preclusion.add(6155);
        preclusion.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePreclusion(preclusion, courseId);
        //assertEquals(-1, status);
        ArrayList<Integer> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6154, coursePreclusion.get(0));
        assertEquals(6155, coursePreclusion.get(1));
        assertEquals(6156, coursePreclusion.get(2));

        //try to delete course preclusion. 6154 is no longer a preclusion to course 6157
        ArrayList<Integer> deleteCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        deleteCoursePreclusion.remove(2);
        deleteCoursePreclusion.remove(1);
        status = adminCourseService.deleteCoursePreclusion(deleteCoursePreclusion, courseId);
        assertEquals(0, status);
        ArrayList<Integer> newCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6155, newCoursePreclusion.get(0));
        assertEquals(6156, newCoursePreclusion.get(1));

        //try to update course preclusion.
        //first. try to update an un exist course.
        ArrayList<Integer> updatedCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        updatedCoursePreclusion.remove(1);
        updatedCoursePreclusion.add(6180); //6155,6180
        status = adminCourseService.updateCoursePreclusion(updatedCoursePreclusion, courseId);
        assertEquals(-1, status);
        //then. try to update valid course.
        updatedCoursePreclusion.remove(1);
        updatedCoursePreclusion.add(6139);//6155,6139
        status = adminCourseService.updateCoursePreclusion(updatedCoursePreclusion, courseId);
        assertEquals(0, status);
    }
}