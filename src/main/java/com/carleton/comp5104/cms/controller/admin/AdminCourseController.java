package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.service.AdminCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping("/getCourseTableSize")
    public Integer getCourseTableSize() {
        return adminCourseService.getCourseTableSize();
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

    @PostMapping("/addPage/addPrerequisite")
    public String addCoursePrerequisite(@RequestParam ArrayList<Integer> prerequisiteList, @RequestParam Integer courseId) {
        int status = adminCourseService.addCoursePrerequisite(prerequisiteList, courseId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @GetMapping("/CourseInfo/getCoursePrerequisite/{courseId}")
    public List<Course> getCoursePrerequisite(@PathVariable Integer courseId) {
        return adminCourseService.getCoursePrerequisite(courseId);
    }

    @GetMapping("/CourseInfo/deleteCoursePrerequisite/{courseId}/{prerequisiteId}")
    public String deleteCoursePrerequisite(@PathVariable Integer courseId, @PathVariable Integer prerequisiteId) {
        int status = adminCourseService.deleteCoursePrerequisite(courseId, prerequisiteId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/editPage/updateCoursePrerequisite")
    public String updateCoursePrerequisite(@RequestBody ArrayList<Integer> updatedPrerequisiteList, @RequestParam Integer courseId) {
        int status = 0;
        try {
            status = adminCourseService.updateCoursePrerequisite(updatedPrerequisiteList, courseId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }


    @PostMapping("/addPage/addPreclusion")
    public String addCoursePreclusion(@RequestBody ArrayList<Integer> preclusionList, @RequestParam Integer courseId) {
        int status = adminCourseService.addCoursePreclusion(preclusionList, courseId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @GetMapping("/CourseInfo/getCoursePreclusion/{courseId}")
    public List<Course> getCoursePreclusion(@PathVariable Integer courseId) {
        return adminCourseService.getCoursePreclusion(courseId);
    }

    @GetMapping("/CourseInfo/deleteCoursePreclusion/{courseId}/{preclusionId}")
    public String deleteCoursePreclusion(@PathVariable Integer courseId, @PathVariable Integer preclusionId) {
        int status = adminCourseService.deleteCoursePreclusion(courseId, preclusionId);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/editPage/updateCoursePreclusion")
    public String updateCoursePreclusion(@RequestBody ArrayList<Integer> updatedPreclusionList, @RequestParam Integer courseId) {
        int status = 0;
        try {
            status = adminCourseService.updateCoursePreclusion(updatedPreclusionList, courseId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }


}
