package com.carleton.comp5104.cms.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeliverableServiceTest {

    @Autowired
    private DeliverableService deliverableService;

    @Test
    public void testRegisterCourse() {
        boolean registerCourse = deliverableService.submitDeliverable(123, 1);
        Assert.assertSame(false, registerCourse);
    }

}
