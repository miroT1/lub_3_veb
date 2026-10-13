package com.example.student_management.service;

import com.example.student_management.dto.StudentRequest;
import com.example.student_management.dto.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Page<StudentResponse> findAll(String fullName, String group, Integer dormNumber, Pageable pageable);

    StudentResponse findById(Long id);

    StudentResponse create(StudentRequest request);

    StudentResponse update(Long id, StudentRequest request);

    StudentResponse patch(Long id, StudentRequest request);

    void delete(Long id);
}