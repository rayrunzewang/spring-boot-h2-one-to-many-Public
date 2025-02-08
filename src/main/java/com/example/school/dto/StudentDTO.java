package com.example.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentDTO {

    private Long id;
    private String name;
    private Set<Long> teacherIds; // 只传递教师的 ID，而不是嵌套的教师对象

    // Constructors, getters, and setters if needed
}
