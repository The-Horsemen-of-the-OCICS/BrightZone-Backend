package com.carleton.comp5104.cms.controller.prof;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProfessorTasksTests {

	@Autowired
	private ProfessorService professorService;

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
	public void testSubmitDeliverableGrade() {
		//Create a new Deliverable
		Deliverable newDeliverable = new Deliverable();
		newDeliverable.setClassId(1069);
		newDeliverable.setDead_line(Timestamp.valueOf("2020-12-24 10:10:10.0"));
		newDeliverable.setDesc("This is a test deliverable");
		newDeliverable.setPercent(0.35f);
		int newId = professorService.submitDeliverable(newDeliverable);

		//Create a new Submission

		//Submit grade for that submission

		//Read the grade of that submission
		assertThat(true);
	}

	@Test
	public void testSubmitFinalGrade() {
		//remove all previous deliverables defined for class 1069
		professorService.deleteAllDeliverable(1069);

		//Create some new Deliverable
		Deliverable newD1 = new Deliverable();
		Deliverable newD2 = new Deliverable();
		Deliverable newD3 = new Deliverable();

		//Create a new Enrollment

		//Create corresponding Submissions

		//Calculate and submit final grade

		assertThat(true);
	}

}
