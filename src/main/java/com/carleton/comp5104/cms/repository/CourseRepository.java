package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

}
