package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void getCourseTableSize() {
        Integer courseTableSize = adminCourseService.getCourseTableSize();
        assertNotEquals(0, courseTableSize);
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
    }


    /*
     * try to update a course and it`s related pre requisite and pre conclusion course.
     * */
    @Test
    void updateACourse() {
        //Case 1. The course do not exist. So this will return null.
        Course lastCourse = adminCourseService.getLastCourse();
        Course courseById = adminCourseService.getCourseById(lastCourse.getCourseId() + 1);
        assertNull(courseById);
        //Search the last course & modify it.
        courseById = adminCourseService.getCourseById(lastCourse.getCourseId());
        //try to change the course number to an already exist number. so the status code should be -1.
        courseById.setCourseNumber("491");
        int status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);
        //try to change the course name to an already exist name. so the status code should be -1.
        courseById.setCourseName("Nanoengineered Polymers");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(-1, status);
        //try to change the course number to an valid number and no-exist name. The status code should be 0.
        int nuwCourseNum = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        courseById.setCourseNumber("" + nuwCourseNum);
        courseById.setCourseName("Nanoengineered Polymers II");
        status = adminCourseService.updateACourse(courseById);
        assertEquals(0, status);
        //check if the info wrote to database is same with the modified course object.
        Course courseByIdNew = adminCourseService.getCourseById(lastCourse.getCourseId());
        assertEquals(courseById, courseByIdNew);

        //try to update the prerequisite
        List<Course> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseById.getCourseId());
        coursePrerequisite.remove(coursePrerequisite.size() - 1);
        coursePrerequisite.add(adminCourseService.getCourseById(6139));

        try {
            adminCourseService.updateCoursePrerequisite(getCourseIdFromCourseList(coursePrerequisite), courseById.getCourseId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //try to update the preclusion
        List<Course> coursePreclusion = adminCourseService.getCoursePreclusion(courseById.getCourseId());
        coursePreclusion.remove(coursePrerequisite.size() - 1);
        coursePreclusion.add(adminCourseService.getCourseById(6137));
        adminCourseService.updateCoursePreclusion(getCourseIdFromCourseList(coursePrerequisite), courseById.getCourseId());

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
        newCourseNumber = "1000";
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
        courseName = "Nanoengineered Polymers";
        status = adminCourseService.newCourseNameValidCheck(courseName);
        assertEquals(-1, status);
    }

    @Test
    void deleteACourse() {
        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int status = -1;
        //valid delete
        int courseId = lastCourse.getCourseId();
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(0, status);
        Course courseById = adminCourseService.getCourseById(courseId);
        assertNull(courseById);
        List<Course> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(0, coursePrerequisite.size());
        List<Course> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(0, coursePreclusion.size());


        //invalid delete
        courseId = lastCourse.getCourseId();
        status = adminCourseService.deleteACourse(courseId);
        assertEquals(-1, status);
    }

    @Test
    void getSubjects() {
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
}