package com.cs458.part1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cs458.part1.model.CustomSurvey;

@Repository
public interface CustomSurveyRepository extends JpaRepository<CustomSurvey, Integer> {
}
