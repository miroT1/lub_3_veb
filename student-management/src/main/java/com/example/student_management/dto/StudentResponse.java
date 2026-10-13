package com.example.student_management.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentResponse {

    private Long id;
    private String fullName;
    private String group;
    private String isu;
    private Integer dormNumber;
    private Integer room;
    private LocalDate dateArrived;
    private Boolean isForeign;
    private String notes;
}