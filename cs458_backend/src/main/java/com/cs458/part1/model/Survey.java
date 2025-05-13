package com.cs458.part1.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "surveys")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String city;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "survey_id")
    private List<AIModel> aiModel;
    
    private String useCaseOfAi;
    private String educationLevel;
}