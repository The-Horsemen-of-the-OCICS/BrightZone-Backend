package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.entity.Prerequisite;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdminCourseService {
    Page<Course> getAllCourse(Integer pageNum, Integer pageSize);

    Course getCourseById(Integer courseId);

    Integer addNewCourse(Course newCourse);

    Integer deleteACourse(Integer courseId);

    Integer updateACourse(Course updatingCourse);

    Integer newCourseNumberValidCheck(String courseProject, String courseNumber);

    Integer newCourseNameValidCheck(String courseName);

    Map<Integer, String> getSubjects();

    //Integer newCoursePrerequisiteValidCheck(Integer prerequisite, Integer courseId);

    Integer addCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId);

    ArrayList<Integer> getCoursePrerequisite(Integer courseId);

    Integer deleteCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId);

    Integer updateCoursePrerequisite(ArrayList<Integer> updatedPrerequisiteList, Integer courseId);

    Integer addCoursePreclusion(ArrayList<Integer> preclusionList, Integer courseId);

    ArrayList<Integer> getCoursePreclusion(Integer courseId);

    Integer deleteCoursePreclusion(ArrayList<Integer> preclusionList, Integer courseId);

    Integer updateCoursePreclusion(ArrayList<Integer> updatedPreclusionList, Integer courseId);
}
