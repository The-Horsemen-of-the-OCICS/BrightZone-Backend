package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Classroom;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.ClassStatus;
import com.carleton.comp5104.cms.enums.WeekDay;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.ClassroomRepository;
import com.carleton.comp5104.cms.repository.ClassroomScheduleRepository;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.service.AdminClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AdminClazzServiceImpl implements AdminClazzService {

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClassroomScheduleRepository classroomScheduleRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public ArrayList<HashMap<String, String>> getClassByCourseId(int courseId) {
        ArrayList<HashMap<String, String>> classesInfoList = new ArrayList<>();

        ArrayList<Clazz> allClassByCourseId = clazzRepository.findAllByCourseId(courseId);
        for (Clazz clazz : allClassByCourseId) {
            HashMap<String, String> singleClassInfo = new HashMap<>();
            ArrayList<ClassroomSchedule> allByClassId = classroomScheduleRepository.findAllByClassId(clazz.getClassId());
            singleClassInfo.put("classId", String.valueOf(clazz.getClassId()));
            singleClassInfo.put("courseId", String.valueOf(clazz.getCourseId()));
            singleClassInfo.put("classDesc", clazz.getClassDesc());
            singleClassInfo.put("classStatus", String.valueOf(clazz.getClassStatus()));
            singleClassInfo.put("section", String.valueOf(clazz.getSection()));
            singleClassInfo.put("enrolled", String.valueOf(clazz.getEnrolled()));
            singleClassInfo.put("enrollCapacity", String.valueOf(clazz.getEnrollCapacity()));
            singleClassInfo.put("profId", String.valueOf(clazz.getProfId()));
            singleClassInfo.put("enrollDeadline", String.valueOf(clazz.getEnrollDeadline()));
            singleClassInfo.put("dropNoPenaltyDeadline", String.valueOf(clazz.getDropNoPenaltyDeadline()));
            singleClassInfo.put("dropNoFailDeadline", String.valueOf(clazz.getDropNoFailDeadline()));
            String scheduleInfo = "";
            for (ClassroomSchedule classroomSchedule : allByClassId) {
                scheduleInfo += classroomSchedule.getWeekday() + " @ " + classroomSchedule.getStartTime() + " - " + classroomSchedule.getEndTime() + " at Room:" + classroomSchedule.getRoomId() + "\n";
            }
            singleClassInfo.put("classSchedule", scheduleInfo);
            classesInfoList.add(singleClassInfo);
        }
        return classesInfoList;
    }


    @Override
    public Account getProfessorById(int id) {
        Optional<Account> byId = accountRepository.findById(id);
        if (byId.isPresent()) {
            AccountType type = byId.get().getType();
            if (type.equals(AccountType.professor)) {
                return byId.get();
            }
        }
        return null;
    }

    @Override
    public Account getProfessorByEmail(String email) {
        Optional<Account> byEmail = accountRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            Account account = byEmail.get();
            AccountType type = account.getType();
            if (type.equals(AccountType.professor)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Classroom> classroomSchedule(HashMap<String, String> checkMap) {
        String weekDay = checkMap.get("weekDay");
        String startTime = checkMap.get("startTime");
        String endTime = checkMap.get("endTime");
        int roomCapacityAsked = Integer.parseInt(checkMap.get("roomCapacityAsked"));
        //System.out.println(weekDay);
//        System.out.println(roomCapacityAsked);
//        int startTimeStamp = Integer.parseInt(startTime.split(":")[0]) * 60 * 60 + Integer.parseInt(startTime.split(":")[1]) * 60;
//        int endTimeStamp = Integer.parseInt(endTime.split(":")[0]) * 60 * 60 + Integer.parseInt(endTime.split(":")[1]) * 60;
        WeekDay weekDay1 = WeekDay.valueOf(weekDay);

        ArrayList<ClassroomSchedule> allByWeekdayAndRoomCapacity = classroomScheduleRepository.findAllByWeekdayAndRoomCapacity(weekDay1, roomCapacityAsked);
        ArrayList<Classroom> allClassroom = classroomRepository.findAllByRoomCapacity(roomCapacityAsked);
        ArrayList<Classroom> allAvailableClassroom = new ArrayList<>();
        ArrayList<Integer> assignedClassroomId = new ArrayList<>();
        for (ClassroomSchedule classroomSchedule : allByWeekdayAndRoomCapacity) {
            Time startTimeExisted = classroomSchedule.getStartTime();
            Time endTimeExisted = classroomSchedule.getEndTime();
            if (formatString2Time(endTime).before(startTimeExisted) || formatString2Time(startTime).after(endTimeExisted)) {
                ;
            } else {
                assignedClassroomId.add(classroomSchedule.getRoomId());
            }
        }
        for (Classroom classroom : allClassroom) {
            if (!assignedClassroomId.contains(classroom.getRoomId())) {
                allAvailableClassroom.add(classroom);
            }
        }
        return allAvailableClassroom;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public Integer addNewClass(HashMap<String, String> infoMap) {
        int status = 0;
        //class info first.
        int courseId = Integer.parseInt(infoMap.get("courseId"));
        String classDesc = infoMap.get("classDesc");
        String classStatus = infoMap.get("classStatus");
        int section = Integer.parseInt(infoMap.get("section"));
        int enrolled = Integer.parseInt(infoMap.get("enrolled"));
        int enrollCapacity = Integer.parseInt(infoMap.get("enrollCapacity"));
        int professorId = Integer.parseInt(infoMap.get("professorId"));
        int roomId = Integer.parseInt(infoMap.get("roomId"));
        String enrollDeadline = infoMap.get("enrollDeadline");
        String dropNoPenaltyDeadline = infoMap.get("dropNoPenaltyDeadline");
        String dropNoFailDeadline = infoMap.get("dropNoFailDeadline");

        Clazz newClazz = new Clazz();
        newClazz.setCourseId(courseId);
        newClazz.setClassDesc(classDesc);
        newClazz.setClassStatus(ClassStatus.valueOf(classStatus));
        newClazz.setSection(section);
        newClazz.setEnrolled(enrolled);
        newClazz.setEnrollCapacity(enrollCapacity);
        newClazz.setProfId(professorId);
        newClazz.setRoomId(roomId);
        newClazz.setEnrollDeadline(formatString2Timestamp(enrollDeadline));
        newClazz.setDropNoPenaltyDeadline(formatString2Timestamp(dropNoPenaltyDeadline));
        newClazz.setDropNoFailDeadline(formatString2Timestamp(dropNoFailDeadline));
        Clazz newAddedClazz = clazzRepository.save(newClazz);

        int classId = newAddedClazz.getClassId();
        Optional<Classroom> classroom = classroomRepository.findById(newAddedClazz.getRoomId());
        if (classroom.isEmpty()) {
            return -1;
        }
        int roomCapacity = classroom.get().getRoomCapacity();
        String weekDay = infoMap.get("weekDay");
        String startTime = infoMap.get("startTime");
        String endTime = infoMap.get("endTime");
        ClassroomSchedule classroomSchedule = new ClassroomSchedule();
        classroomSchedule.setRoomId(roomId);
        classroomSchedule.setProfessorId(professorId);
        classroomSchedule.setClassId(classId);
        classroomSchedule.setRoomCapacity(roomCapacity);
        classroomSchedule.setWeekday(WeekDay.valueOf(weekDay));
        classroomSchedule.setStartTime(formatString2Time(startTime));
        classroomSchedule.setEndTime(formatString2Time(endTime));
        classroomScheduleRepository.save(classroomSchedule);

        return status;
    }

    private Time formatString2Time(String inputTime) {
        DateFormat df = new SimpleDateFormat("HH:MM:SS");
        Time inputTimeFormatted = null;
        try {
            inputTimeFormatted = new Time(df.parse(inputTime).getTime());
            System.out.println(inputTimeFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputTimeFormatted;
    }

    private Timestamp formatString2Timestamp(String tsStr) {
        Timestamp ts = null;
        try {
            ts = Timestamp.valueOf(tsStr);
            System.out.println(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

}
