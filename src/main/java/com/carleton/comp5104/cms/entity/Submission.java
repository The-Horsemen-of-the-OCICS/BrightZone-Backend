package com.carleton.comp5104.cms.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Submission {
    private int submissionId;
    private Date submitTime;
    private String fileName;
    private int studentId;
    private int deliverableId;
    private String desc;
}
