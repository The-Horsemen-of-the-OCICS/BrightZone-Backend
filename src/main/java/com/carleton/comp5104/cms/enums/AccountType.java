package com.carleton.comp5104.cms.enums;

public enum AccountType {
    student(1, "student"),
    professor(2, "professor"),
    teaching_assistant(3, "teaching_assistant"),
    administrator(4, "administrator"),
    ;

    private int value;
    private String desc;

    AccountType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
