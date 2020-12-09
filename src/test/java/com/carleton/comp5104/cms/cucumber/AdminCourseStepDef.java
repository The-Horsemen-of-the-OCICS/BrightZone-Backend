package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.service.AdminClazzService;
import com.carleton.comp5104.cms.service.AdminCourseService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminCourseStepDef {

    @Autowired
    private AdminCourseService adminCourseService;
    @Autowired
    private AdminClazzService adminClazzService;

    private Course newCourse;
    private int isValid;
    private Course courseBySubjectAndNumber;

    @Given("the admin press the add course button")
    public void the_admin_press_the_add_course_button() {
        newCourse = new Course();
    }

    @When("the admin input the new {string} and {string}")
    public void the_admin_input_the_new_and(String courseSubject, String courseNumber) {
        newCourse.setCourseSubject(courseSubject);
        newCourse.setCourseNumber(courseNumber);
    }

    @When("the system check if the course number has conflicts")
    public void the_system_check_if_the_course_number_has_conflicts() {
        isValid = adminCourseService.newCourseNumberValidCheck(newCourse.getCourseSubject(), newCourse.getCourseNumber());
    }

    @When("the system output the course number is {string}")
    public void the_system_output_the_course_number_is(String string) {
        if (string.equals("repeat")) {
            Assert.assertEquals(-1, isValid);
            System.out.println("the course number is repeated");
        } else {
            Assert.assertEquals(0, isValid);
            System.out.println("the course number is valid");
        }
    }

    @When("the admin input the new {string}")
    public void the_admin_input_the_new(String courseName) {
        newCourse.setCourseName(courseName);
    }

    @When("the system check if the course name has conflicts")
    public void the_system_check_if_the_course_name_has_conflicts() {
        isValid = adminCourseService.newCourseNameValidCheck(newCourse.getCourseName());
    }

    @When("the system output the course name is {string}")
    public void the_system_output_the_course_name_is(String string) {
        if (string.equals("repeat")) {
            Assert.assertEquals(-1, isValid);
            System.out.println("the course name is repeated");
        } else {
            Assert.assertEquals(0, isValid);
            System.out.println("the course name is valid");
        }
    }

    @When("the admin assign a {int} to the new course")
    public void the_admin_assign_a_to_the_new_course(Integer credit) {
        newCourse.setCredit(credit);
    }

    @When("the admin input the {string} of the course")
    public void the_admin_input_the_of_the_course(String description) {
        newCourse.setCourseDesc(description);
    }

    @Then("the admin press the submit button")
    public void the_admin_press_the_submit_button() {
        System.out.println("Admin add a new course to CMS");
    }

    @Then("the system add the new course")
    public void the_system_add_the_new_course() {
        int integer = adminCourseService.addNewCourse(newCourse);
        Assert.assertEquals(0, integer);
        Course courseById = adminCourseService.getCourseById(newCourse.getCourseId());
        Assert.assertSame(courseById.getCourseId(), newCourse.getCourseId());
        adminCourseService.deleteACourse(courseById.getCourseId());
    }

    @Given("A test course has been added to the course table")
    public void a_test_course_has_been_added_to_the_course_table() {
        //construct a valid course first.
        Course lastCourse = adminCourseService.getLastCourse();
        int newCourseNumber = Integer.parseInt(lastCourse.getCourseNumber()) + 1;
        newCourse = new Course();
        newCourse.setCourseName("Test Course " + lastCourse.getCourseName().split(" ")[2] + "I");
        newCourse.setCourseSubject("NE");
        newCourse.setCourseNumber("" + newCourseNumber);
        newCourse.setCourseDesc("xxx");
        newCourse.setCredit(3);

        //try to add this new course to database.
        int status = adminCourseService.addNewCourse(newCourse);
        assertEquals(0, status);
        Course newLastCourse = adminCourseService.getLastCourse();
        Course newAddedCourse = adminCourseService.getCourseById(newLastCourse.getCourseId());
        assertEquals(Integer.parseInt(newAddedCourse.getCourseNumber()), newCourseNumber);


    }

    @Given("some prerequisite course has been added to the course")
    public void some_prerequisite_course_has_been_added_to_the_course() {
        //add course prerequisite
        ArrayList<Integer> prerequisite = new ArrayList<>();
        prerequisite.add(6156);
        prerequisite.add(6155);
        prerequisite.add(6154);
        int courseId = newCourse.getCourseId();
        int status = adminCourseService.addCoursePrerequisite(prerequisite, courseId);
        assertEquals(0, status);
        List<Course> coursePrerequisite = adminCourseService.getCoursePrerequisite(courseId);
        assertEquals(6154, coursePrerequisite.get(0).getCourseId());
        assertEquals(6155, coursePrerequisite.get(1).getCourseId());
        assertEquals(6156, coursePrerequisite.get(2).getCourseId());
    }

    @Given("some preclusion course has been added to the course")
    public void some_preclusion_course_has_been_added_to_the_course() {
        //add course preclusion
        ArrayList<Integer> preclusion = new ArrayList<>();
        preclusion.add(6156);
        preclusion.add(6155);
        preclusion.add(6154);
        int courseId = newCourse.getCourseId();
        int status = adminCourseService.addCoursePreclusion(preclusion, courseId);
        assertEquals(0, status);
        List<Course> coursePreclusion = adminCourseService.getCoursePreclusion(courseId);
        assertEquals(6154, coursePreclusion.get(0).getCourseId());
        assertEquals(6155, coursePreclusion.get(1).getCourseId());
        assertEquals(6156, coursePreclusion.get(2).getCourseId());
    }

    @When("the admin search the course with the {string} and {string}")
    public void the_admin_search_the_course_with_the_and(String courseSubject, String courseNumber) {
        courseBySubjectAndNumber = adminCourseService.getCourseBySubjectAndNumber(courseSubject, courseNumber);
    }

    @When("the system output no such course")
    public void the_system_output_no_such_course() {
        Assert.assertNull(courseBySubjectAndNumber);
        System.out.println("No such course");
    }

    @When("the system output the course Info")
    public void the_system_output_the_course_info() {
        Assert.assertNotNull(courseBySubjectAndNumber);
        System.out.println(courseBySubjectAndNumber.toString());
    }

    @Then("the system delete the prerequisite and preclusion")
    public void the_system_delete_the_prerequisite_and_preclusion() {
        List<Course> coursePrerequisiteList = adminCourseService.getCoursePrerequisite(newCourse.getCourseId());
        for (Course prerequisite : coursePrerequisiteList) {
            adminCourseService.deleteCoursePrerequisite(newCourse.getCourseId(), prerequisite.getCourseId());
        }
        Assert.assertEquals(0, adminCourseService.getCoursePrerequisite(newCourse.getCourseId()).size());
        System.out.println("success delete prerequisite course");
        List<Course> coursePreclusionList = adminCourseService.getCoursePreclusion(newCourse.getCourseId());
        for (Course preclusion : coursePreclusionList) {
            adminCourseService.deleteCoursePreclusion(newCourse.getCourseId(), preclusion.getCourseId());
        }
        Assert.assertEquals(0, adminCourseService.getCoursePreclusion(newCourse.getCourseId()).size());
        System.out.println("success delete preclusion course");
    }

    @Then("the system delete the course and classes under this course")
    public void the_system_delete_the_course_and_classes_under_this_course() {
        int status = adminCourseService.deleteACourse(newCourse.getCourseId());
        Assert.assertEquals(0, status);
        Assert.assertNull(adminCourseService.getCourseById(newCourse.getCourseId()));
        ArrayList<Clazz> classInfoByCourseId = adminClazzService.getClassInfoByCourseId(newCourse.getCourseId());
        Assert.assertEquals(0, classInfoByCourseId.size());
    }
}
