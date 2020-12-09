package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.service.AdminClazzService;
import com.carleton.comp5104.cms.service.AdminCourseService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

public class AdminClazzStepDef {

    @Autowired
    AdminClazzService adminClazzService;

    @Autowired
    AdminCourseService adminCourseService;


    private Clazz newClazz;
    private Course upperCourse;

    @Given("the system received the new added course id")
    public void the_system_received_the_new_added_course_id() {
        upperCourse = adminCourseService.getLastCourse();
    }

    @When("The admin press {string} button to add class")
    public void the_admin_press_button_to_add_class(String string) {
        newClazz = new Clazz();
    }

    @When("the new class is under the new added course")
    public void the_new_class_is_under_the_new_added_course() {
        newClazz.setCourseId(upperCourse.getCourseId());
    }

    @When("the admin assign a {int} to the class")
    public void the_admin_assign_a_to_the_class(Integer profId) {
        newClazz.setProfId(profId);
    }

    @When("the admin input the class section {int}")
    public void the_admin_input_the_class_section(Integer section) {
        newClazz.setSection(section);
    }

    @When("the admin input the class enrolled {int}")
    public void the_admin_input_the_class_enrolled(Integer enrolled) {
        newClazz.setEnrolled(enrolled);
    }

    @When("the admin input the class enrollCapacity {int}")
    public void the_admin_input_the_class_enroll_capacity(Integer enrollCapacity) {
        newClazz.setEnrolled(enrollCapacity);
    }

    @When("the admin choose the enrolled Deadline {string}")
    public void the_admin_choose_the_enrolled_deadline(String enrolledDL) {
        newClazz.setEnrollDeadline(formatString2Timestamp(enrolledDL));
    }

    @When("the admin choose the Drop no penalty Deadline {string}")
    public void the_admin_choose_the_drop_no_penalty_deadline(String DnpDL) {
        newClazz.setDropNoPenaltyDeadline(formatString2Timestamp(DnpDL));
    }

    @When("the admin choose the Drop no fail Deadline {string}")
    public void the_admin_choose_the_drop_no_fail_deadline(String DnfDL) {
        newClazz.setDropNoFailDeadline(formatString2Timestamp(DnfDL));
    }

    @When("the admin input the class description {string}")
    public void the_admin_input_the_class_description(String Description) {
        newClazz.setClassDesc(Description);
    }


    @Then("The admin press {string} button to submit class")
    public void the_admin_press_button_to_submit_class(String code) {
        System.out.println("admin press the " + code + " button to add the new class to table¬");
    }

    @Then("the system add the new class")
    public void the_system_add_the_new_class() {
        Clazz clazz = adminClazzService.addNewClassInfo(newClazz);
        Assert.assertSame(newClazz, clazz);

        //delete the new added class and course
        adminCourseService.deleteACourse(upperCourse.getCourseId());

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
