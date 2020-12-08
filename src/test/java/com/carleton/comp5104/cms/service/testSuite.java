package com.carleton.comp5104.cms.service;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringJUnit4ClassRunner.class)
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
