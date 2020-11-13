package com.carleton.comp5104.cms.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Clazz {
    @Id
    private Integer clazzId;
    private Integer courseId;
    private String classDescription;
    private String classStatus;
    private Integer section;
    private Integer enrolled;
    private Integer enrollCapacity;
    private Integer profId;
    private Time startTime;
    private Time endTime;
    private Integer classroomId;
    private Date enrollDeadline;
    private Date dropNoPenaltyDeadline;
    private Date dropNoFileDeadline;
}
