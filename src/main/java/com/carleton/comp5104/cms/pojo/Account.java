package com.carleton.comp5104.cms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Integer userId;
    private String userName;
    private String type;
    private Department department;
    private String program;
    private String email;
    private Date birthday;
    private String password;
    private Integer accountStatus;
    private Date lastLogin;
    private String verificationCode;
}
