package com.carleton.comp5104.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(Prerequisite.PrerequisiteId.class)
public class Prerequisite {
    @Id
    private Integer prerequisiteId;
    @Id
    private Integer courseId;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrerequisiteId implements Serializable {
        private Integer prerequisiteId;
        private Integer courseId;
    }
}
