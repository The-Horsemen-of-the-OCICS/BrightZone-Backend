package com.carleton.comp5104.cms.entity;


import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private Integer studentId;
    @Id
    private Integer classId;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
    private float finalGrade;
    private Timestamp enrollTime;
    private Timestamp dropTime;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnrollmentId implements Serializable {
        private int studentId;
        private int classId;
    }
}
