package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.*;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.ClassStatus;
import com.carleton.comp5104.cms.enums.WeekDay;
import com.carleton.comp5104.cms.repository.*;
import com.carleton.comp5104.cms.service.AdminClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public ArrayList<Clazz> getClassInfoByCourseId(int courseId) {
        return clazzRepository.findAllByCourseId(courseId);
    }

    @Override
    public ArrayList<ClassroomSchedule> getClassSchedulesByClassId(int classId) {
        return classroomScheduleRepository.findAllByClassId(classId);
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
    public ArrayList<Account> getProfessorList() {
        return accountRepository.findByType(AccountType.valueOf("professor"));
    }

    @Override
    public TreeSet<Integer> getClassroomSizeList() {
        List<Classroom> allClassroom = classroomRepository.findAll();
        TreeSet<Integer> classroomSizeSet = new TreeSet<Integer>();
        for (Classroom classroom : allClassroom) {
            classroomSizeSet.add(classroom.getRoomCapacity());
        }
        for (Integer s : classroomSizeSet) {
            System.out.println(s);
        }
        return classroomSizeSet;
        //return new ArrayList<>(classroomSizeSet);
    }

    @Override
    public ArrayList<Classroom> classroomSchedule(HashMap<String, String> checkMap) {
        String weekDay = checkMap.get("weekday");
        String startTime = checkMap.get("startTime");
        String endTime = checkMap.get("endTime");

//        System.out.println(checkMap.get("roomCapacityAsked"));
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
    public Clazz addNewClassInfo(Clazz newClazz) {
        if (newClazz != null) {
            return clazzRepository.save(newClazz);
        } else {
            return null;
        }
    }

    @Override
    public Clazz updateClassInfo(Clazz newEditClazz) {
        try {
            return clazzRepository.save(newEditClazz);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Integer addNewClassSchedules(ArrayList<HashMap<String, String>> newClassroomSchedules) {
        int status = -1;
        try {
            if (newClassroomSchedules.size() != 0) {
                HashMap<String, String> profAndClassId = newClassroomSchedules.get(newClassroomSchedules.size() - 1);
                int professorId = Integer.parseInt(profAndClassId.get("profId"));
                int classId = Integer.parseInt(profAndClassId.get("classId"));
                newClassroomSchedules.remove(newClassroomSchedules.size() - 1);

                for (HashMap<String, String> schedule : newClassroomSchedules) {
                    int roomId = Integer.parseInt(schedule.get("roomId"));
                    int roomCapacityAsked = Integer.parseInt(schedule.get("roomCapacityAsked"));
                    WeekDay weekDay = WeekDay.valueOf(schedule.get("weekday"));

                    Time startTime = formatString2Time(schedule.get("startTime"));
                    Time endTime = formatString2Time(schedule.get("endTime"));

                    ClassroomSchedule newClassroomSchedule = new ClassroomSchedule();
                    newClassroomSchedule.setRoomId(roomId);
                    newClassroomSchedule.setProfessorId(professorId);
                    newClassroomSchedule.setClassId(classId);
                    newClassroomSchedule.setRoomCapacity(roomCapacityAsked);
                    newClassroomSchedule.setWeekday(weekDay);
                    newClassroomSchedule.setStartTime(startTime);
                    newClassroomSchedule.setEndTime(endTime);
                    classroomScheduleRepository.save(newClassroomSchedule);
                }
                status = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return status;
    }

    @Override
    public Integer updateClassSchedules(ArrayList<HashMap<String, String>> newEditClassroomSchedule) {
        int status = -1;
        try {
            if (newEditClassroomSchedule.size() != 0) {
                HashMap<String, String> profAndClassId = newEditClassroomSchedule.get(newEditClassroomSchedule.size() - 1);
                int professorId = Integer.parseInt(profAndClassId.get("profId"));
                int classId = Integer.parseInt(profAndClassId.get("classId"));
                newEditClassroomSchedule.remove(newEditClassroomSchedule.size() - 1);

                for (HashMap<String, String> schedule : newEditClassroomSchedule) {
                    int scheduleId = Integer.parseInt(schedule.get("scheduleId"));
                    int roomId = Integer.parseInt(schedule.get("roomId"));
                    int roomCapacityAsked = Integer.parseInt(schedule.get("roomCapacityAsked"));
                    WeekDay weekDay = WeekDay.valueOf(schedule.get("weekday"));

                    Time startTime = formatString2Time(schedule.get("startTime"));
                    Time endTime = formatString2Time(schedule.get("endTime"));

                    Optional<ClassroomSchedule> byId = classroomScheduleRepository.findById(scheduleId);
                    if (byId.isPresent()) {
                        ClassroomSchedule classroomSchedule = byId.get();
                        classroomSchedule.setRoomId(roomId);
                        classroomSchedule.setProfessorId(professorId);
                        classroomSchedule.setClassId(classId);
                        classroomSchedule.setRoomCapacity(roomCapacityAsked);
                        classroomSchedule.setWeekday(weekDay);
                        classroomSchedule.setStartTime(startTime);
                        classroomSchedule.setEndTime(endTime);
                        classroomScheduleRepository.save(classroomSchedule);
                    }
                }
                status = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public Integer deleteClassByClassId(int classId) {
        int status = -1;
        try {
            Optional<Clazz> byClassId = clazzRepository.findByClassId(classId);
            if (byClassId.isEmpty()) {
                return status;
            } else {
                classroomScheduleRepository.deleteByClassId(classId);
//                int i = enrollmentRepository.deleteByClassId(classId);
                List<Deliverable> AllDeliverableByClassId = deliverableRepository.findByClassId(classId);

                for (Deliverable deliverable : AllDeliverableByClassId) {
                    System.out.println(deliverable.getDeliverableId());
                    submissionRepository.deleteByDeliverableId(deliverable.getDeliverableId());
                }
                deliverableRepository.deleteByClassId(classId);
                enrollmentRepository.deleteByClassId(classId);
                clazzRepository.deleteById(classId);
                status = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return status;
    }


    private Time formatString2Time(String inputTime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Time inputTimeFormatted = null;
        try {
            inputTimeFormatted = new Time(df.parse(inputTime).getTime());
            System.out.println(inputTimeFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputTimeFormatted;
    }

}
