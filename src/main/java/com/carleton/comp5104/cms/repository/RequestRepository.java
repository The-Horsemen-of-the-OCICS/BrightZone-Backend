package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    void deleteAllByUserId(int userId);
}
