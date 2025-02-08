package com.example.school.controller;

import com.example.school.entity.Student;
import com.example.school.entity.Teacher;
import com.example.school.dto.TeacherDTO;
import com.example.school.dto.StudentDTO;
import com.example.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping("/teacher/{teacherId}/addStudent")
    public ResponseEntity<TeacherDTO> addStudentToTeacher(
            @PathVariable Long teacherId,
            @RequestBody StudentDTO studentDTO) {
        TeacherDTO updatedTeacherDTO = schoolService.addStudentToTeacher(teacherId, studentDTO);
        return ResponseEntity.status(201).body(updatedTeacherDTO);
    }

    @PostMapping("/student/{studentId}/addTeacher")
    public ResponseEntity<StudentDTO> addTeacherToStudent(
            @PathVariable Long studentId, 
            @RequestBody TeacherDTO teacherDTO) {
        StudentDTO updatedStudentDTO = schoolService.addTeacherToStudent(studentId, teacherDTO);
        return ResponseEntity.status(201).body(updatedStudentDTO);
    }


    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<Set<StudentDTO>> getStudentsByTeacher(@PathVariable Long teacherId) {
        Set<StudentDTO> studentDTOs = schoolService.getStudentsByTeacher(teacherId);
        return ResponseEntity.ok(studentDTOs);
    }

    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<Set<TeacherDTO>> getTeachersByStudent(@PathVariable Long studentId) {
        Set<TeacherDTO> teacherDTOs = schoolService.getTeachersByStudent(studentId);
        return ResponseEntity.ok(teacherDTOs);
    }
}
