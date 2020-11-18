package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
}
