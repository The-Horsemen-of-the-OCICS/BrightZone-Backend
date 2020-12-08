package com.carleton.comp5104.cms.cucumber;


import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UseCaseDependencyStepdefs {
    //id
    private final int S1 = 3000001;
    private final int S2 = 3000002;
    private final int S3 = 3000003;

    private final int P1 = 2000001;
    private final int P2 = 2000002;

    private final int C1 = 1014;
    private final int C2 = 1015;
    private final int C3 = 1016;

    private int project_deliverable_id = 0;
    private int essay_deliverable_id = 0;

    private int S1_essay_submission_id = 0;
    private int S2_essay_submission_id = 0;

    private int S2_project_submission_id = 0;
    private int S3_project_submission_id = 0;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Given("Admin creates a term and its deadlines")
    public void admin_creates_a_term_and_its_deadlines() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S1 requests creation Admin creates C2")
    public void s1_requests_creation_admin_creates_c2() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("P1 and P2 simultaneously request creation")
    public void p1_and_p2_simultaneously_request_creation() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("Admin creates C1 Admin creates C3")
    public void admin_creates_c1_admin_creates_c3() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S2 and S3 simultaneously request creation")
    public void s2_and_s3_simultaneously_request_creation() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("Admin assigns C1 to P1 assigns C3 to P2 assigns C2 to P1")
    public void admin_assigns_c1_to_p1_assigns_c3_to_p2_assigns_c2_to_p1() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S2 logins in, then S3, then S1, then P1, then P2")
    public void s2_logins_in_then_s3_then_s1_then_p1_then_p2() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S2 and S3 simultaneously register in C1")
    public void s2_and_s3_simultaneously_register_in_c1() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S1 registers in C2 S1 registers in C3 S2 registers in C3")
    public void s1_registers_in_c2_s1_registers_in_c3_s2_registers_in_c3() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("P1 creates deliverable Project for C1 P2 creates deliverable Essay for C3")
    public void p1_creates_deliverable_project_for_c1_p2_creates_deliverable_essay_for_c3() {
        //P1 creates deliverable Project
        Deliverable project = new Deliverable();
        project.setClassId(C1);
        project.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
        project.setDesc("This is a project");
        project.setPercent(0.7f);
        project.setIsNotified(false);
        this.project_deliverable_id = professorService.submitDeliverable(project);

        //P2 creates deliverable Essay for C3
        Deliverable essay = new Deliverable();
        essay.setClassId(C3);
        essay.setDead_line(Timestamp.valueOf("2020-12-25 10:10:10.0"));
        essay.setDesc("This is an Essay");
        essay.setPercent(0.3f);
        essay.setIsNotified(false);
        this.essay_deliverable_id = professorService.submitDeliverable(essay);
    }

    @Then("S1 drops C2")
    public void s1_drops_c2() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S2 and S3 simultaneously submit Project in C1")
    public void s2_and_s3_simultaneously_submit_project_in_c1() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("S1 submits Essay in C3, S2 submits Essay in C3")
    public void s1_submits_essay_in_c3_s2_submits_essay_in_c3() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("P1 submits marks for Project in C1")
    public void p1_submits_marks_for_project_in_c1() {
        //Grade submission from S2
        professorService.submitDeliverableGrade(S2_project_submission_id, 0.77f);
        float S2_grade = submissionRepository.findById(S2_project_submission_id).get().getGrade();
        Assert.assertEquals(0.77, S2_grade, 0.0001);

        //Grade submission from S3
        professorService.submitDeliverableGrade(S3_project_submission_id, 0.66f);
        float S3_grade = submissionRepository.findById(S3_project_submission_id).get().getGrade();
        Assert.assertEquals(0.66, S3_grade, 0.0001);
    }

    @Then("P2 submit marks for Essay in C3")
    public void p2_submit_marks_for_essay_in_c3() {
        //Grade submission from S1
        professorService.submitDeliverableGrade(S1_essay_submission_id, 0.77f);
        float S1_grade = submissionRepository.findById(S1_essay_submission_id).get().getGrade();
        Assert.assertEquals(0.77, S1_grade, 0.0001);

        //Grade submission from S2
        professorService.submitDeliverableGrade(S2_essay_submission_id, 0.66f);
        float S2_grade = submissionRepository.findById(S2_essay_submission_id).get().getGrade();
        Assert.assertEquals(0.66, S2_grade, 0.0001);
    }

    @Then("Simultaneously P1 and P2 submit final grades respectively for C1 and C3")
    public void simultaneously_p1_and_p2_submit_final_grades_respectively_for_c1_and_c3() {
        Thread P1_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Final grade for S2
                professorService.submitFinalGrade(C1, S2);
                float S2_finalGrade = enrollmentRepository.findByClassIdAndStudentId(C1, S2).get().getFinalGrade();
                Assert.assertEquals(0.77, S2_finalGrade, 0.0001);

                //Final grade for S3
                professorService.submitFinalGrade(C1, S3);
                float S3_finalGrade = enrollmentRepository.findByClassIdAndStudentId(C1, S3).get().getFinalGrade();
                Assert.assertEquals(0.66, S3_finalGrade, 0.0001);
            }
        });

        Thread P2_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Final grade for S1
                professorService.submitFinalGrade(C3, S1);
                float S1_finalGrade = enrollmentRepository.findByClassIdAndStudentId(C3, S1).get().getFinalGrade();
                Assert.assertEquals(0.77, S1_finalGrade, 0.0001);

                //Final grade for S2
                professorService.submitFinalGrade(C3, S2);
                float S2_finalGrade = enrollmentRepository.findByClassIdAndStudentId(C3, S2).get().getFinalGrade();
                Assert.assertEquals(0.66, S2_finalGrade, 0.0001);
            }
        });

        P1_thread.start();
        P2_thread.start();
    }

    @Then("S1, S2, S3, P1 and P2 log out")
    public void s1_s2_s3_p1_and_p2_log_out() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
