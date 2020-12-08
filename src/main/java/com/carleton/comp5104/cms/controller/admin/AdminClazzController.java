package com.carleton.comp5104.cms.controller.admin;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.repository.ClassroomRepository;
import com.carleton.comp5104.cms.service.AdminClazzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/admin/class")
public class AdminClazzController {
    @Autowired
    AdminClazzService adminClazzService;

//    @GetMapping("/getClassByCourseId/{courseId}")
//    public ArrayList<HashMap<String, String>> getClassByCourseId(@PathVariable int courseId) {
//        return adminClazzService.getClassByCourseId(courseId);
//    }

    @GetMapping("/getClassInfoByCourseId/{courseId}")
    public ArrayList<Clazz> getClassInfoByCourseId(@PathVariable String courseId) {
        System.out.println(courseId);
        return adminClazzService.getClassInfoByCourseId(Integer.parseInt(courseId));
    }

    @GetMapping("/getClassSchedulesByClassId/{classId}")
    public ArrayList<ClassroomSchedule> getClassSchedulesByClassId(@PathVariable String classId) {
        System.out.println(classId);
        return adminClazzService.getClassSchedulesByClassId(Integer.parseInt(classId));
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

    @GetMapping("/getProfessorList")
    public ArrayList<Account> getProfessorList() {
        return adminClazzService.getProfessorList();
    }

    @GetMapping("/getClassroomSizeList")
    public TreeSet<Integer> getClassroomSizeList() {
        return adminClazzService.getClassroomSizeList();
    }

    @PostMapping("/addNewClass")
    public String addNewClass(@RequestParam HashMap<String, String> classBasicInfo, @RequestParam HashMap<String, String> scheduleForm) {

        System.out.println(classBasicInfo.toString());
        System.out.println(scheduleForm.toString());
//        if (adminClazzService.addNewClass(infoMap) == 0) {
//            return "success";
//        } else {
//            return "error";
//        }
        return "success";
    }

    @PostMapping("/addNewClassInfo")
    public Clazz addNewClassInfo(@RequestBody Clazz newClazz) {
        System.out.println(newClazz.toString());
        Clazz clazz = adminClazzService.addNewClassInfo(newClazz);
        if (clazz != null) {
            return clazz;
        } else {
            return null;
        }
    }

    @PostMapping("/updateClassInfo")
    public Clazz updateClassInfo(@RequestBody Clazz newEditClazz) {
        System.out.println(newEditClazz.toString());
        Clazz clazz = adminClazzService.updateClassInfo(newEditClazz);
        if (clazz != null) {
            return clazz;
        } else {
            return null;
        }
    }


    @PostMapping("/addNewClassSchedules")
    public String addNewClassSchedules(@RequestBody ArrayList<HashMap<String, String>> newClassroomSchedules) {
        System.out.println(newClassroomSchedules.toString());
        Integer status = adminClazzService.addNewClassSchedules(newClassroomSchedules);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @PostMapping("/updateClassSchedules")
    public String updateClassSchedules(@RequestBody ArrayList<HashMap<String, String>> newEditClassroomSchedules) {
        System.out.println(newEditClassroomSchedules.toString());
        Integer status = adminClazzService.updateClassSchedules(newEditClassroomSchedules);
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

    @DeleteMapping("/deleteClassByClassId/{classId}")
    public String deleteClassByClassId(@PathVariable String classId) {
        Integer status = adminClazzService.deleteClassByClassId(Integer.parseInt(classId));
        if (status == 0) {
            return "success";
        } else {
            return "error";
        }
    }

}
