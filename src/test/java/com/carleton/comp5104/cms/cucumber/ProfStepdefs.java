package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.entity.Submission;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProfStepdefs {

    private int newDeliverableId = -1;
    private int newSubmissionId = -1;
    private int count = 0;
    private List<Integer> deliverableIDs = new ArrayList<Integer>();

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    DeliverableRepository deliverableRepository;

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;


    @Given("A professor with id {int} is assigned to class {int}")
    public void a_professor_with_id_is_assigned_to_class(int prof_id, int class_id) {
        Optional<Clazz> curClazz = clazzRepository.findById(class_id);
        if (curClazz.isPresent()) {
            curClazz.get().setProfId(prof_id);
            clazzRepository.save(curClazz.get());
        }
    }
    @When("The professor submits a deliverable to class {int} with deadline {string}, description {string} and percentage {float}")
    public void the_professor_submits_a_deliverable_to_class_with_deadline_description_and_percentage(int class_id, String dead_line, String desc, float percent) {
        Deliverable newDeliverable = new Deliverable();
        newDeliverable.setClassId(class_id);
        newDeliverable.setDead_line(Timestamp.valueOf(dead_line));
        newDeliverable.setDesc(desc);
        newDeliverable.setPercent(percent);
        newDeliverable.setIsNotified(false);
        this.newDeliverableId = professorService.submitDeliverable(newDeliverable);
    }

    @Then("The corresponding new entry is created in the Deliverable table")
    public void the_corresponding_new_entry_is_created_in_the_Deliverable_table() {
        Assert.assertTrue(deliverableRepository.findById(newDeliverableId).isPresent());
    }

    @Then("No entry is created in the Deliverable table")
    public void no_entry_is_created_in_the_Deliverable_table() {
        Assert.assertTrue(deliverableRepository.findById(newDeliverableId).isEmpty());
    }


    @Given("The student with id {int} made a submission to that deliverable with file name {string}, description {string} at time {string}")
    public void the_student_with_id_made_a_submission_to_that_deliverable_with_file_name_description_at_time(int student_id, String file_name, String submission_desc, String submit_time) {
        Submission newSub = new Submission();
        newSub.setStudentId(student_id);
        newSub.setFileName(file_name);
        newSub.setDesc(submission_desc);
        newSub.setSubmitTime(Timestamp.valueOf(submit_time));
        newSub.setDeliverableId(this.newDeliverableId);
        newSub = submissionRepository.save(newSub);
        newSubmissionId = newSub.getSubmissionId();
    }

    @When("The professor submit the grade of {float} to that submission")
    public void the_professor_submit_the_grade_of_grade_to_that_submission(float grade) {
        Assert.assertEquals(professorService.submitDeliverableGrade(newSubmissionId, grade), 0);;
    }

    @Then("The grade column of that submission is modified to {float} in the Submission table")
    public void the_grade_column_of_that_submission_is_modified_to_grade_in_the_submission_table(float grade) {
        Optional<Submission> newSub = submissionRepository.findById(newSubmissionId);
        if (newSub.isPresent()) {
            newSub.get().setGrade(grade);
            submissionRepository.save(newSub.get());
            Assert.assertEquals(newSub.get().getGrade(), grade, 0.001);
        }
    }

    @When("The professor submit the grade of {float} to submission with id {int}")
    public void the_professor_submit_the_grade_of_to_submission_with_id(float grade, int submission_id) {
        newSubmissionId = professorService.submitDeliverableGrade(newSubmissionId, grade);
    }

    @Then("No changes were made to the Submission table as the submission id is invalid")
    public void no_changes_were_made_to_the_submission_table_as_the_submission_id_is_invalid() {
        Assert.assertEquals(newSubmissionId, -1);;
    }

    @Given("A student with id {int} is enrolled to class {int}")
    public void a_student_with_id_is_enrolled_to_class(int student_id, int class_id) {
        Enrollment newEnroll = new Enrollment();
        newEnroll.setClassId(class_id);
        newEnroll.setStudentId(student_id);
        newEnroll.setStatus(EnrollmentStatus.ongoing);
        newEnroll.setFinalGrade(0);
        enrollmentRepository.save(newEnroll);
    }

    @Transactional
    @Given("There are no deliverables in class {int}")
    public void there_are_no_deliverables_in_class(int class_id) {
        professorService.deleteAllDeliverable(class_id);
        deliverableIDs.clear();
        count = 0;
        Assert.assertTrue(deliverableRepository.findById(class_id).isEmpty());
    }

    @Given("The professor submits a deliverable to class {int} with deadline {string}, and percentage {float}")
    public void the_professor_submits_a_deliverable_to_class_with_deadline_and_percentage(int class_id, String dead_line, float percent) {
        Deliverable newDeliverable = new Deliverable();
        newDeliverable.setClassId(class_id);
        newDeliverable.setDead_line(Timestamp.valueOf(dead_line));
        newDeliverable.setPercent(percent);
        newDeliverable.setIsNotified(false);
        this.deliverableIDs.add(professorService.submitDeliverable(newDeliverable));
    }

    @Given("The student with id {int} has submitted for a deliverable at time {string} and has grades {float}")
    public void the_student_with_id_has_submitted_for_a_deliverable_at_time_and_has_grades(int student_id, String submit_time, float grade) {
        Submission newSub = new Submission();
        newSub.setStudentId(student_id);
        newSub.setSubmitTime(Timestamp.valueOf(submit_time));
        newSub.setDeliverableId(this.deliverableIDs.get(count));
        newSub.setGrade(grade);
        newSub = submissionRepository.save(newSub);
        newSubmissionId = newSub.getSubmissionId();
        count += 1;
    }

    @When("The professor submit the final grade of student {int} for class {int}")
    public void the_professor_submit_the_final_grade_for_student_for_class(int student_id, int class_id) {
        professorService.submitFinalGrade(class_id, student_id);
    }

    @Then("The final_grade column of enrollment of student {int} and class {int} is modified to {float}")
    public void the_final_grade_column_of_enrollment_of_student_and_class_is_modified_to(int student_id, int class_id, float grade) {
        Optional<Enrollment> curEnroll = enrollmentRepository.findByClassIdAndStudentId(class_id, student_id);
        Assert.assertEquals(grade, curEnroll.get().getFinalGrade(), 0.001);
    }

}
