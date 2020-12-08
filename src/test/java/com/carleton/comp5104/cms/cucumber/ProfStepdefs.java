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
import com.carleton.comp5104.cms.service.DeliverableService;
import com.carleton.comp5104.cms.service.impl.ProfessorService;

import com.carleton.comp5104.cms.util.FileUtil;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProfStepdefs {

    private int newDeliverableId = -1;
    private int newSubmissionId = -1;
    private int count = 0;
    private List<Integer> deliverableIDs = new ArrayList<Integer>();
    MockHttpServletResponse mockResponse;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    DeliverableService deliverableService;

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
        newSub.setSubmissionDesc(submission_desc);
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

    @When("The professor delete this new created deliverable")
    public void the_professor_delete_this_new_created_deliverable() {
        professorService.deleteDeliverable(this.deliverableIDs.get(count - 1));
    }

    @Then("The deliverable is deleted")
    public void the_deliverable_is_deleted() {
        Optional<Deliverable> curDeliverable= professorService.getDeliverable(this.deliverableIDs.get(count - 1));
        Assert.assertTrue(curDeliverable.isEmpty());
    }

    @Then("All submissions related to the deliverable are deleted")
    public void all_submissions_related_to_the_deliverable_are_deleted() {
        List<Submission> submissions = submissionRepository.findByDeliverableIdOrderBySubmitTimeDesc(this.deliverableIDs.get(count - 1));
        Assert.assertTrue(submissions.isEmpty());
    }

    @When("The professor delete deliverable with id {int}")
    public void the_professor_delete_deliverable_with_id(int deliverable_id) {
        this.newDeliverableId = professorService.deleteDeliverable(deliverable_id);
    }

    @Then("No entry is deleted in the Deliverable table")
    public void no_entry_is_deleted_in_the_deliverable_table() {
        Assert.assertEquals(-1,this.newDeliverableId);
    }

    @Then("Then nothing changes in the database for student {int} and class {int}")
    public void then_nothing_changes_in_the_database_for_student_and_class(int student_id, int class_id) {
        Assert.assertTrue(enrollmentRepository.findByClassIdAndStudentId(class_id, student_id).isEmpty());
    }

    @Given("The student with id {int} made a submission to deliverable {int} with file name {string}, description {string} at time {string}")
    public void the_student_with_id_made_a_submission_to_deliverable_with_file_name_description_at_time(Integer student_id, Integer deliverable_id, String file_name, String submission_desc, String submit_time) {
        Submission newSub = new Submission();
        newSub.setStudentId(student_id);
        newSub.setFileName(file_name);
        newSub.setSubmissionDesc(submission_desc);
        newSub.setSubmitTime(Timestamp.valueOf(submit_time));
        newSub.setDeliverableId(deliverable_id);
        newSub = submissionRepository.save(newSub);
        newSubmissionId = newSub.getSubmissionId();
    }

    @Then("No changes were made to the Submission table as the deliverable id is invalid")
    public void no_changes_were_made_to_the_submission_table_as_the_deliverable_id_is_invalid() {
        Assert.assertEquals(newSubmissionId, -1);
    }

    @Given("There are no class materials in class {int}")
    public void there_are_no_class_materials_in_class(Integer class_id) {
        //Delete everything in class <class_id>
        String absolutePath = FileUtil.getRootPath() +"/" + class_id + "/course_materials";
        File dir = new File(absolutePath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File childDir : directoryListing) {
                File[] fileListing = childDir.listFiles();
                if (fileListing != null) {
                    for (File childFile : fileListing) {
                        childFile.delete();
                    }
                }
                childDir.delete();
            }
        }
    }

    @When("The professor upload a file with filename {string} under directory {string} to class {int}")
    public void the_professor_upload_a_file_with_filename_under_directory_to_class(String file_name, String dir_name, Integer class_id) {

        MultipartFile file
                = new MockMultipartFile(
                "file",
                file_name,
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Assert.assertEquals(0,professorService.uploadClassMaterial(class_id, dir_name, file));
    }

    @Then("The file {string} for class {int} is uploaded to server")
    public void the_file_for_class_is_uploaded_to_server(String file_name, int class_Id) {
        List<List<String>> result = professorService.getClassMaterialNames(class_Id);
        Assert.assertEquals(file_name, result.get(1).get(0));
    }

    @When("The professor download the file with filename {string} under directory {string} from class {int}")
    public void the_professor_download_the_file_with_filename_under_directory_from_class(String file_name, String dir, int class_id) {
        this.mockResponse = new MockHttpServletResponse();
        professorService.getClassMaterial(class_id,dir,file_name, this.mockResponse);
    }

    @Then("The file is downloaded")
    public void the_file_for_class_is_downloaded() throws UnsupportedEncodingException {
        assertThat(this.mockResponse.getContentAsString().equals("Hello, World!"));
    }

    @Given("The student with id {int} submitted file {string}, description {string} to that deliverable")
    public void the_student_with_id_submitted_file_description_at_time_to_that_deliverable(int student_id, String file_name, String desc) throws IOException {
        MultipartFile file
                = new MockMultipartFile(
                "file",
                file_name,
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        deliverableService.submitDeliverable(student_id, this.newDeliverableId, file, desc);
    }

    @When("the professor download the file submitted by the student {int} for that deliverable of class {int}")
    public void the_professor_download_the_file_submitted_by_the_student_for_that_deliverable(int student_id, int class_id) {
        //get the submission back;
        List<Submission> newSubs = submissionRepository.findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(this.newDeliverableId, student_id);
        Submission curSub = newSubs.get(0);
        //get that submission
        this.mockResponse = new MockHttpServletResponse();
        professorService.getGetSubmissionFile(class_id, this.newDeliverableId, student_id, Long.toString(curSub.getSubmitTime().getTime()), curSub.getFileName(), this.mockResponse);
    }

    @When("The professor delete the file with filename {string} under directory {string} from class {int}")
    public void the_professor_delete_the_file_with_filename_under_directory_from_class(String file_name, String dir, int class_id) {
        Assert.assertEquals(0,professorService.deleteClassMaterial(class_id, dir, file_name));
    }

    @Then("The file file with filename {string} under directory {string} from class {int} is deleted")
    public void the_file_file_with_filename_under_directory_from_class_is_deleted(String file_name, String dir, int class_id) {
        File newFile = new File(FileUtil.getRootPath() + "/" + class_id + "/course_materials/" + dir + "/" + file_name);
        Assert.assertTrue(!newFile.exists());
    }
}
