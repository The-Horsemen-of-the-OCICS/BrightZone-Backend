package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.repository.CourseRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminCourseServiceTest {
    @Autowired
    private AdminCourseService adminCourseService;
    @Autowired
    private CourseRepository courseRepository;


    @Test
    void getCourseTableSize() {
        Integer courseTableSize = adminCourseService.getCourseTableSize();
        assertNotEquals(0, courseTableSize);
    }

    //try to update a course and it`s related pre requisite and pre conclusion course.
    @Test
    void testUpdateACourse() {

        //first add a new course for edit.
        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int newCourseNumber = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        Course course = new Course();
        course.setCourseName("Test Course " + lastCourse.getCourseName().split(" ")[2] + "I");
        course.setCourseSubject("NE");
        course.setCourseNumber("" + newCourseNumber);
        course.setCourseDesc("xxx");
        course.setCredit(3);

        //try to add this new course to database.
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course newLastCourse = adminCourseService.getLastCourse();
        Course newAddedCourse = adminCourseService.getCourseById(newLastCourse.getCourseId());
        assertEquals(Integer.parseInt(newAddedCourse.getCourseNumber()), newCourseNumber);

        //Search the new added course & modify it.
        Course courseById = adminCourseService.getCourseById(course.getCourseId());

        //try to change the course number to an already exist number. so the status code should be -1.
        courseById.setCourseNumber("491");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);

        //try to change the course name to an already exist name. so the status code should be -1.
        courseById.setCourseName("Nanoengineered Polymers");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);
        //try to change the course number to an valid number and no-exist name. The status code should be 0.
        newCourseNumber = newCourseNumber + 1;
        courseById.setCourseNumber("" + newCourseNumber);
        courseById.setCourseName("xxxxx");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(0, status);

        //check if the info wrote to database is same with the modified course object.
        Course courseByIdNew = adminCourseService.getCourseById(course.getCourseId());
        assertEquals(courseById, courseByIdNew);
        //delete the new added course.
        status = adminCourseService.deleteACourse(courseById.getCourseId());
        assertEquals(0, status);
        assertNull(adminCourseService.getCourseById(courseById.getCourseId()));
        ;
    }

    @Test
    void add_Delete_UpdateCoursePrerequisite() {
        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int newCourseNumber = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        Course course = new Course();
        course.setCourseName("Test Course " + lastCourse.getCourseName().split(" ")[2] + "I");
        course.setCourseSubject("NE");
        course.setCourseNumber("" + newCourseNumber);
        course.setCourseDesc("xxx");
        course.setCredit(3);

        //try to add this new course to database.
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course newLastCourse = adminCourseService.getLastCourse();
        Course newAddedCourse = adminCourseService.getCourseById(newLastCourse.getCourseId());
        assertEquals(Integer.parseInt(newAddedCourse.getCourseNumber()), newCourseNumber);

        //add course prerequisite
        ArrayList<Integer> prerequisite = new ArrayList<>();
        prerequisite.add(6156);
        prerequisite.add(6155);
        prerequisite.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePrerequisite(prerequisite, courseId);
        assertEquals(0, status);
        List<Course> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6154, coursePrerequisite.get(0).getCourseId());
        assertEquals(6155, coursePrerequisite.get(1).getCourseId());
        assertEquals(6156, coursePrerequisite.get(2).getCourseId());

        //try to delete course prerequisite. 6154 is no longer a prerequisite course.
        List<Course> deleteCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        status = adminCourseService.deleteCoursePrerequisite(courseId, deleteCoursePrerequisite.get(0).getCourseId());
        assertEquals(0, status);
        List<Course> newCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6155, newCoursePrerequisite.get(0).getCourseId());
        assertEquals(6156, newCoursePrerequisite.get(1).getCourseId());

        //try to update course prerequisite.
        //first. try to update an un exist course.
        List<Course> updatedCoursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        updatedCoursePrerequisite.remove(1);
        updatedCoursePrerequisite.add(ANewCourse(6880)); //6155,6880
        try {
            status = adminCourseService.updateCoursePrerequisite(getCourseIdFromCourseList(updatedCoursePrerequisite), courseId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        assertEquals(-1, status);
        //then. try to update valid course.
        updatedCoursePrerequisite.remove(1);
        updatedCoursePrerequisite.add(adminCourseService.getCourseById(6139));
        try {
            status = adminCourseService.updateCoursePrerequisite(getCourseIdFromCourseList(updatedCoursePrerequisite), courseId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        assertEquals(0, status);
        //valid delete
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(0, coursePrerequisite.size());
    }

    @Test
    void add_Delete_UpdateCoursePreclusion() {

        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int newCourseNumber = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        Course course = new Course();
        course.setCourseName("Test Course " + lastCourse.getCourseName().split(" ")[2] + "I");
        course.setCourseSubject("NE");
        course.setCourseNumber("" + newCourseNumber);
        course.setCourseDesc("xxx");
        course.setCredit(3);

        //try to add this new course to database.
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course newLastCourse = adminCourseService.getLastCourse();
        Course newAddedCourse = adminCourseService.getCourseById(newLastCourse.getCourseId());
        assertEquals(Integer.parseInt(newAddedCourse.getCourseNumber()), newCourseNumber);


        //add course preclusion
        ArrayList<Integer> preclusion = new ArrayList<>();
        preclusion.add(6156);
        preclusion.add(6155);
        preclusion.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePreclusion(preclusion, courseId);
        assertEquals(0, status);
        List<Course> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6154, coursePreclusion.get(0).getCourseId());
        assertEquals(6155, coursePreclusion.get(1).getCourseId());
        assertEquals(6156, coursePreclusion.get(2).getCourseId());

        //try to delete course preclusion. 6154 is no longer a preclusion course.
        List<Course> deleteCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        status = adminCourseService.deleteCoursePreclusion(courseId, deleteCoursePreclusion.get(0).getCourseId());
        assertEquals(0, status);
        List<Course> newCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6155, newCoursePreclusion.get(0).getCourseId());
        assertEquals(6156, newCoursePreclusion.get(1).getCourseId());

        //try to update course preclusion.
        //first. try to update an un exist course.
        List<Course> updatedCoursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        updatedCoursePreclusion.remove(1);
        updatedCoursePreclusion.add(ANewCourse(6880)); //6155,6180
        status = adminCourseService.updateCoursePreclusion(getCourseIdFromCourseList(updatedCoursePreclusion), courseId);
        assertEquals(-1, status);
        //then. try to update valid course.
        updatedCoursePreclusion.remove(1);
        updatedCoursePreclusion.add(adminCourseService.getCourseById(6139));//6155,6139
        status = adminCourseService.updateCoursePreclusion(getCourseIdFromCourseList(updatedCoursePreclusion), courseId);
        assertEquals(0, status);

        //valid delete
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(0, coursePreclusion.size());
    }

    private ArrayList<Integer> getCourseIdFromCourseList(List<Course> courseArrayList) {
        ArrayList<Integer> coursePrerequisiteId = new ArrayList<>();
        for (Course course : courseArrayList) {
            coursePrerequisiteId.add(course.getCourseId());
        }
        return coursePrerequisiteId;
    }

    private Course ANewCourse(int courseId) {
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseId + "xxx");
        course.setCourseSubject("ASD");
        course.setCourseNumber(courseId + "");
        course.setCourseDesc("aaa");
        course.setCourseDesc("3");
        return course;
    }

    @Test
    void testGetAllCourse() {
        int pageNum = 1;
        int pageSize = 10;
        Page<Course> allCourse = adminCourseService.getAllCourse(pageNum, pageSize);
        System.out.println(allCourse.getSize());
        assertEquals(pageSize, allCourse.getSize());
    }

    @Test
    void testGetCourseTableSize() {
        int targetSize = courseRepository.findAll().size();
        Integer courseTableSize = adminCourseService.getCourseTableSize();
        assertEquals(targetSize, courseTableSize);
    }

    @Test
    void getLastCourse() {

    }

    @Test
    void testGetCourseById() {
        int courseId = 1000;
        Course courseById = adminCourseService.getCourseById(courseId);
        assertEquals("History and Orientation", courseById.getCourseName());
        assertEquals(courseId, courseById.getCourseId());
    }

    @Test
    void testAdd_DeleteANewCourse() {
        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int newCourseNumber = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        Course course = new Course();
        course.setCourseName("Test Course " + lastCourse.getCourseName().split(" ")[2] + "I");
        course.setCourseSubject("NE");
        course.setCourseNumber("" + newCourseNumber);
        course.setCourseDesc("xxx");
        course.setCredit(3);

        //try to add this new course to database.
        int status = adminCourseService.addNewCourse(course);
        assertEquals(0, status);
        Course newLastCourse = adminCourseService.getLastCourse();
        Course newAddedCourse = adminCourseService.getCourseById(newLastCourse.getCourseId());
        assertEquals(Integer.parseInt(newAddedCourse.getCourseNumber()), newCourseNumber);

        //add course prerequisite
        ArrayList<Integer> prerequisite = new ArrayList<>();
        prerequisite.add(6156);
        prerequisite.add(6155);
        prerequisite.add(6154);
        int courseId = course.getCourseId();
        status = adminCourseService.addCoursePrerequisite(prerequisite, courseId);
        assertEquals(0, status);
        List<Course> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6154, coursePrerequisite.get(0).getCourseId());
        assertEquals(6155, coursePrerequisite.get(1).getCourseId());
        assertEquals(6156, coursePrerequisite.get(2).getCourseId());

        //add course preclusion
        ArrayList<Integer> preclusion = new ArrayList<>();
        preclusion.add(6156);
        preclusion.add(6155);
        preclusion.add(6154);
        courseId = course.getCourseId();
        status = adminCourseService.addCoursePreclusion(preclusion, courseId);
        assertEquals(0, status);
        List<Course> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6154, coursePreclusion.get(0).getCourseId());
        assertEquals(6155, coursePreclusion.get(1).getCourseId());
        assertEquals(6156, coursePreclusion.get(2).getCourseId());

        //valid delete
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(0, coursePrerequisite.size());
        coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(0, coursePreclusion.size());
    }


    @Test
    void newCourseNumberValidCheck() {
        //situation 1: Already exist;
        String courseSubject = "NE";
        String newCourseNumber = "491";
        Integer status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(-1, status);
        //situation 2: none exist;
        courseSubject = "NE";
        newCourseNumber = "1000";
        status = adminCourseService.newCourseNumberValidCheck(courseSubject, newCourseNumber);
        assertEquals(0, status);
    }

    @Test
    void testNewCourseNameValidCheck() {
        //valid new course name;
        String courseName = "Nanoengineered Materials Project III";
        Integer status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(0, status);
        //invalid new course name;
        courseName = "Nanoengineered Polymers";
        status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(-1, status);
    }

    @Test
    void testGetCourseBySubjectAndNumber() {
        String subject = "OPTOM";
        String number = "103";
        int courseId = 1001;
        Course courseBySubjectAndNumber = adminCourseService.getCourseBySubjectAndNumber(subject, number);
        assertEquals(courseId, courseBySubjectAndNumber.getCourseId());
    }

    @Test
    void testGetCourseBySubject() {
        String subject = "PACS";
        ArrayList<Course> courseBySubject = adminCourseService.getCourseBySubject(subject);
        for (Course course : courseBySubject) {
            assertEquals(subject, course.getCourseSubject());
        }
    }


    @Test
    void testGetSubjects() {
        ArrayList<HashMap<String, String>> subjects = adminCourseService.getSubjects();
        Assert.assertTrue(subjects.size() > 0);
    }


}