package com.carleton.comp5104.cms.entity;

import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import lombok.Data;

@Data
public class Enrollment {
    private int studentId;
    private int classId;
    private int finalGrade;
    private int enrollTime;
    private int dropTime;
    private EnrollmentStatus status;
}
