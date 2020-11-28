package com.carleton.comp5104.cms.controller.student;

import com.carleton.comp5104.cms.controller.BaseController;
import com.carleton.comp5104.cms.service.CourseService;
import com.carleton.comp5104.cms.service.DeliverableService;
import com.carleton.comp5104.cms.vo.CourseVo;
import com.carleton.comp5104.cms.vo.DeliverableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Slf4j
@RestController
public class StudentController extends BaseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DeliverableService deliverableService;

    @PostMapping("/submitDeliverable")
    public @ResponseBody
    boolean submitDeliverable(int deliverableId, MultipartFile file) {
        try {
            return deliverableService.submitDeliverable(getUserId(), deliverableId, file, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/registerCourse")
    public @ResponseBody
    boolean registerCourse(int clazzId) {
        return courseService.registerCourse(getUserId(), clazzId);
    }

    @GetMapping("/getAllOpenedCourse")
    public @ResponseBody
    Set<CourseVo> getAllOpenedCourse() {
        return courseService.getAllOpenedCourse(getUserId());
    }

    @GetMapping("/getAllRegisteredCourse")
    public @ResponseBody
    Set<CourseVo> getAllRegisteredCourse() {
        return courseService.getAllRegisteredCourse(getUserId());
    }

    @GetMapping("/getAllDeliverable")
    public @ResponseBody
    Set<DeliverableVo> getAllDeliverable(int clazzId) {
        return deliverableService.getAllCourseAssignment(clazzId, getUserId());
    }

    @GetMapping("/dropCourse")
    public @ResponseBody
    boolean dropCourse(int clazzId) {
        return courseService.dropCourse(getUserId(), clazzId);
    }

    @GetMapping("/getCourseVo")
    @ResponseBody
    public CourseVo getCourseVo(int clazzId) {
        return courseService.getCourse(clazzId);
    }
}
