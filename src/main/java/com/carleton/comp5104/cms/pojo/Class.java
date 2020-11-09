package com.carleton.comp5104.cms.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    private Integer classID;
    private Course course;
    private String classDescription;
    private String classStatus;
    private Integer section;
    private Integer enrolled;
    private Integer enrollCapacity;
    private Integer profId;
    private Time startTime;
    private Time endTime;
    private Classroom classroom;
    private Date enrollDeadline;
    private Date dropNoPenaltyDeadline;
    private Date dropNoFileDeadline;
}
