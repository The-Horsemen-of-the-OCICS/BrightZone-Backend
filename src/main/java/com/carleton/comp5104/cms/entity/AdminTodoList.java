package com.carleton.comp5104.cms.entity;

import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.enums.AdminTodoLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private Integer adminId;
    private String notes;
    private Date startTime;
    private Date endTime;
    @Enumerated(EnumType.STRING)
    private AdminTodoLevel level;
    private boolean status;
}
