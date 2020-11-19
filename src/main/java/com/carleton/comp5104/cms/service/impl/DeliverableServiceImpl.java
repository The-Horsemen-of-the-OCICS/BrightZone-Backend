package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.service.DeliverableService;
import org.springframework.stereotype.Service;

@Service
public class DeliverableServiceImpl implements DeliverableService {

    @Override
    public boolean submitDeliverable(int studentId, int deliverableId) {
        return false;
    }
}
