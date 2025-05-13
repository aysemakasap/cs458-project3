package com.cs458.part1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cs458.part1.model.AIModel;
import com.cs458.part1.model.AIType;
import com.cs458.part1.model.Gender;
import com.cs458.part1.model.Survey;
import com.cs458.part1.repository.SurveyRepository;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyService surveyService;

    private Survey survey;
    private AIModel aiModel;

    @BeforeEach
    public void setup() {
        // Create an AIModel
        aiModel = new AIModel();
        aiModel.setAiType(AIType.chatGPT);
        aiModel.setDescription("Test description");

        // Create a Survey
        survey = Survey.builder()
                .id(1)
                .name("Test Name")
                .surname("Test Surname")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Test City")
                .gender(Gender.male)
                .educationLevel("Bachelor Degree")
                .useCaseOfAi("Test use case")
                .aiModel(Collections.singletonList(aiModel))
                .build();
    }

    @Test
    public void shouldSaveSurvey() {
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey savedSurvey = surveyService.saveSurvey(survey);

        assertNotNull(savedSurvey);
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

        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    public void shouldGetAllSurveys() {
        List<Survey> surveyList = new ArrayList<>();
        surveyList.add(survey);
        
        when(surveyRepository.findAll()).thenReturn(surveyList);

        List<Survey> retrievedSurveys = surveyService.getAllSurveys();

        assertNotNull(retrievedSurveys);
        assertEquals(1, retrievedSurveys.size());
        assertEquals("Test Name", retrievedSurveys.get(0).getName());
        
        verify(surveyRepository, times(1)).findAll();
    }
}