package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Course;

public interface CourseService {
    boolean registerCourse(int studentId, int clazzId) ;

     boolean dropCourse(int studentId, int clazzId);
}
