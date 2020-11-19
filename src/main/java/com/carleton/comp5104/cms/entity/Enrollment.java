package com.carleton.comp5104.cms.entity;

import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(Enrollment.EnrollmentId.class)
public class Enrollment {
    @Id
    private int studentId;
    @Id
    private int classId;

    private float finalGrade;
    private Timestamp enrollTime;
    private Timestamp dropTime;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    public void setGrade(float grade) {
        this.finalGrade = grade;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    @Embeddable
    static class EnrollmentId implements Serializable {
        private int classId;
        private int studentId;

        public EnrollmentId(int c, int s) {
            this.classId = c;
            this.studentId = s;
        }

        public EnrollmentId() {
        }
    }
}
