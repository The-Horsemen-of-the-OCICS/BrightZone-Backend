package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ClassroomScheduleRepository extends JpaRepository<ClassroomSchedule, Integer> {
    //ClassroomSchedule findByWeekdayAndRoomCapacity(WeekDay weekDay, int roomCapacity);

    ArrayList<ClassroomSchedule> findAllByWeekdayAndRoomCapacity(WeekDay weekDay, int roomCapacity);

    ArrayList<ClassroomSchedule> findAllByClassId(int classId);

    int deleteByClassId(int classId);

    int deleteByProfessorId(int professorId);
}
