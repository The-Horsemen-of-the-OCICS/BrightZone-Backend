package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Optional;

@SpringBootTest
public class ProfStepdefs {

    private int newId = -1;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ClazzRepository clazzRepository;

    @Given("A professor with id {int} is assigned to class {int}")
    public void a_professor_with_id_is_assigned_to_class(int prof_id, int class_id) {
        System.out.println(clazzRepository.count());
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
        this.newId = professorService.submitDeliverable(newDeliverable);
    }

    @Then("The corresponding new entry is created in the Deliverable table")
    public void the_corresponding_new_entry_is_created_in_the_Deliverable_table() {
        Assert.assertTrue(professorService.getDeliverable(newId).isPresent());
    }

    @Then("No entry is created in the Deliverable table")
    public void no_entry_is_created_in_the_Deliverable_table() {
        Assert.assertTrue(professorService.getDeliverable(newId).isEmpty());
    }
}
