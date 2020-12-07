package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.AdminTodoList;
import net.minidev.json.JSONObject;

import java.util.List;

public interface AdminIndexService {
    Integer getAccountTableSize();

    Integer getCourseTableSize();

    Integer getClazzTableSize();

    Integer getClazzRoomTableSize();

    List<AdminTodoList> getAdminTodoList(int adminId);

    Integer addAdminToDoList(JSONObject addForm);

    Integer changeToDoStatus(int todoListId);

    Integer modifyAdminTodoList(JSONObject addForm);
}
