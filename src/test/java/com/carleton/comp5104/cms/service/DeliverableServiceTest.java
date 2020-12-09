package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import com.carleton.comp5104.cms.vo.DeliverableVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class DeliverableServiceTest {

    @Autowired
    private DeliverableService deliverableService;

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private ProfessorService professorService;

    @Test
    public void testSubmitDeliverable() throws IOException {
        boolean b = deliverableService.submitDeliverable(3000182, 199, null, null);
        Assert.assertSame(false, b);
    }

    @Test
    public void testSubmitDeliverable1() throws IOException {
        List<Deliverable> byClassId = deliverableRepository.findByClassId(1032);
        int deliverableId = byClassId.get(0).getDeliverableId();
        boolean b = deliverableService.submitDeliverable(3000182, deliverableId, new MockMultipartFile("file",
                "myAssignment.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()), "test");
        Assert.assertSame(true, b);
    }

    @Test
    public void testGetAllCourseAssignment() throws IOException {
        List<Deliverable> byClassId = deliverableRepository.findByClassId(1032);
        int deliverableId = byClassId.get(0).getDeliverableId();
        deliverableService.submitDeliverable(3000182, deliverableId, new MockMultipartFile("file",
                "myAssignment.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()), "test");
        Set<DeliverableVo> allCourseAssignment = deliverableService.getAllCourseAssignment(1032, 3000182);
        Assert.assertSame(true, allCourseAssignment.size() >= 0);
    }

    @Test
    public void testDeleteAssignment() {
        deliverableService.deleteAssignment(1, 3000112);
    }
}
