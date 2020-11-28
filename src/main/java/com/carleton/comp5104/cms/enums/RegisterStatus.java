package com.carleton.comp5104.cms.enums;

import lombok.Getter;

@Getter
public enum RegisterStatus {
    success(true, "Register Success"),
    fail(false, "Register failed"),
    fail1(false, "Course is full"),
    fail2(false, "It is after registration deadline"),
    fail3(false, "You haven't passed pre-requisite courses"),
    fail4(false, "You passed preclusion courses"),
    ;

    private boolean status;
    private String reason;

    RegisterStatus(boolean status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
