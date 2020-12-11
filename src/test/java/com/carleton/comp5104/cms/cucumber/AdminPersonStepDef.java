package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.service.PersonService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminPersonStepDef {

    private int personId;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Given("the admin input personId {int} in the search box")
    public void the_admin_input_person_id_in_the_search_box(int personId) {
        this.personId = personId;
    }

    @When("Person with personId {int} exist")
    public void person_with_person_id_exist(int personId) {
        Assert.assertTrue(personService.isExist(this.personId));
    }

    @Then("the admin will find this person")
    public void the_admin_will_find_this_person() {
        Assert.assertNotNull(personService.findById(this.personId));
    }

}
