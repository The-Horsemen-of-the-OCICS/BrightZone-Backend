package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.enums.DropStatus;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.enums.RegisterStatus;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;

import java.util.Set;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private  EnrollmentRepository enrollmentRepository;


    @Test
    public void testRegisterCourse() {
        courseService.dropAllCourse(3000382);
        RegisterStatus registerStatus = courseService.registerCourse(3000382, 1049);
        Assert.assertEquals(RegisterStatus.success, registerStatus);
    }

    @Test
    public void testRegisterCourse1() {
        RegisterStatus registerStatus = courseService.registerCourse(3000382, 1070);
        Assert.assertEquals(RegisterStatus.fail1, registerStatus);
    }

    @Test
    public void testRegisterCourse2() {
        RegisterStatus registerStatus = courseService.registerCourse(3000382, 1071);
        Assert.assertEquals(RegisterStatus.fail3, registerStatus);
    }

    @Test
    public void testRegisterCourse3() {
        RegisterStatus registerStatus = courseService.registerCourse(3000382, 1072);
        Assert.assertEquals(RegisterStatus.fail3, registerStatus);
    }

    @Test
    public void testDropCourse() {
        DropStatus dropStatus = courseService.dropCourse(3000382, 1074);
        Assert.assertEquals(DropStatus.success1, dropStatus);
    }

    @Test
    public void testDropCourse1() {
        DropStatus dropStatus = courseService.dropCourse(3000382, 1076);
        Assert.assertEquals(DropStatus.fail, dropStatus);
    }
}
