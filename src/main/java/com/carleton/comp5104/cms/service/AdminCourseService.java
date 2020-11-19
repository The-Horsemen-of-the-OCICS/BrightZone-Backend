package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AdminCourseService {
    Page<Course> getAllCourse(Integer pageNum, Integer pageSize);

    Course getCourseById(Integer courseId);

    Integer addNewCourse(Course newCourse);

    Integer deleteACourse(Integer courseId);

    Integer updateACourse(Course updatingCourse);

    Integer newCourseNumberValidCheck(String courseProject, String courseNumber);

    Integer newCourseNameValidCheck(String courseName);

    Map<Integer, String> getSubjects();
}
