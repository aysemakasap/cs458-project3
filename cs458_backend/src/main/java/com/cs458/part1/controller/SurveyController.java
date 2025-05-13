package com.cs458.part1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cs458.part1.model.Survey;
import com.cs458.part1.service.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/save")
    public ResponseEntity<Survey> saveSurvey(@RequestBody Survey survey) {
        Survey savedSurvey = surveyService.saveSurvey(survey);
        return ResponseEntity.ok(savedSurvey);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Survey>> getAllSurveys() {
        List<Survey> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }
}