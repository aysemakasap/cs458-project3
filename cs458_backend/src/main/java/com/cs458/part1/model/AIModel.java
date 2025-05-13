package com.cs458.part1.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_models")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    private AIType aiType;
    
    private String description;
}