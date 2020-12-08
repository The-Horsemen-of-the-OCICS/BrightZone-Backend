package com.carleton.comp5104.cms.service;


import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public interface AdminClazzService {

    ArrayList<Clazz> getClassInfoByCourseId(int courseId);

    ArrayList<ClassroomSchedule> getClassSchedulesByClassId(int classId);

    Account getProfessorById(int id);

    Account getProfessorByEmail(String email);

    ArrayList<Account> getProfessorList();

    TreeSet<Integer> getClassroomSizeList();

    ArrayList<Classroom> classroomSchedule(HashMap<String, String> checkMap) throws ParseException;

    Clazz addNewClassInfo(Clazz newClazz);

    Clazz updateClassInfo(Clazz newEditClazz);

    Integer addNewClassSchedules(ArrayList<HashMap<String, String>> newClassroomSchedule);

    Integer updateClassSchedules(ArrayList<HashMap<String, String>> newEditClassroomSchedule);

    Integer deleteClassByClassId(int classId);

}
