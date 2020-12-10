package com.carleton.comp5104.cms.cucumber;

import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.RequestStatus;
import com.carleton.comp5104.cms.enums.RequestType;
import com.carleton.comp5104.cms.repository.RequestRepository;
import com.carleton.comp5104.cms.service.AdminRequestService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminRequestStepDef {

    private int requestId;
    private String newStatus;
    private boolean success;
    private int userId;
    private String status;
    private List<Request> requests;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private AdminRequestService adminRequestService;

    @Given("the admin select request with request id {int}")
    public void the_admin_select_request_with_request_id(int requestId) {
        this.requestId = requestId;
    }

    @Given("modify request status to {string}")
    public void modify_request_status_to(String newStatus) {
        this.newStatus = newStatus;
    }

    @Given("Request with request id {int} doesn't exist")
    public void request_with_request_id_doesn_t_exist(int requestId) {
        requestRepository.deleteAll();
        Assert.assertTrue(requestRepository.findById(requestId).isEmpty());
    }

    @When("the admin hit update request message button")
    public void the_admin_hit_update_request_message_button() {
        this.success = adminRequestService.updateRequestStatus(this.requestId, this.newStatus);
    }

    @Then("Update request message fail")
    public void update_request_message_fail() {
        Assert.assertFalse(this.success);
    }

    @Given("Request with request id {int} exists")
    public void request_with_request_id_exists(int requestId) {
        Request request = new Request();
        request.setUserId(3000000);
        request.setType(RequestType.enroll);
        request.setStatus(RequestStatus.open);
        request.setMessage("Hello, please help me enroll COMP5104");
        Request save = requestRepository.save(request);
        this.requestId = save.getRequestId();
        Assert.assertTrue(requestRepository.findById(this.requestId).isPresent());
    }

    @Then("Update request message success")
    public void update_request_message_success() {
        Assert.assertTrue(this.success);
        // add newly add requests
        requestRepository.deleteAll();
    }

    @Given("the admin select request with user id {int}")
    public void the_admin_select_request_with_user_id(int userId) {
        this.userId = userId;
    }

    @When("the admin hit delete request message button")
    public void the_admin_hit_delete_request_message_button() {
        this.success = adminRequestService.deleteAllByUserId(this.userId);
    }

    @Then("Delete request message success")
    public void delete_request_message_success() {
        Assert.assertTrue(this.success);
    }

    @Given("the admin select request with status open")
    public void the_admin_select_request_with_status_open() {
        this.status = "open";
    }

    @When("the admin hit search request message button")
    public void the_admin_hit_search_request_message_button() {
        Request request = new Request();
        request.setUserId(3000000);
        request.setType(RequestType.enroll);
        request.setStatus(RequestStatus.open);
        request.setMessage("Hello, please help me enroll COMP5104");
        Request save = requestRepository.save(request);
        this.requests = adminRequestService.getAllOpenRequest();
    }

    @Then("the admin get all open status")
    public void the_admin_get_all_open_status() {
        Assert.assertTrue(this.requests.size() >= 1);
        requestRepository.deleteAll();
    }


}
