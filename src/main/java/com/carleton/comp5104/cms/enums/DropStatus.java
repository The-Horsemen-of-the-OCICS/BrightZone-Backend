package com.carleton.comp5104.cms.enums;

import lombok.Getter;

@Getter
public enum DropStatus {
    success(true, "Drop Success No DR"),
    success1(true, "Drop Success with DR"),
    fail(false, "Drop failed"),
    ;

    private boolean status;
    private String reason;

    DropStatus(boolean status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
