package com.example.school.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE) 
@ToString(exclude = "students") 
@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include 
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "teacher_student",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    public static Teacher of(String name) {
        return new Teacher(null, name, new HashSet<>());
    }

    public void addStudent(Student student) {
        if (student == null) return;
        if (this.students == null) {
            this.students = new HashSet<>();
        }
        if (student.getTeachers() == null) {
            student.setTeachers(new HashSet<>());
        }
        this.students.add(student);
        student.getTeachers().add(this);
    }

    public void removeStudent(Student student) {
        if (student == null || this.students == null) return;
        this.students.remove(student);
        if (student.getTeachers() != null) {
            student.getTeachers().remove(this);
        }
    }
}
