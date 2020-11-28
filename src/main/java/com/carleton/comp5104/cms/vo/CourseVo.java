package com.carleton.comp5104.cms.vo;

import lombok.Data;

@Data
public class CourseVo {
    private int clazzId;
    private int courseId;
    private String courseName;
    private String courseDesc;
    private String courseNo;
    private int professorId;
    private String professorName;
    private int enrolled;
}
