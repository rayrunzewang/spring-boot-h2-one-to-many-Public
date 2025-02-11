package com.example.school.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class StudentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Set<Long> teachers;
}
