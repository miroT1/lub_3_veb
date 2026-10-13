package com.example.student_management.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "group_name")
    private String group;

    @Column(nullable = false, unique = true)
    private String isu;

    @Column(name = "dorm_number")
    private Integer dormNumber;

    @Column(name = "room")
    private Integer room;

    @Column(name = "date_arrived")
    private LocalDate dateArrived;

    @Column(name = "is_foreign")
    private Boolean isForeign;

    @Column(name = "notes", length = 1000)
    private String notes;
}