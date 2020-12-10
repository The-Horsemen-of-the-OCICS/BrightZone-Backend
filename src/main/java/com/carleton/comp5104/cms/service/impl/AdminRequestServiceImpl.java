package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.RequestStatus;
import com.carleton.comp5104.cms.repository.RequestRepository;
import com.carleton.comp5104.cms.service.AdminRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminRequestServiceImpl implements AdminRequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public boolean updateRequestStatus(int requestId, String newStatus) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            request.setStatus(RequestStatus.valueOf(newStatus));
            requestRepository.save(request);
            return true;
        }
        return false;  // cannot find Request object with requestId
    }

    @Override
    @Transactional
    public boolean deleteAllByUserId(int userId) {
        requestRepository.deleteAllByUserId(userId);
        return true;
    }

    @Override
    public List<Request> getAllOpenRequest() {
        return requestRepository.findAllByStatus(RequestStatus.open);
    }
}
