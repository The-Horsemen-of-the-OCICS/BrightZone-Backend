package com.carleton.comp5104.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.NumericBooleanType;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deliverable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int deliverableId;

    private int classId;
    private Timestamp deadLine;
    private String deliverableDesc;
    private float percent;
    private Boolean isNotified;

    public int getDeliverableId() {
        return deliverableId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int class_id) {
        this.classId = class_id;
    }

    public Timestamp getDeadLine() {
        return deadLine;
    }

    public void setDead_line(Timestamp dead_line) {
        this.deadLine = dead_line;
    }

    public String getDesc() {
        return deliverableDesc;
    }

    public void setDesc(String desc) {
        this.deliverableDesc = desc;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Boolean getIsNotified() {
        return isNotified;
    }

    public void setIsNotified(Boolean is_notified) {
        this.isNotified = is_notified;
    }


}
