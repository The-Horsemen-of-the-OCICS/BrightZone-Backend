package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    void deleteAllByUserId(int userId);

    List<Request> findAllByStatus(RequestStatus requestStatus);
}
