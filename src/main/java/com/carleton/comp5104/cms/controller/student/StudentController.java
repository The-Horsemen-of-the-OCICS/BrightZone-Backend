package com.carleton.comp5104.cms.controller.student;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.service.CourseService;
import com.carleton.comp5104.cms.service.DeliverableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class StudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DeliverableService deliverableService;

    @GetMapping("/submitDeliverable/")
    public @ResponseBody
    String submitDeliverable(int studentId, int deliverableId, MultipartFile file) {
        return deliverableService.submitDeliverable(studentId, deliverableId, null, null) + "";
    }

    @GetMapping("/registerCourse/")
    public @ResponseBody
    String registerCourse(int studentId, int clazzId) {
        return courseService.registerCourse(studentId, clazzId) + "";
    }

    @GetMapping("/dropCourse/")
    public @ResponseBody
    String dropCourse(int studentId, int clazzId) {
        return courseService.dropCourse(studentId, clazzId) + "";
    }
}
