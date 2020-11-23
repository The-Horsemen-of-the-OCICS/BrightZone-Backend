package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.repository.ClassroomRepository;
import com.carleton.comp5104.cms.service.AdminClazzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

@Slf4j
@RestController
@RequestMapping("/admin/class")
public class AdminClazzController {
    @Autowired
    AdminClazzService adminClazzService;

    @GetMapping("/getClassByCourseId/{courseId}")
    public ArrayList<HashMap<String, String>> getClassByCourseId(@PathVariable int courseId) {
        return adminClazzService.getClassByCourseId(courseId);
    }

    @GetMapping("/getProfessorById/{userId}")
    public Account getProfessorById(@PathVariable int userId) {
        return adminClazzService.getProfessorById(userId);
    }

    @GetMapping("/getProfessorByEmail/{email}")
    public Account getProfessorByEmail(@PathVariable String email) {
        return adminClazzService.getProfessorByEmail(email);
    }

    @PostMapping("/classroomSchedule/")
    public ArrayList<Classroom> classroomSchedule(@RequestParam HashMap<String, String> checkMap) throws ParseException {
        return adminClazzService.classroomSchedule(checkMap);
    }

//    @GetMapping("/getClassroomSchedule/")
//    public ArrayList<Classroom> classroomSchedule(@RequestParam HashMap<String, String> checkMap) throws ParseException {
//        return adminClazzService.classroomSchedule(checkMap);
//    }

    @PostMapping("/addNewClass")
    public String addNewClass(@RequestBody HashMap<String, String> infoMap) {
        if (adminClazzService.addNewClass(infoMap) == 0) {
            return "success";
        } else {
            return "error";
        }
    }

}
