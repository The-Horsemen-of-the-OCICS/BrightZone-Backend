package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.enums.DropStatus;
import com.carleton.comp5104.cms.enums.RegisterStatus;
import com.carleton.comp5104.cms.vo.CourseVo;

import java.util.List;
import java.util.Set;

public interface CourseService {
    RegisterStatus registerCourse(int studentId, int clazzId);

    DropStatus dropCourse(int studentId, int clazzId);

    List<CourseVo> getAllOpenedCourse(int studentId);

    Set<CourseVo> getAllRegisteredCourse(int studentId);

    CourseVo getCourse(int clazzId);

    void dropAllCourse(int studentId);


}
