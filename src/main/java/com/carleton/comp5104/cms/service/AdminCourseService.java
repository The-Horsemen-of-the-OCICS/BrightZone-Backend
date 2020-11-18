package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface AdminCourseService {
    public Page<Course> getAllCourse(Integer pageNum, Integer pageSize);

    public Course getCourseById(Integer courseId);

    public String addNewCourse(Course newCourse);

    public String deleteACourse(Integer courseId);

    public String updateACourse(Course updatingCourse);

    public String newCourseNumberValidCheck(Integer courseNumber);

    public String newCourseNameValidCheck(Integer courseName);

    Map<Integer, String> getSubjects();
}
