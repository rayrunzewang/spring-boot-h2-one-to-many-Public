package com.example.school.service;

import com.example.school.entity.Teacher;
import com.example.school.entity.Student;
import com.example.school.repository.TeacherRepository;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DatabaseSeeder {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public DatabaseSeeder(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void seedDatabase() {
        Teacher teacher = new Teacher();
        teacher.setName("John Doe");
        teacherRepository.save(teacher);

        Student student = new Student();
        student.setName("Jane Smith");
        studentRepository.save(student);

        System.out.println("Database has been seeded with initial data.");
    }
}