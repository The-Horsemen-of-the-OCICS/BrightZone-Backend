package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.service.AdminCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/course")
public class AdminCourseController {

    @Autowired
    private AdminCourseService adminCourseService;

    @GetMapping("/getAll/{pageNum}/{pageSize}")
    public Page<Course> getAllCourse(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        return adminCourseService.getAllCourse(pageNum, pageSize);
    }

    @GetMapping("/get/{id}")
    public Course getCourseById(@PathVariable("id") Integer courseId) {
        return adminCourseService.getCourseById(courseId);
    }

    @PostMapping("/add")
    public String addNewCourse(@RequestBody Course newCourse) {
        int status = adminCourseService.addNewCourse(newCourse);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteACourse(@PathVariable("id") Integer courseId) {
        //TODO deleteValidCheck
        //权限认证
        int status = adminCourseService.deleteACourse(courseId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PutMapping("/update")
    public String updateACourse(@RequestBody Course updatingCourse) {
        int status = adminCourseService.updateACourse(updatingCourse);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    //font-end check box data source
    @GetMapping("/addPage/getSubject")
    public Map<Integer, String> getSubjects() {
        return adminCourseService.getSubjects();
    }


    @GetMapping("/addPageCheck/number/{courseProject}/{courseNumber}")
    public String newCourseNumberValidCheck(@PathVariable String courseProject, @PathVariable String courseNumber) {
        int status = adminCourseService.newCourseNumberValidCheck(courseProject, courseNumber);
        if (status == 0) {
            return "valid";
        } else
            return "Repeat";
    }

    @GetMapping("/addPageCheck/name/{courseName}")
    public String newCourseNameValidCheck(@PathVariable String courseName) {
        int status = adminCourseService.newCourseNameValidCheck(courseName);
        if (status == 0) {
            return "valid";
        } else
            return "Repeat";
    }

    //TODO discuss the Preclusion table again.
    @PostMapping("/addPage/addPreclusion")
    public String addCoursePreclusion(@RequestBody List<Integer> preclusionList) {
        return "OK";
    }

    //TODO discuss the Prerequisite table again.
    @PostMapping("/addPage/addPrerequisite")
    public String addCoursePrerequisite(@RequestBody List<Integer> prerequisiteList) {
        return "OK";
    }


}
