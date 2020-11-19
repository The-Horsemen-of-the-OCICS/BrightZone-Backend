package com.carleton.comp5104.cms.service;

import org.springframework.stereotype.Service;


public interface DeliverableService {
    boolean submitDeliverable(int studentId, int deliverableId);
}
