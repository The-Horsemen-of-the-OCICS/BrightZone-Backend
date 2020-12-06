package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.entity.Prerequisite;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

public interface AdminCourseService {
    Page<Course> getAllCourse(Integer pageNum, Integer pageSize);

    Integer getCourseTableSize();

    Course getLastCourse();

    Course getCourseById(Integer courseId);

    Integer addNewCourse(Course newCourse);

    Integer deleteACourse(Integer courseId);

    Integer updateACourse(Course updatingCourse);

    Integer newCourseNumberValidCheck(String courseSubject, String courseNumber);

    Course getCourseBySubjectAndNumber(String courseSubject, String courseNumber);

    ArrayList<Course> getCourseBySubject(String courseSubject);

    Integer newCourseNameValidCheck(String courseName);

    ArrayList<HashMap<String, String>> getSubjects();

    /*  course prerequisite service: add/get/delete/update*/
    Integer addCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId);

    List<Course> getCoursePrerequisite(Integer courseId);

    Integer deleteCoursePrerequisite(Integer courseId, Integer prerequisiteId);

    Integer updateCoursePrerequisite(ArrayList<Integer> updatedPrerequisiteList, Integer courseId) throws Exception;

    /*  course preclusion service: add/get/delete/update*/
    Integer addCoursePreclusion(ArrayList<Integer> preclusionList, Integer courseId);

    List<Course> getCoursePreclusion(Integer courseId);

    Integer deleteCoursePreclusion(Integer courseId, Integer preclusionId);

    Integer updateCoursePreclusion(ArrayList<Integer> updatedPreclusionList, Integer courseId);
}
