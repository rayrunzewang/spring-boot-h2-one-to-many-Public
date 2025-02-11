package com.example.school.service;

import com.example.school.dto.TeacherDTO;
import com.example.school.dto.StudentDTO;
import com.example.school.entity.Teacher;
import com.example.school.entity.Student;
import com.example.school.repository.TeacherRepository;
import com.example.school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public TeacherDTO addStudentToTeacher(Long teacherId, StudentDTO studentDTO) {
        Teacher teacher = getTeacherById(teacherId);
        Student student = getOrCreateStudent(studentDTO);

        teacher.addStudent(student);

        return convertTeacherToDTO(teacher);
    }

    @Transactional
    public StudentDTO addTeacherToStudent(Long studentId, TeacherDTO teacherDTO) {
        Student student = getStudentById(studentId);
        Teacher teacher = getOrCreateTeacher(teacherDTO);

        student.getTeachers().add(teacher);
        teacher.getStudents().add(student);

        studentRepository.save(student);
        teacherRepository.save(teacher);

        return convertStudentToDTO(student);
    }

    public Set<StudentDTO> getStudentsByTeacher(Long teacherId) {
        return getTeacherById(teacherId).getStudents().stream()
                .map(this::convertStudentToDTO)
                .collect(Collectors.toSet());
    }

    public Set<TeacherDTO> getTeachersByStudent(Long studentId) {
        return getStudentById(studentId).getTeachers().stream()
                .map(this::convertTeacherToDTO)
                .collect(Collectors.toSet());
    }

    private Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
    }

    private Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private Student getOrCreateStudent(StudentDTO dto) {
        if (dto == null || dto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student data");
        }

        if (dto.getId() != null) {
            return studentRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        }

        return studentRepository.save(Student.of(dto.getName()));
    }

    private Teacher getOrCreateTeacher(TeacherDTO dto) {
        if (dto == null || dto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid teacher data");
        }

        if (dto.getId() != null) {
            return teacherRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        }

        return teacherRepository.save(Teacher.of(dto.getName()));
    }

    private TeacherDTO convertTeacherToDTO(Teacher teacher) {
        return TeacherDTO.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .students(teacher.getStudents().stream()
                        .map(Student::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    private StudentDTO convertStudentToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .teachers(student.getTeachers().stream()
                        .map(Teacher::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
