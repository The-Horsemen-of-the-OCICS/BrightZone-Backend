package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.entity.Submission;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProfessorServiceTest {

	@Autowired
	private com.carleton.comp5104.cms.service.impl.ProfessorService professorService;

	@Autowired
	private ClazzRepository clazzRepository;

	@Autowired
	DeliverableRepository deliverableRepository;

	@Autowired
	SubmissionRepository submissionRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Test
	public void contextLoads() throws Exception {
		assertThat(professorService).isNotNull();
	}

	@Test
	public void testSubmitDeliverable() {
		//Case 1: submit successfully
		Deliverable newDeliverable = new Deliverable();
		newDeliverable.setClassId(1069);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);

		int newId = professorService.submitDeliverable(newDeliverable);
		Optional<Deliverable> deliverableOptional = professorService.getDeliverable(newId);
		assertThat(deliverableOptional.isPresent());
		assertThat(deliverableOptional.get().getClassId() == 1069);
		assertThat(deliverableOptional.get().getDeadLine().equals(new Timestamp(0)));
		assertThat(deliverableOptional.get().getDesc() == "This is a test deliverable");
		assertThat(deliverableOptional.get().getPercent() == 0.35f);

		//Case 2: invalid class_id
		newDeliverable = new Deliverable();
		newDeliverable.setClassId(9999);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		newId = professorService.submitDeliverable(newDeliverable);
		assertThat(newId == -1);

		//Case 3: invalid dead_line
		newDeliverable = new Deliverable();
		newDeliverable.setClassId(1069);
		newDeliverable.setDead_line(Timestamp.valueOf("2000-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		newId = professorService.submitDeliverable(newDeliverable);
		assertThat(newId == -1);

	}

	@Test
	public void testDeleteDeliverable() {
		//case 1 delete successfully
		Deliverable newDeliverable = new Deliverable();
		newDeliverable.setClassId(1069);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		int newId = professorService.submitDeliverable(newDeliverable);
		int deleteResult = professorService.deleteDeliverable(newId);
		assertThat(deleteResult == -1);

		//case 2 invalid deliverable id, no change
		deleteResult = professorService.deleteDeliverable(-5645457);
		assertThat(deleteResult == -1);

	}

	@Test
	public void testSubmitDeliverableGrade() {
		//case 1: submit successfully:
		Deliverable newDeliverable = new Deliverable();
		newDeliverable.setClassId(1069);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		int newDeliverableId = professorService.submitDeliverable(newDeliverable);

		Submission newSub = new Submission();
		newSub.setFileName("test_submission");
		newSub.setStudentId(3000002);
		newSub.setDeliverableId(newDeliverableId);
		newSub.setSubmitTime(Timestamp.valueOf("2020-11-24 10:10:10.0"));
		newSub.setSubmissionDesc("My submission");
		newSub = submissionRepository.save(newSub);

		professorService.submitDeliverableGrade(newSub.getSubmissionId(), 0.77f);
		float newGrade = submissionRepository.findById(newSub.getSubmissionId()).get().getGrade();
		Assert.assertEquals(0.77, newGrade, 0.0001);

		//case 2: invalid submission_id, no changes made
		int result = professorService.submitDeliverableGrade(-58787, 0.77f);
		Assert.assertEquals(-1, result);
	}

	@Test
	public void testSubmitFinalGrade() {
		// case 1: Submit successfully
		professorService.deleteAllDeliverable(1069);

		Deliverable newD1 = new Deliverable();
		Deliverable newD2 = new Deliverable();
		Deliverable newD3 = new Deliverable();
		newD1.setDead_line(Timestamp.valueOf("2021-01-01 10:10:10"));
		newD1.setClassId(1069);
		newD1.setPercent(0.34f);
		newD2.setDead_line(Timestamp.valueOf("2021-08-23 10:00:00"));
		newD2.setClassId(1069);
		newD2.setPercent(0.26f);
		newD3.setDead_line(Timestamp.valueOf("2021-09-12 10:00:00"));
		newD3.setClassId(1069);
		newD3.setPercent(0.4f);
		int D1ID = professorService.submitDeliverable(newD1);
		int D2ID = professorService.submitDeliverable(newD2);
		int D3ID = professorService.submitDeliverable(newD3);

		Enrollment newEnroll = new Enrollment();
		newEnroll.setStudentId(3000002);
		newEnroll.setStatus(EnrollmentStatus.ongoing);
		newEnroll.setClassId(1069);
		enrollmentRepository.save(newEnroll);

		Submission newS1 = new Submission();
		Submission newS2 = new Submission();
		Submission newS3 = new Submission();
		newS1.setSubmitTime(Timestamp.valueOf("2020-12-30 10:10:10"));
		newS1.setGrade(0.74f);
		newS1.setStudentId(3000002);
		newS1.setDeliverableId(D1ID);
		newS2.setSubmitTime(Timestamp.valueOf("2021-08-20 10:00:00"));
		newS2.setGrade(0.88f);
		newS2.setStudentId(3000002);
		newS2.setDeliverableId(D2ID);
		newS3.setSubmitTime(Timestamp.valueOf("2021-09-10 10:00:00"));
		newS3.setGrade(0.62f);
		newS3.setStudentId(3000002);
		newS3.setDeliverableId(D3ID);
		submissionRepository.save(newS1);
		submissionRepository.save(newS2);
		submissionRepository.save(newS3);

		professorService.submitFinalGrade(1069, 3000002);
		float finalGrade = enrollmentRepository.findByClassIdAndStudentId(1069, 3000002).get().getFinalGrade();

		Assert.assertEquals(0.7284, finalGrade, 0.0001);

		// case 2: invalid class_id, no changes made
		int result = professorService.submitFinalGrade(-457, 3000002);
		Assert.assertEquals(-1, result);

		// case 3: invalid student_id, no changes made
		result = professorService.submitFinalGrade(1069, -77574);
		Assert.assertEquals(-1, result);
	}

}
