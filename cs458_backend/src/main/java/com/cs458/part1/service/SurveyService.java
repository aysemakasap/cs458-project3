package com.cs458.part1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs458.part1.model.Survey;
import com.cs458.part1.repository.SurveyRepository;

import java.util.List;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public Survey saveSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }
    
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }
}