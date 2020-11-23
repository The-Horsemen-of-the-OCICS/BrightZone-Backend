package com.carleton.comp5104.cms.service;


import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public interface AdminClazzService {
    ArrayList<HashMap<String, String>> getClassByCourseId(int courseId);

    Account getProfessorById(int id);

    Account getProfessorByEmail(String email);

    ArrayList<Classroom> classroomSchedule(HashMap<String, String> checkMap) throws ParseException;

    Integer addNewClass(HashMap<String, String> infoMap);

}
