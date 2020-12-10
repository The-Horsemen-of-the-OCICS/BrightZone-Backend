package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.RequestStatus;
import com.carleton.comp5104.cms.enums.RequestType;
import com.carleton.comp5104.cms.repository.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AdminRequestServiceTest {

    @Autowired
    private AdminRequestService adminRequestService;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    void updateRequestStatusTest() {
        requestRepository.deleteAll();

        // scenario 1: no matching Request object with requestId
        int requestId = 1;
        String newStatus = "fulfilled";
        Assertions.assertTrue(requestRepository.findById(requestId).isEmpty());
        boolean success = adminRequestService.updateRequestStatus(1, newStatus);
        Assertions.assertFalse(success);

        // scenario 2: update request status success
        Request request = new Request();
        request.setUserId(3000000);
        request.setType(RequestType.enroll);
        request.setStatus(RequestStatus.open);
        request.setMessage("Hello, please help me enroll COMP5104");
        Request save = requestRepository.save(request);
        requestId = save.getRequestId();
        newStatus = "fulfilled";
        Assertions.assertTrue(adminRequestService.updateRequestStatus(requestId, newStatus));
        // delete newly added request
        requestRepository.deleteAll();
    }

    @Test
    void deleteAllByUserIdTest() {
        Request request = new Request();
        request.setUserId(3000000);
        request.setType(RequestType.enroll);
        request.setStatus(RequestStatus.open);
        request.setMessage("Hello, please help me enroll COMP5104");
        requestRepository.save(request);
        Assertions.assertTrue(requestRepository.findAll().size() >= 1);

        int userId = 3000000;
        boolean success = adminRequestService.deleteAllByUserId(userId);
        Assertions.assertTrue(success);
        Assertions.assertEquals(0, requestRepository.findAll().size());
    }

    @Test
    void getAllOpenRequestTest() {
        Request request = new Request();
        request.setUserId(3000000);
        request.setType(RequestType.enroll);
        request.setStatus(RequestStatus.open);
        request.setMessage("Hello, please help me enroll COMP5104");
        Request save = requestRepository.save(request);

        Assertions.assertTrue(adminRequestService.getAllOpenRequest().size() >= 1);
        // delete newly added request
        requestRepository.deleteById(save.getRequestId());
    }

}
