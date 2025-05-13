package com.cs458.part1.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CustomSurvey")
@Data
public class CustomSurvey {
    @Id
    @GeneratedValue
    Integer Id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_id") // owns the relationship
    List<Question> Questions;
}
