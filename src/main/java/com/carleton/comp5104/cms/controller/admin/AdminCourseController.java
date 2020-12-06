package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.service.AdminCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/getCourseById/{courseId}")
    public Course getCourseById(@PathVariable("courseId") Integer courseId) {
        return adminCourseService.getCourseById(courseId);
    }

    @GetMapping("/getCourseBySubjectAndNumber/{courseSubject}/{courseNumber}")
    public Course getCourseBySubjectAndNumber(@PathVariable String courseSubject, @PathVariable String courseNumber) {
        System.out.println(courseSubject);
        System.out.println(courseNumber);
        return adminCourseService.getCourseBySubjectAndNumber(courseSubject, courseNumber);
    }

    @GetMapping("/getCourseBySubject/{courseSubject}")
    public ArrayList<Course> getCourseBySubject(@PathVariable String courseSubject) {
        System.out.println(courseSubject);
        return adminCourseService.getCourseBySubject(courseSubject);
    }

    @PostMapping("/addNewCourse")
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

    @PostMapping("/update")
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
    public ArrayList<HashMap<String, String>> getSubjects() {
        return adminCourseService.getSubjects();
    }


    @GetMapping("/addPageCheck/number/{courseSubject}/{courseNumber}")
    public String newCourseNumberValidCheck(@PathVariable String courseSubject, @PathVariable String courseNumber) {
        int status = adminCourseService.newCourseNumberValidCheck(courseSubject, courseNumber);
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
    public String addCoursePrerequisite(@RequestParam String prerequisiteCourseId, @RequestParam String courseId) {
        ArrayList<Integer> prerequisiteList = new ArrayList<>();
        prerequisiteList.add(Integer.parseInt(prerequisiteCourseId));
        int status = adminCourseService.addCoursePrerequisite(prerequisiteList, Integer.parseInt(courseId));
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

    @DeleteMapping("/CourseInfo/deleteCoursePrerequisite/{courseId}/{prerequisiteId}")
    public String deleteCoursePrerequisite(@PathVariable String courseId, @PathVariable String prerequisiteId) {
        System.out.println(courseId);
        System.out.println(prerequisiteId);
        int status = adminCourseService.deleteCoursePrerequisite(Integer.parseInt(courseId), Integer.parseInt(prerequisiteId));
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
    public String addCoursePreclusion(@RequestParam String preclusionCourseId, @RequestParam String courseId) {
        ArrayList<Integer> preclusionList = new ArrayList<>();
        preclusionList.add(Integer.parseInt(preclusionCourseId));
        int status = adminCourseService.addCoursePreclusion(preclusionList, Integer.parseInt(courseId));
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

    @DeleteMapping("/CourseInfo/deleteCoursePreclusion/{courseId}/{preclusionId}")
    public String deleteCoursePreclusion(@PathVariable String courseId, @PathVariable String preclusionId) {
        int status = adminCourseService.deleteCoursePreclusion(Integer.parseInt(courseId), Integer.parseInt(preclusionId));
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
