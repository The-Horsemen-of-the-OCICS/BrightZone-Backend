package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.AdminTodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminTodoListRepository extends JpaRepository<AdminTodoList, Integer> {
    List<AdminTodoList> findAllByAdminIdAndStatusEquals(int adminId, boolean isFinished);

    AdminTodoList findByAdminId(int adminId);
}
