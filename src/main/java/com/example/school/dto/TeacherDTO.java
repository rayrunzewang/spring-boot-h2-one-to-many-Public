package com.example.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeacherDTO {

    private Long id;
    private String name;
    private Set<Long> studentIds; // 只传递学生的 ID，而不是嵌套的学生对象

    // Constructors, getters, and setters if needed
}
