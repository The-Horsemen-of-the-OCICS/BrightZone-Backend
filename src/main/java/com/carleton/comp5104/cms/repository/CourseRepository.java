package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsCourseByCourseSubjectAndCourseNumber(String courseSubject, String courseNumber);

    Course findByCourseSubjectAndCourseNumber(String courseSubject, String courseNumber);

    ArrayList<Course> findAllByCourseSubject(String courseSubject);

    boolean existsCourseByCourseName(String courseName);

}
