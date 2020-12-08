package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.*;
import com.carleton.comp5104.cms.enums.ClassStatus;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.*;
import com.carleton.comp5104.cms.service.CourseService;
import com.carleton.comp5104.cms.service.DeliverableService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class StudentStepdefs {

    private int studentId;
    private List<Clazz> clazzSet;
    private int clazzId;
    private int courseId;
    private Map<Integer, List<Integer>> courseClazzMap;
    private int deliverableId;

    private boolean isSubmit;
    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private PreclusionRepository preclusionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DeliverableService deliverableService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Given("A student with id {int} check all opened courses")
    public void getOpenedCourses(int studentId) {
        this.studentId = studentId;
        this.clazzSet = clazzRepository.findAllByClassStatus(ClassStatus.open);
        courseClazzMap = new HashMap<>();
        this.clazzSet.forEach(clazz -> {
            int courseId = clazz.getCourseId();
            if (null == courseClazzMap.get(courseId)) {
                List<Integer> clazzList = new ArrayList<>();
                clazzList.add(clazz.getClassId());
                courseClazzMap.put(courseId, clazzList);
            } else {
                courseClazzMap.get(courseId).add(clazz.getClassId());
            }
        });
    }

    @Given("The student choose a class {int} of a course")
    public void chooseCourse(int classId) {
        this.clazzId = classId;
        for (Clazz clazz : clazzSet) {
            if (clazz.getClassId() == classId) {
                this.courseId = clazz.getCourseId();
            }
        }
    }

    @Given("Student A with id {int} register a class {int} of a course with limit of {int}")
    public void chooseCourseWithLimit(int studentId, int clazzId, int limit) {
        Optional<Clazz> byId = clazzRepository.findById(clazzId);
        byId.ifPresent(clazz -> {
            clazz.setEnrollCapacity(limit);
            clazz.setEnrolled(0);
            clazzRepository.save(clazz);
            this.clazzId = clazz.getClassId();

            courseService.dropAllCourseByClazz(clazzId);
        });
        courseService.registerCourse(studentId, clazzId);
    }

    @Given("Student B with id {int} and Student B with id {int} choose the class of the course")
    public void chooseCourseWithLimit(int studentIdB, int studentIdC) {
        Thread t1 = new Thread(() -> {
            courseService.registerCourse(studentIdB, clazzId);
        });
        Thread t2 = new Thread(() -> {
            courseService.registerCourse(studentIdC, clazzId);
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("Student A with id {int} drop the class of the course")
    public void Drop(int studentId) {
        courseService.dropCourse(studentId, clazzId);
    }

    @Then("the enrolled student of the course is equal to {int}")
    public void checkEnrolled(int limit) {
        Optional<Clazz> byId = clazzRepository.findById(clazzId);
        byId.ifPresent(clazz -> {
            Assert.assertEquals(limit, clazz.getEnrolled());
        });
    }

    @Then("B {int} and C {int} only one can register success")
    public void checkOnlyOne(int studentIdB, int studentIdC) {
        List<Enrollment> enrollmentsB = enrollmentRepository.findByClassIdAndStudentIdAndStatus(clazzId, studentIdB, EnrollmentStatus.ongoing);
        List<Enrollment> enrollmentsC = enrollmentRepository.findByClassIdAndStudentIdAndStatus(clazzId, studentIdC, EnrollmentStatus.ongoing);
        int sizeB = enrollmentsB == null ? 0 : enrollmentsB.size();
        int sizeC = enrollmentsC == null ? 0 : enrollmentsC.size();
        Assert.assertEquals(1, sizeB + sizeC);

    }

    @Then("Both B {int} and C {int} register success")
    public void checkBoth(int studentIdB, int studentIdC) {
        List<Enrollment> enrollmentsB = enrollmentRepository.findByClassIdAndStudentIdAndStatus(clazzId, studentIdB, EnrollmentStatus.ongoing);
        List<Enrollment> enrollmentsC = enrollmentRepository.findByClassIdAndStudentIdAndStatus(clazzId, studentIdC, EnrollmentStatus.ongoing);
        int sizeB = enrollmentsB == null ? 0 : enrollmentsB.size();
        int sizeC = enrollmentsC == null ? 0 : enrollmentsC.size();
        Assert.assertEquals(2, sizeB + sizeC);
    }

    @When("The class has remaining space")
    public void setRemainingSpace() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setEnrollCapacity(50);
            c.setEnrolled(49);
            clazzRepository.save(c);
        });

    }

    @When("The class has no remaining space")
    public void setNoRemainingSpace() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setEnrollCapacity(50);
            c.setEnrolled(50);
            clazzRepository.save(c);
        });
    }

    @When("The student finished all pre-requisite courses")
    public void setFinishedPreReqCourses() {
        Prerequisite prerequisite = new Prerequisite();
        prerequisite.setCourseId(courseId);
        prerequisite.setPrerequisiteId(3845);
        prerequisiteRepository.save(prerequisite);

        Set<Prerequisite> prerequisiteSet = prerequisiteRepository.findByCourseId(courseId);
        prerequisiteSet.forEach(p -> {
            int preCourse = p.getPrerequisiteId();
            List<Integer> clazzes = courseClazzMap.get(preCourse);
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setClassId(clazzes.get(0));
            enrollment.setStatus(EnrollmentStatus.passed);
            enrollmentRepository.save(enrollment);
        });
    }

    @When("The student has not finished all pre-requisite courses")
    public void setNotFinishedPreReqCourses() {
        Prerequisite prerequisite = new Prerequisite();
        prerequisite.setCourseId(courseId);
        prerequisite.setPrerequisiteId(3845);
        prerequisiteRepository.save(prerequisite);

        Set<Prerequisite> prerequisiteSet = prerequisiteRepository.findByCourseId(courseId);
        prerequisiteSet.forEach(p -> {
            int preCourse = p.getPrerequisiteId();
            List<Integer> clazzes = courseClazzMap.get(preCourse);
            clazzes.forEach(clazz -> {
                Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazz, studentId);
                enrollment.ifPresent(e -> enrollmentRepository.delete(e));
            });

        });
    }

    @When("The student did not take preclusion courses")
    public void setNotFinishedPreCluCourses() {
        Preclusion preclusion = new Preclusion();
        preclusion.setCourseId(courseId);
        preclusion.setPreclusionId(3844);
        preclusionRepository.save(preclusion);

        Set<Preclusion> preclusionSet = preclusionRepository.findByCourseId(courseId);
        preclusionSet.forEach(p -> {
            int preCourse = p.getPreclusionId();
            List<Integer> clazzes = courseClazzMap.get(preCourse);
            clazzes.forEach(clazz -> {
                Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazz, studentId);
                enrollment.ifPresent(e -> enrollmentRepository.delete(e));
            });
        });
    }

    @When("The student took one of the preclusion courses")
    public void setFinishedPreCluCourses() {
        Preclusion preclusion = new Preclusion();
        preclusion.setCourseId(courseId);
        preclusion.setPreclusionId(3844);
        preclusionRepository.save(preclusion);

        Set<Preclusion> preclusionSet = preclusionRepository.findByCourseId(courseId);
        preclusionSet.forEach(p -> {
            int preCourse = p.getPreclusionId();
            List<Integer> clazzes = courseClazzMap.get(preCourse);
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setClassId(clazzes.get(0));
            enrollment.setStatus(EnrollmentStatus.passed);
            enrollmentRepository.save(enrollment);
        });
    }

    @When("The student click register")
    public void register() {
        courseService.registerCourse(studentId, clazzId);
    }

    @Then("student register success")
    public void registerSuccess() {
        Assert.assertTrue(enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId).isPresent());
    }

    @Then("student register failed")
    public void registerFail() {
        Assert.assertFalse(enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId).isPresent());
    }

    @Given("A student with id {int} check all registered courses")
    public void getRegisteredCourses(int studentId) {
        this.studentId = studentId;
    }

    @Given("The student choose a course {int} to drop")
    public void chooseDropCourse(int clazzId) {
        this.clazzId = clazzId;
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setClassId(clazzId);
        enrollment.setStatus(EnrollmentStatus.ongoing);
        enrollmentRepository.save(enrollment);
    }

    @When("It is before the course deadline")
    public void setBeforeDeadline() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setDropNoPenaltyDeadline(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60));
            clazzRepository.save(c);
        });
    }

    @When("It is after the course deadline")
    public void setAfterDeadline() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setDropNoPenaltyDeadline(new Timestamp(System.currentTimeMillis() - 24 * 60 * 60));
            clazzRepository.save(c);
        });
    }

    @When("It is before the DR deadline")
    public void setBeforeDRDeadline() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setDropNoFailDeadline(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60));
            clazzRepository.save(c);
        });
    }

    @When("It is after the DR deadline")
    public void setAfterDRDeadline() {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        clazz.ifPresent(c -> {
            c.setDropNoFailDeadline(new Timestamp(System.currentTimeMillis() - 24 * 60 * 60));
            clazzRepository.save(c);
        });
    }

    @When("The student click drop")
    public void drop() {
        courseService.dropCourse(studentId, clazzId);
    }

    @Then("student drop success no DR")
    public void dropSuccess() {
        Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId);
        enrollment.ifPresent(e -> {
            Assert.assertSame(EnrollmentStatus.dropped, enrollment.get().getStatus());
        });
    }

    @Then("student drop success with DR")
    public void dropSuccessWithDr() {
        Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId);
        enrollment.ifPresent(e -> {
            Assert.assertSame(EnrollmentStatus.dropped_dr, enrollment.get().getStatus());
        });
    }

    @Then("student drop failed")
    public void dropFail() {
        Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId);
        enrollment.ifPresent(e -> {
            Assert.assertSame(EnrollmentStatus.ongoing, enrollment.get().getStatus());
        });
    }

    @Given("A student {int} check all deliverable sections for the course {int}")
    public void chooseDeliverable(int studentId, int clazzId) {
        this.studentId = studentId;
        this.clazzId = clazzId;
    }

    @Given("The Student choose a section {int} and file to submit")
    public void chooseDeliverable(int deliverableId) {
        this.deliverableId = deliverableId;
    }

    @When("It is before the deadline")
    public void beforeDeadline() {
        Optional<Deliverable> deliverable = deliverableRepository.findById(deliverableId);
        deliverable.ifPresent(c -> {
            c.setDeadLine(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60));
            deliverableRepository.save(c);
        });
    }

    @When("It is after the deadline")
    public void afterDeadline() {
        Optional<Deliverable> deliverable = deliverableRepository.findById(deliverableId);
        deliverable.ifPresent(c -> {
            c.setDeadLine(new Timestamp(System.currentTimeMillis() - 24 * 60 * 60));
            deliverableRepository.save(c);
        });
    }

    @When("The student click submit")
    public void submit() throws IOException {
        deliverableService.deleteAssignment(deliverableId, studentId);
        isSubmit = deliverableService.submitDeliverable(studentId, deliverableId, new MockMultipartFile("file",
                "myAssignment.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()), "test");
    }

    @Then("The student submit success")
    public void submitSuccess() {
        Assert.assertTrue(submissionRepository.findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(deliverableId, studentId).size() > 0);
    }

    @Then("The student submit failed")
    public void submitFail() {
        Assert.assertFalse(submissionRepository.findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(deliverableId, studentId).size() == 0);
    }
}
