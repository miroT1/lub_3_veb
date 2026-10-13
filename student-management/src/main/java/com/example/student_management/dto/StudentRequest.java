package com.example.student_management.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {

    @NotBlank(message = "fullName is required")
    @Size(min = 2, max = 100, message = "fullName must be between 2 and 100 characters")
    private String fullName;

    @Pattern(regexp = "[A-Za-z]\\d{4}", message = "group must be like P3223")
    private String group;

    @NotBlank(message = "isu is required")
    @Pattern(regexp = "\\d{6}", message = "isu must contain exactly 6 digits")
    private String isu;

    @Min(value = 1, message = "dormNumber must be positive")
    @Max(value = 30, message = "dormNumber must be <= 30")
    private Integer dormNumber;

    @Min(value = 1, message = "room must be positive")
    private Integer room;

    @PastOrPresent(message = "dateArrived cannot be in the future")
    private LocalDate dateArrived;

    private Boolean isForeign;

    @Size(max = 1000, message = "notes must be <= 1000 characters")
    private String notes;
}