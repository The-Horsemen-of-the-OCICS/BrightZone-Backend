package com.carleton.comp5104.cms.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DeliverableServiceTest {

    @Autowired
    private DeliverableService deliverableService;

    @Test
    public void testRegisterCourse() throws IOException {
        boolean registerCourse = deliverableService.submitDeliverable(123, 1, null, null);
        Assert.assertSame(false, registerCourse);
    }

}
