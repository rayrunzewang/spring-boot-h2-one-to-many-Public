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
@ToString(exclude = "teachers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "students", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default 
    private Set<Teacher> teachers = new HashSet<>();

    public static Student of(String name) {
        return new Student(null, name, new HashSet<>());
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) return;
        if (this.teachers == null) {
            this.teachers = new HashSet<>();
        }
        if (teacher.getStudents() == null) {
            teacher.setStudents(new HashSet<>());
        }
        this.teachers.add(teacher);
        teacher.getStudents().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        if (teacher == null || this.teachers == null) return;
        this.teachers.remove(teacher);
        if (teacher.getStudents() != null) {
            teacher.getStudents().remove(this);
        }
    }
}
