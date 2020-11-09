package com.carleton.comp5104.cms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classroom {
    private Integer roomId;
    private Integer roomCapacity;
    private String roomDescription;
}
