package com.cs458.part1.model;

import java.util.List;

import com.cs458.part1.Enums.QuestionType;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Question")
@Data
public class Question {
    @Id
    @GeneratedValue
    Integer Id;
    @Enumerated(EnumType.STRING)
    QuestionType Type;
    Integer Rating;
    String Question;
    String OpenAnswer;
    @ElementCollection
    List<String> Options;
}
