package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Enrollment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void save() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1000000);  // do make sure record of this student already existed in db
        enrollment.setClassId(1000);       // do make sure record of this class already existed in db
        Enrollment save = enrollmentRepository.save(enrollment);
        System.out.println(save);
    }
}