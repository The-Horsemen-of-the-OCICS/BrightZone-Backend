package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.CourseRepository;
import com.carleton.comp5104.cms.service.AdminCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Override
    public Page<Course> getAllCourse(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return courseRepository.findAll(pageable);
    }


    @Override
    public Course getCourseById(Integer courseId) {
        Optional<Course> byId = courseRepository.findById(courseId);
        return byId.orElse(null);
    }

    @Override
    public String addNewCourse(Course newCourse) {
        Integer newCourseId = newCourse.getCourseId();
        Optional<Course> byId = courseRepository.findById(newCourseId);
        if (byId.isEmpty()) {
            courseRepository.save(newCourse);
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String deleteACourse(Integer CourseID) {
        ArrayList<Clazz> allClazzByCourseId = clazzRepository.findAllByCourseId(CourseID);
        clazzRepository.deleteAll(allClazzByCourseId);
        courseRepository.deleteById(CourseID);
        return "success";
    }

    @Override
    public String updateACourse(Course updatingCourse) {
        courseRepository.save(updatingCourse);
        return "success";
    }

    @Override
    public String newCourseNumberValidCheck(Integer courseNumber) {
        if (courseRepository.existsById(courseNumber)) {
            return "Repeat";
        } else {
            return "Valid";
        }
    }

    @Override
    public String newCourseNameValidCheck(Integer courseName) {
        if (courseRepository.existsById(courseName)) {
            return "Repeat";
        } else {
            return "Valid";
        }
    }

    @Override
    public Map<Integer, String> getSubjects() {
        HashMap<Integer, String> subjects = new HashMap<>();
        List<Course> allCourses = courseRepository.findAll();
        for (Course course : allCourses) {
            if (!subjects.containsValue(course.getCourseSubject())) {
                subjects.put(subjects.size(), course.getCourseSubject());
            }
        }
        return subjects;
    }


}
