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
import java.util.Optional;
import java.util.Set;

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
        if (clazz.isEmpty()) {
            return false;
        }
        Set<Enrollment> passedCourseIdSet = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.passed);
        Set<Preclusion> preCourseIdSet = preclusionRepository.findByCourseId(clazz.get().getCourseId());

        if (passedCourseIdSet.contains(preCourseIdSet)) {
            return false;
        }

        Set<Prerequisite> prerequisiteSet = prerequisiteRepository.findByCourseId(clazz.get().getCourseId());
        for (Prerequisite prerequisite : prerequisiteSet) {
            if (!passedCourseIdSet.contains(prerequisite)) {
                return false;
            }
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setClassId(classId);
        enrollment.setEnrollTime(new Timestamp(System.currentTimeMillis()));
        enrollment.setStatus(EnrollmentStatus.ongoing);

        enrollmentRepository.save(enrollment);
        return true;
    }

}
