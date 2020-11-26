package com.carleton.comp5104.cms.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeliverableVo {
    private int deliverableId;
    private String deliverableDesc;
    private Timestamp deadline;
    private Timestamp submitTime;
    private float score;

}
