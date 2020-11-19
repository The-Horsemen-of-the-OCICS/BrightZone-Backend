package com.carleton.comp5104.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private int submissionId;
    private Timestamp submitTime;
    private String fileName;
    private int studentId;
    private int deliverableId;
    private String submissionDesc;
    private float grade;

}