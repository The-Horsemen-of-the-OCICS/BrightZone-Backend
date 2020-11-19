package com.carleton.comp5104.cms.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

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
    public Integer addNewCourse(Course newCourse) {
        int status = -1;
        try {
            Integer newCourseId = newCourse.getCourseId();
            Optional<Course> byId = courseRepository.findById(newCourseId);
            if (byId.isEmpty()) {
                courseRepository.save(newCourse);
                status = 0;
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    @Transactional
    public Integer deleteACourse(Integer courseId) {

        int status = -1;
        try {
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isPresent()) {
                ArrayList<Clazz> allClazzByCourseId = clazzRepository.findAllByCourseId(courseId);
                clazzRepository.deleteAll(allClazzByCourseId);
                courseRepository.deleteById(courseId);
                status = 0;
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public Integer updateACourse(Course updatingCourse) {
        int status = -1;
        try {
            Optional<Course> courseOptional = courseRepository.findById(updatingCourse.getCourseId());
            if (courseOptional.isPresent()) {
                courseRepository.save(updatingCourse);
                status = 0;
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public Integer newCourseNumberValidCheck(String courseProject, String courseNumber) {
        int status = -1;
        if (!courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseProject, courseNumber)) {
            status = 0;
        }
        return status;
    }

    @Override
    public Integer newCourseNameValidCheck(String courseName) {
        int status = -1;
        if (!courseRepository.existsCourseByCourseName(courseName)) {
            status = 0;
        }
        return status;
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
