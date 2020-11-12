package com.carleton.comp5104.cms.enums;

public enum EnrollmentStatus {
    ONGOING(1, "ONGOING"),
    PASSED(2, "PASSED"),
    DROPPED(3, "DROPPED"),
    FAILED(4, "FAILED"),
    ;
    private int value;
    private String desc;

    EnrollmentStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
