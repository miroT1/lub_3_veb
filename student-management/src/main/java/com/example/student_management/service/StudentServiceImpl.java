package com.example.student_management.service;

import com.example.student_management.dto.StudentRequest;
import com.example.student_management.dto.StudentResponse;
import com.example.student_management.entity.Student;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> findAll(String fullName, String group, Integer dormNumber, Pageable pageable) {
        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (fullName != null && !fullName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%"));
            }
            if (group != null && !group.isBlank()) {
                predicates.add(cb.equal(root.get("group"), group));
            }
            if (dormNumber != null) {
                predicates.add(cb.equal(root.get("dormNumber"), dormNumber));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return studentRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        return toResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByIsu(request.getIsu())) {
            throw new DuplicateResourceException("Student with ISU " + request.getIsu() + " already exists");
        }
        Student student = new Student();
        applyRequest(student, request);
        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));

        if (request.getIsu() != null && !request.getIsu().equals(student.getIsu())
                && studentRepository.existsByIsu(request.getIsu())) {
            throw new DuplicateResourceException("Student with ISU " + request.getIsu() + " already exists");
        }
        applyRequest(student, request);
        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public StudentResponse patch(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));

        if (request.getIsu() != null && !request.getIsu().equals(student.getIsu())
                && studentRepository.existsByIsu(request.getIsu())) {
            throw new DuplicateResourceException("Student with ISU " + request.getIsu() + " already exists");
        }
        if (request.getFullName() != null) student.setFullName(request.getFullName());
        if (request.getGroup() != null)     student.setGroup(request.getGroup());
        if (request.getIsu() != null)       student.setIsu(request.getIsu());
        if (request.getDormNumber() != null) student.setDormNumber(request.getDormNumber());
        if (request.getRoom() != null)      student.setRoom(request.getRoom());
        if (request.getDateArrived() != null) student.setDateArrived(request.getDateArrived());
        if (request.getIsForeign() != null) student.setIsForeign(request.getIsForeign());
        if (request.getNotes() != null)     student.setNotes(request.getNotes());

        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    private void applyRequest(Student student, StudentRequest r) {
        student.setFullName(r.getFullName());
        student.setGroup(r.getGroup());
        student.setIsu(r.getIsu());
        student.setDormNumber(r.getDormNumber());
        student.setRoom(r.getRoom());
        student.setDateArrived(r.getDateArrived());
        student.setIsForeign(r.getIsForeign());
        student.setNotes(r.getNotes());
    }

    private StudentResponse toResponse(Student s) {
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setFullName(s.getFullName());
        r.setGroup(s.getGroup());
        r.setIsu(s.getIsu());
        r.setDormNumber(s.getDormNumber());
        r.setRoom(s.getRoom());
        r.setDateArrived(s.getDateArrived());
        r.setIsForeign(s.getIsForeign());
        r.setNotes(s.getNotes());
        return r;
    }
}