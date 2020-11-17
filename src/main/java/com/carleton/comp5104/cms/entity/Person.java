package com.carleton.comp5104.cms.entity;

import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    private Integer personId;
    private String name;

    @Enumerated(EnumType.STRING)
    private AccountType type;
    private Integer facultyId;
    private String program;

    @Enumerated(EnumType.STRING)
    private GenderType gender;
    private String email;
}
