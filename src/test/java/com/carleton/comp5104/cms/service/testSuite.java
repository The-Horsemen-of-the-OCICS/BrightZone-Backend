package com.carleton.comp5104.cms.service;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountServiceTest.class,
        AdminAccountServiceTest.class,
        AdminClazzServiceTest.class,
        AdminCourseServiceTest.class,
        AdminIndexServiceTest.class,
        CourseServiceTest.class,
        DeliverableServiceTest.class,
        ProfessorServiceTest.class,
})

public class testSuite {
}
