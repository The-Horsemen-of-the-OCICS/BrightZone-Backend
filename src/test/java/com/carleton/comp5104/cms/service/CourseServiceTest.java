package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.enums.DropStatus;
import com.carleton.comp5104.cms.enums.RegisterStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Test
    public void testRegisterCourse() {
        RegisterStatus registerStatus = courseService.registerCourse(123, 1);
    }

    @Test
    public void testDropCourse() {
        DropStatus dropStatus = courseService.dropCourse(123, 1);
    }
}
