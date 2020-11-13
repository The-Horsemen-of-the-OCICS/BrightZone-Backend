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
@IdClass(Preclusion.PreclusionId.class)
public class Preclusion {
    @Id
    private Integer preclusionId;
    @Id
    private Integer courseId;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreclusionId implements Serializable {
        private Integer preclusionId;
        private Integer courseId;
    }
}
