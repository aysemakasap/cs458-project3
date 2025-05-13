package com.cs458.part1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.cs458.part1.Enums.AIType;
import com.cs458.part1.Enums.Gender;
import com.cs458.part1.model.AIModel;
import com.cs458.part1.model.Survey;
import com.cs458.part1.service.SurveyService;

@SpringBootTest
@Transactional
public class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @Test
    public void testSaveSurvey() {
        // Create a test AIModel
        AIModel aiModel = new AIModel();
        aiModel.setAiType(AIType.chatGPT);
        aiModel.setDescription("Test description");

        List<AIModel> aiModels = new ArrayList<>();
        aiModels.add(aiModel);

        // Create a test Survey
        Survey survey = new Survey();
        survey.setName("Test Name");
        survey.setSurname("Test Surname");
        survey.setDateOfBirth(LocalDate.of(1990, 1, 1));
        survey.setCity("Test City");
        survey.setGender(Gender.male);
        survey.setEducationLevel("Bachelor Degree");
        survey.setUseCaseOfAi("Test use case");
        survey.setAiModel(aiModels);

        // Save the survey
        Survey savedSurvey = surveyService.saveSurvey(survey);

        // Verify the survey was saved correctly
        assertNotNull(savedSurvey.getId());
        assertEquals("Test Name", savedSurvey.getName());
        assertEquals("Test Surname", savedSurvey.getSurname());
        assertEquals(LocalDate.of(1990, 1, 1), savedSurvey.getDateOfBirth());
        assertEquals("Test City", savedSurvey.getCity());
        assertEquals(Gender.male, savedSurvey.getGender());
        assertEquals("Bachelor Degree", savedSurvey.getEducationLevel());
        assertEquals("Test use case", savedSurvey.getUseCaseOfAi());
        assertEquals(1, savedSurvey.getAiModel().size());
        assertEquals(AIType.chatGPT, savedSurvey.getAiModel().get(0).getAiType());
        assertEquals("Test description", savedSurvey.getAiModel().get(0).getDescription());
    }
}