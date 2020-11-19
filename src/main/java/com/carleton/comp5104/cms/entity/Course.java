package com.carleton.comp5104.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private Integer courseId;
    private String courseSubject;
    private String courseNumber;
    private String courseName;
    private String courseDesc;
    private Integer credit;
}
