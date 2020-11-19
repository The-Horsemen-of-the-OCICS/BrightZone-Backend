package com.carleton.comp5104.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface DeliverableService {
    boolean submitDeliverable(int studentId, int deliverableId, MultipartFile file, String desc);
}
