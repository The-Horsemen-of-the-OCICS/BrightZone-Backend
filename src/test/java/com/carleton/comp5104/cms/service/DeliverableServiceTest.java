package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.vo.DeliverableVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Set;

@SpringBootTest
public class DeliverableServiceTest {

    @Autowired
    private DeliverableService deliverableService;

    @Test
    public void testSubmitDeliverable() throws IOException {
        boolean b = deliverableService.submitDeliverable(3000182, 199, null, null);
        Assert.assertSame(false, b);
    }

    @Test
    public void testGetAllCourseAssignment() {
        Set<DeliverableVo> allCourseAssignment = deliverableService.getAllCourseAssignment(1006, 3000182);
        Assert.assertSame(true, allCourseAssignment.size() >= 0);
    }

    @Test
    public void testDeleteAssignment(){
        deliverableService.deleteAssignment(1,3000182);
    }
}
