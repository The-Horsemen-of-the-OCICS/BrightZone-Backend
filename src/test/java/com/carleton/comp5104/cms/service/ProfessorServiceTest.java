package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.*;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import com.carleton.comp5104.cms.util.FileUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProfessorServiceTest {

	@Autowired
	private ProfessorService professorService;

	@Autowired
	private DeliverableService deliverableService;

	@Autowired
	DeliverableRepository deliverableRepository;

	@Autowired
	SubmissionRepository submissionRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Test
	public void contextLoads()  {
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

	@Test
	public void testGetAllClass() {
		List<Clazz> mClass = professorService.getAllClass(2000014);
		assertThat(mClass.size() == 1);
	}

	@Test
	public void testGetAllDeliverables() {
		professorService.deleteAllDeliverable(1000);
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
		List<Deliverable> mDeliverables = professorService.getAllDeliverables(1000);
		assertThat(mDeliverables.size() == 3);
	}

	@Test
	public void testGetAllEnrollment() {
		Enrollment newEnroll = new Enrollment();
		newEnroll.setStudentId(3000001);
		newEnroll.setStatus(EnrollmentStatus.ongoing);
		newEnroll.setClassId(1000);
		enrollmentRepository.save(newEnroll);

		Enrollment newEnroll2 = new Enrollment();
		newEnroll2.setStudentId(3000002);
		newEnroll2.setStatus(EnrollmentStatus.ongoing);
		newEnroll2.setClassId(1000);
		enrollmentRepository.save(newEnroll2);


		List<Enrollment> mEnrollments = professorService.getAllEnrollment(1000);
		assertThat(mEnrollments.size() == 2);
	}

	@Test
	public void testGetAllStudent() {
		Enrollment newEnroll = new Enrollment();
		newEnroll.setStudentId(3000001);
		newEnroll.setStatus(EnrollmentStatus.ongoing);
		newEnroll.setClassId(1000);
		enrollmentRepository.save(newEnroll);

		Enrollment newEnroll2 = new Enrollment();
		newEnroll2.setStudentId(3000002);
		newEnroll2.setStatus(EnrollmentStatus.ongoing);
		newEnroll2.setClassId(1000);
		enrollmentRepository.save(newEnroll2);

		List<Person> mStudents = professorService.getAllStudent(1000);
		assertThat(mStudents.size() == 2);
	}

	@Test
	public void testGetAllSubmission() {
		List<Submission> mSubmission = professorService.getAllSubmission(1000);
		assertThat(mSubmission.size() == 0);
	}

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void testUploadClassMaterial() {
		//Successfully upload a txt file
		MultipartFile file
				= new MockMultipartFile(
				"file",
				"testFile.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		assertThat(professorService.uploadClassMaterial(1000, "testDirectory", file) == 0);
	}

	@Test
	public void testGetClassMaterialNames() {
		//Delete everything in class 1000
		String absolutePath = FileUtil.getRootPath() + "/1000/course_materials";
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

		//Upload two files
		MultipartFile file1
				= new MockMultipartFile(
				"file",
				"testFile1.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);

		MultipartFile file2
				= new MockMultipartFile(
				"file",
				"testFile2.png",
				MediaType.IMAGE_PNG_VALUE,
				"Hello, World!".getBytes()
		);
		assertThat(professorService.uploadClassMaterial(1000, "dir1", file1) == 0);
		assertThat(professorService.uploadClassMaterial(1000, "dir2", file2) == 0);

		//get filenames successfully;
		List<List<String>> result = professorService.getClassMaterialNames(1000);
		assertThat(result.get(0).get(0).equals("dir1"));
		assertThat(result.get(0).get(0).equals("dir2"));
		assertThat(result.get(1).get(0).equals("testFile1.txt"));
		assertThat(result.get(2).get(0).equals("testFile2.png"));
	}

	@Test
	public void testGetClassMaterial() throws UnsupportedEncodingException {

		//upload a new class material
		MultipartFile file
				= new MockMultipartFile(
				"file",
				"testFile.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		assertThat(professorService.uploadClassMaterial(1000, "testDirectory", file) == 0);

		//get that class material
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		professorService.getClassMaterial(1000,"testDirectory","testFile.txt", mockResponse);

		assertThat(mockResponse.getContentAsString().equals("Hello, World!"));
	}

	@Test
	public void testDeleteClassMaterial() {
		//upload a new class material
		MultipartFile file
				= new MockMultipartFile(
				"file",
				"deleteThis.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		assertThat(professorService.uploadClassMaterial(1000, "testDirectory", file) == 0);

		//delete that class material
		assertThat(professorService.deleteClassMaterial(1000,"testDirectory", "deleteThis.txt") == 0);

		File newFile = new File(FileUtil.getRootPath() + "/1000/course_materials/testDirectory/deleteThis.txt");
		assertThat(!newFile.exists());
	}

	@Test
	public void testGetSubmissionFile() throws IOException {
		Deliverable newDeliverable = new Deliverable();
		newDeliverable.setClassId(1000);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		int newId = professorService.submitDeliverable(newDeliverable);

		//upload a new submission
		MultipartFile file
				= new MockMultipartFile(
				"file",
				"testFile.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		assertThat(deliverableService.submitDeliverable(3000001, newId, file, "TestSubmission"));

		//get the submission back;
		List<Submission> newSubs = submissionRepository.findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(newId, 3000001);
		Submission curSub = newSubs.get(0);
		//get that submission
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		professorService.getGetSubmissionFile(1000, newId, 3000001, Long.toString(curSub.getSubmitTime().getTime()), curSub.getFileName(), mockResponse);

		assertThat(mockResponse.getContentAsString().equals("Hello, World!"));
	}

}
