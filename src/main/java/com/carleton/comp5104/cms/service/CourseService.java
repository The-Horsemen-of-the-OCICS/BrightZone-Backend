package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.vo.CourseVo;

import java.util.Set;

public interface CourseService {
    boolean registerCourse(int studentId, int clazzId);

    boolean dropCourse(int studentId, int clazzId);

    Set<CourseVo> getAllOpenedCourse(int studentId);

    Set<CourseVo> getAllRegisteredCourse(int studentId);

    CourseVo getCourse(int clazzId);


}
