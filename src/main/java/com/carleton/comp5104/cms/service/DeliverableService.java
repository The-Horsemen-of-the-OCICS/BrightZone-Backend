package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.vo.DeliverableVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;


public interface DeliverableService {
    boolean submitDeliverable(int studentId, int deliverableId, MultipartFile file, String desc) throws IOException;

    Set<DeliverableVo> getAllCourseAssignment(int clazzId, int studentId);

    void deleteAssignment(int deliverableId, int studentId);
}
