package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Request;

import java.util.List;

public interface AdminRequestService {
    boolean updateRequestStatus(int requestId, String newStatus);

    boolean deleteAllByUserId(int userId);

    List<Request> getAllOpenRequest();
}
