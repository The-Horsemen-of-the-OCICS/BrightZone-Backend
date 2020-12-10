package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.service.AdminRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/request")
public class AdminRequestController {

    @Autowired
    private AdminRequestService adminRequestService;

    @DeleteMapping("/deleteRequestByUserId")
    public boolean deleteRequestByUserId(@RequestParam("userId") Integer userId) {
        return adminRequestService.deleteAllByUserId(userId);
    }

    @PutMapping("/updateRequest")
    public boolean updateRequestStatus(@RequestParam("id") Integer requestId,
                                       @RequestParam("status") String newStatus) {
        return adminRequestService.updateRequestStatus(requestId, newStatus);
    }

    @GetMapping("/getAllOpenRequest")
    public List<Request> getAllOpenRequest() {
        return adminRequestService.getAllOpenRequest();
    }

}
