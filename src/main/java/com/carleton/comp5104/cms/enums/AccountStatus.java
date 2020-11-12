package com.carleton.comp5104.cms.enums;

public enum AccountStatus {
    unauthorized(1, "unauthorized"),
    expelled(2, "expelled"),
    current(3, "current"),
    sabbatical(4, "sabbatical"),
    alumni(5, "alumni"),
    ;


    private int value;
    private String desc;

    AccountStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
