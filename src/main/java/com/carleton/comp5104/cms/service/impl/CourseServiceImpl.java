package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.entity.Preclusion;
import com.carleton.comp5104.cms.entity.Prerequisite;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.PreclusionRepository;
import com.carleton.comp5104.cms.repository.PrerequisiteRepository;
import com.carleton.comp5104.cms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private PreclusionRepository preclusionRepository;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    public boolean registerCourse(int studentId, int classId) {
        Optional<Clazz> clazz = clazzRepository.findById(classId);
        if (!clazz.isPresent()) {
            return false;
        }

        if (clazz.get().getEnrollCapacity() <= clazz.get().getEnrolled()) {
            return false;
        }

        if (System.currentTimeMillis() > clazz.get().getEnrollDeadline().getTime()) {
            return false;
        }
        Set<Enrollment> enrollmentSet = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.passed);
        List<Integer> clazzIdList = enrollmentSet.stream().map(p -> p.getClassId()).collect(Collectors.toList());
        Set<Integer> enrolledCourseId = StreamSupport.stream(clazzRepository.findAllById(clazzIdList).spliterator(), false).map(e -> e.getCourseId()).collect(Collectors.toSet());

        Set<Integer> preReqId = prerequisiteRepository.findByCourseId(clazz.get().getCourseId()).stream().map(p -> p.getPrerequisiteId()).collect(Collectors.toSet());
        if (!enrolledCourseId.containsAll(preReqId)) {
            return false;
        }

        List<Integer> preCluId = preclusionRepository.findByCourseId(clazz.get().getCourseId()).stream().map(p -> p.getPreclusionId()).collect(Collectors.toList());
        if (!preCluId.retainAll(enrolledCourseId)) {
            return false;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setClassId(classId);
        enrollment.setEnrollTime(new Timestamp(System.currentTimeMillis()));
        enrollment.setStatus(EnrollmentStatus.ongoing);

        enrollmentRepository.save(enrollment);
        return true;
    }

    @Override
    public boolean dropCourse(int studentId, int clazzId) {
        Optional<Clazz> clazz = clazzRepository.findById(clazzId);
        if (!clazz.isPresent()) {
            return false;
        }

        if (System.currentTimeMillis() < clazz.get().getDropNoPenaltyDeadline().getTime()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(studentId, clazzId);
            enrollment.ifPresent(e -> {
                e.setStatus(EnrollmentStatus.dropped);
                enrollmentRepository.save(e);
            });
            return true;
        }

        if (System.currentTimeMillis() < clazz.get().getDropNoFailDeadline().getTime()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(studentId, clazzId);
            enrollment.ifPresent(e -> {
                e.setStatus(EnrollmentStatus.dropped_dr);
                enrollmentRepository.save(e);
            });
            return true;
        }

        return false;
    }

}
