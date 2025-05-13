package com.cs458.part1.controller;

import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.service.CustomSurveyService;
import com.cs458.part1.exception.SurveyNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class CustomSurveyController {

    @Autowired
    private CustomSurveyService customSurveyService;

    @GetMapping
    public List<CustomSurvey> getAllSurveys() {
        return customSurveyService.getAllSurveys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSurvey> getSurveyById(@PathVariable Integer id) {
        try {
            CustomSurvey survey = customSurveyService.getSurveyById(id);
            return ResponseEntity.ok(survey);
        } catch (SurveyNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CustomSurvey> createSurvey(@RequestBody CustomSurvey survey) {
        CustomSurvey created = customSurveyService.createSurvey(survey);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomSurvey> updateSurvey(@PathVariable Integer id, @RequestBody CustomSurvey survey) {
        try {
            CustomSurvey updated = customSurveyService.updateSurvey(id, survey);
            return ResponseEntity.ok(updated);
        } catch (SurveyNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Integer id) {
        try {
            customSurveyService.deleteSurvey(id);
            return ResponseEntity.noContent().build();
        } catch (SurveyNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
