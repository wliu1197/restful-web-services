package com.rest.webservices.restful_web_services.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student")
public class StudentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @Size(min=2,message = "Name should has at least 2 characters")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "student_course",   // Join table name
            joinColumns = @JoinColumn(name = "student_id"),   // Student foreign key
            inverseJoinColumns = @JoinColumn(name = "course_id")  // Course foreign key
    )
    private Set<CourseDetails> courses = new HashSet<>();

}
