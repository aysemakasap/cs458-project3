package com.cs458.part1.service;

import com.cs458.part1.exception.SurveyNotFoundException;
import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.repository.CustomSurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomSurveyService {

    @Autowired
    private CustomSurveyRepository repository;

    public List<CustomSurvey> getAllSurveys() {
        return repository.findAll();
    }

    public CustomSurvey getSurveyById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new SurveyNotFoundException("Survey not found with id: " + id));
    }

    public CustomSurvey createSurvey(CustomSurvey survey) {
        return repository.save(survey);
    }

    public CustomSurvey updateSurvey(Integer id, CustomSurvey surveyData) {
        CustomSurvey existing = getSurveyById(id);
        existing.setQuestions(surveyData.getQuestions());
        return repository.save(existing);
    }

    public void deleteSurvey(Integer id) {
        CustomSurvey existing = getSurveyById(id);
        repository.delete(existing);
    }
}
