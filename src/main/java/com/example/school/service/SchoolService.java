package com.example.school.service;

import com.example.school.dto.TeacherDTO;
import com.example.school.dto.StudentDTO;
import com.example.school.entity.Teacher;
import com.example.school.entity.Student;
import com.example.school.repository.TeacherRepository;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


@Service
@RequiredArgsConstructor
public class SchoolService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public TeacherDTO addStudentToTeacher(Long teacherId, StudentDTO studentDTO) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        
        Student student;
        if (studentDTO.getId() == null) {
            student = new Student();
            student.setName(studentDTO.getName());
            student = studentRepository.save(student); 
        } else {
            student = studentRepository.findById(studentDTO.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        }

        teacher.addStudent(student);
        teacherRepository.save(teacher);

        return convertTeacherToDTO(teacher);
    }

    public StudentDTO addTeacherToStudent(Long studentId, TeacherDTO teacherDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Teacher teacher;
        if (teacherDTO.getId() == null) {
            teacher = new Teacher();
            teacher.setName(teacherDTO.getName());
            teacher = teacherRepository.save(teacher); 
        } else {
            teacher = teacherRepository.findById(teacherDTO.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        }

        student.getTeachers().add(teacher);
        teacher.getStudents().add(student);

        studentRepository.save(student);
        teacherRepository.save(teacher);

        return convertStudentToDTO(student);
    }

    public Set<StudentDTO> getStudentsByTeacher(Long teacherId) {
    Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

    return teacher.getStudents().stream()
            .map(this::convertStudentToDTO)
            .collect(Collectors.toSet());
}


    public Set<TeacherDTO> getTeachersByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        return student.getTeachers().stream()
                .map(this::convertTeacherToDTO)
                .collect(Collectors.toSet());
    }

    private TeacherDTO convertTeacherToDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setName(teacher.getName());
        teacherDTO.setStudentIds(teacher.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet()));
        return teacherDTO;
    }

    private StudentDTO convertStudentToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setTeacherIds(student.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toSet()));
        return studentDTO;
    }
}