package com.carleton.comp5104.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer courseId;

    private String courseSubject;
    private String courseNumber;
    private String courseName;
    private String courseDesc;
    private Integer credit;
}
