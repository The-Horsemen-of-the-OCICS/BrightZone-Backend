package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.*;
import com.carleton.comp5104.cms.enums.ClassStatus;
import com.carleton.comp5104.cms.enums.WeekDay;
import com.carleton.comp5104.cms.service.AdminClazzService;
import com.carleton.comp5104.cms.service.AdminCourseService;
import com.carleton.comp5104.cms.service.DeliverableService;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminClazzStepDef {

    @Autowired
    AdminClazzService adminClazzService;

    @Autowired
    AdminCourseService adminCourseService;

    @Autowired
    DeliverableService deliverableService;

    @Autowired
    ProfessorService professorService;


    private Clazz newClazz;
    private Course upperCourse;

    private boolean isValid;

    private ClassroomSchedule classroomSchedule;

    private ArrayList<HashMap<String, String>> newClassroomSchedules;
    private HashMap<String, String> singleSchedule;
    private ArrayList<Classroom> availableClassrooms;

    @Given("the admin search the new added course")
    public void the_admin_search_the_new_added_course() {
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
        if (isValid) {
            Clazz clazz = adminClazzService.addNewClassInfo(newClazz);
            Assert.assertSame(newClazz, clazz);

            //delete the new added class and course
            adminCourseService.deleteACourse(upperCourse.getCourseId());
        } else {
            System.out.println("Invalid input, check again!");
        }
    }

    @When("the system check if the section Num is valid")
    public void the_system_check_if_the_section_num_is_valid() {
        ArrayList<Clazz> classInfoByCourseId = adminClazzService.getClassInfoByCourseId(upperCourse.getCourseId());
        for (Clazz clazz : classInfoByCourseId) {
            if (clazz.getSection() == newClazz.getSection()) {
                System.out.println("Repeat section number");
                isValid = false;
            }
        }
        isValid = true;
        System.out.println("valid section number");
    }

    @When("the system check if the enrolledNum and enrolledCapacity is valid")
    public void the_system_check_if_the_enrolled_num_and_enrolled_capacity_is_valid() {
        if (newClazz.getEnrolled() > newClazz.getEnrollCapacity()) {
            System.out.println("Invalid number");
            isValid = false;
        } else {
            System.out.println("Valid input");
            isValid = true;
        }
    }

    @When("the system check if the three is valid")
    public void the_system_check_if_the_three_is_valid() {
        Timestamp enrollDeadline = newClazz.getEnrollDeadline();
        Timestamp dropNoPenaltyDeadline = newClazz.getDropNoPenaltyDeadline();
        Timestamp dropNoFailDeadline = newClazz.getDropNoFailDeadline();
        if (enrollDeadline.before(new Timestamp(System.currentTimeMillis()))) {
            isValid = false;
        } else if (dropNoPenaltyDeadline.before(enrollDeadline)) {
            isValid = false;
        } else if (dropNoFailDeadline.before(dropNoPenaltyDeadline)) {
            isValid = false;
        } else {
            isValid = true;
        }
    }

    @Given("the test class has been added to the class table")
    public void the_test_class_has_been_added_to_the_class_table() {
        newClazz = new Clazz();
        newClazz.setCourseId(upperCourse.getCourseId());
        newClazz.setProfId(2000000);
        newClazz.setSection(1);
        newClazz.setEnrolled(0);
        newClazz.setEnrollCapacity(2);
        newClazz.setEnrollDeadline(formatString2Timestamp("2021-2-10 20:30:30"));
        newClazz.setDropNoPenaltyDeadline(formatString2Timestamp("2021-3-1 20:30:30"));
        newClazz.setDropNoFailDeadline(formatString2Timestamp("2021-4-1 20:30:30"));
        newClazz.setClassStatus(ClassStatus.open);
        newClazz.setClassDesc("Test class 1");
        Clazz clazz = adminClazzService.addNewClassInfo(newClazz);
        Assert.assertEquals(newClazz, clazz);
    }

    @When("the admin press the add schedule button")
    public void the_admin_press_the_add_schedule_button() {
        newClassroomSchedules = new ArrayList<>();
        singleSchedule = new HashMap<>();
        //classroomSchedule = new ClassroomSchedule();
    }

    @When("the admin choose the {string} of the schedule")
    public void the_admin_choose_the_of_the_schedule(String weekDay) {
        singleSchedule.put("weekday", weekDay);
    }

    @When("the admin choose the start Time {string}")
    public void the_admin_choose_the_start_time(String startTime) {
        singleSchedule.put("startTime", startTime);
    }

    @When("the admin choose the end time {string}")
    public void the_admin_choose_the_end_time(String endTime) {
        singleSchedule.put("endTime", endTime);
    }

    @When("the admin choose the {string} the schedule needs")
    public void the_admin_choose_the_the_schedule_needs(String roomCapacityAsked) {
        singleSchedule.put("roomCapacityAsked", roomCapacityAsked);
    }

    @When("the system check if there is any room available")
    public void the_system_check_if_there_is_any_room_available() {
        try {
            availableClassrooms = adminClazzService.classroomSchedule(singleSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (availableClassrooms.size() != 0) {
            System.out.println("Has room");
            Assert.assertEquals(availableClassrooms.get(0).getRoomCapacity(), Integer.valueOf(singleSchedule.get("roomCapacityAsked")));
        }
    }

    @When("the system output the available room")
    public void the_system_output_the_available_room() {
        for (Classroom classroom : availableClassrooms) {
            System.out.println(classroom.toString());
        }
    }

    @When("the admin select a room for this schedule")
    public void the_admin_select_a_room_for_this_schedule() {
        singleSchedule.put("roomId", String.valueOf(availableClassrooms.get(0).getRoomId()));
    }

    @Then("the admin save the new schedule")
    public void the_admin_save_the_new_schedule() {
        newClassroomSchedules.add(singleSchedule);
        HashMap<String, String> classInfo = new HashMap<>();
        classInfo.put("profId", String.valueOf(newClazz.getProfId()));
        classInfo.put("classId", String.valueOf(newClazz.getClassId()));
        newClassroomSchedules.add(classInfo);
        int status = adminClazzService.addNewClassSchedules(newClassroomSchedules);
        Assert.assertEquals(0, status);
        ArrayList<ClassroomSchedule> classSchedulesByClassId = adminClazzService.getClassSchedulesByClassId(newClazz.getClassId());
        int roomId = availableClassrooms.get(0).getRoomId();
        int roomId1 = classSchedulesByClassId.get(0).getRoomId();
        Assert.assertEquals(roomId, roomId1);

        adminCourseService.deleteACourse(upperCourse.getCourseId());
    }

    @When("the admin press {string} class button")
    public void the_admin_press_class_button(String string) {
        System.out.println("admin press " + string + " button");
        //now the admin use the newClazz to modify
    }

    @When("the admin change the class status to {string}")
    public void the_admin_change_the_class_status_to(String classStatus) {
        newClazz.setClassStatus(ClassStatus.valueOf(classStatus));
    }

    @When("the admin submit the change")
    public void the_admin_submit_the_change() {
        Clazz clazz = adminClazzService.updateClassInfo(newClazz);
        Assert.assertEquals(ClassStatus.cancel, clazz.getClassStatus());
    }

    @Then("the system delete all the submission related")
    public void the_system_delete_all_the_submission_related() {
        List<Submission> allSubmission = professorService.getAllSubmission(newClazz.getClassId());
        Assert.assertEquals(0, allSubmission.size());
    }

    @Then("the system delete all the deliverable related")
    public void the_system_delete_all_the_deliverable_related() {
        List<Deliverable> allDeliverables = professorService.getAllDeliverables(newClazz.getClassId());
        Assert.assertEquals(0, allDeliverables.size());
    }

    @Then("the system update the class status to {string}")
    public void the_system_update_the_class_status_to(String string) {
        ArrayList<Clazz> classInfoByCourseId = adminClazzService.getClassInfoByCourseId(newClazz.getCourseId());
        Assert.assertEquals(ClassStatus.cancel, classInfoByCourseId.get(0).getClassStatus());

        //delete new added course
        adminCourseService.deleteACourse(newClazz.getCourseId());
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

    private Time formatString2Time(String inputTime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Time inputTimeFormatted = null;
        try {
            inputTimeFormatted = new Time(df.parse(inputTime).getTime());
            System.out.println(inputTimeFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputTimeFormatted;
    }

}
