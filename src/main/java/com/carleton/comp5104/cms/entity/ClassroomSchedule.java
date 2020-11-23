package com.carleton.comp5104.cms.entity;

import com.carleton.comp5104.cms.enums.StartDayOfWeek;
import com.carleton.comp5104.cms.enums.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomSchedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int scheduleId;
    private int roomId;
    private int professorId;
    private int classId;
    private int roomCapacity;
    @Enumerated(EnumType.STRING)
    private WeekDay weekday;
    private Time startTime;
    private Time endTime;
}
