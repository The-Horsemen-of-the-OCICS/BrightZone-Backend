package com.carleton.comp5104.cms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private Integer userId;
    private String name;
    private String type;
    private Integer facultyId;
    private String program;
    private String email;
    private String password;
    private String accountStatus;
    private Date lastLogin;
    private String verificationCode;
}
