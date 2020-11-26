package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.entity.Submission;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.DeliverableService;
import com.carleton.comp5104.cms.vo.DeliverableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeliverableServiceImpl implements DeliverableService {

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    private final static String templatePath = "/data/deliverableFiles/";

    @Override
    @Transactional
    public boolean submitDeliverable(int studentId, int deliverableId, MultipartFile file, String desc) throws IOException {
        Optional<Deliverable> deliverable = deliverableRepository.findById(deliverableId);
        if (!deliverable.isPresent()) {
            return false;
        }

        if (System.currentTimeMillis() > deliverable.get().getDeadLine().getTime()) {
            return false;
        }

        File dest0 = new File(templatePath);
        File dest = new File(dest0, "123");

        if (!dest0.getParentFile().exists()) {
            dest0.getParentFile().mkdirs();
            //检测文件是否存在
        }
        if (!dest.exists()) {
            dest.mkdirs();
        }
        file.transferTo(dest);
        Submission submission = new Submission();
        submission.setDeliverableId(deliverableId);
        submission.setStudentId(studentId);
        submission.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        submission.setSubmissionDesc(desc);
        submissionRepository.save(submission);


        return true;
    }

    @Override
    public Set<DeliverableVo> getAllCourseAssignment(int clazzId, int studentId) {
        List<Deliverable> byClassId = deliverableRepository.findByClassId(clazzId);

        List<Submission> submissionList = submissionRepository.findByStudentId(studentId);
        Map<Integer, List<Submission>> submissionMap = submissionList.stream().collect(Collectors.groupingBy(Submission::getDeliverableId));


        Set<DeliverableVo> collect = byClassId.stream().map(d -> {
            DeliverableVo deliverableVo = new DeliverableVo();
            deliverableVo.setDeliverableId(d.getDeliverableId());
            deliverableVo.setDeliverableDesc(d.getDeliverableDesc());
            deliverableVo.setDeadline(d.getDeadLine());

            List<Submission> submissions = submissionMap.get(d.getDeliverableId());
            if (submissions != null) {
                submissions.stream().sorted(Comparator.comparing(Submission::getSubmitTime)).findFirst().ifPresent(f -> {
                    deliverableVo.setSubmitTime(f.getSubmitTime());
                    deliverableVo.setScore(f.getGrade());
                });
            }
            return deliverableVo;
        }).collect(Collectors.toSet());
        return collect;
    }
}
