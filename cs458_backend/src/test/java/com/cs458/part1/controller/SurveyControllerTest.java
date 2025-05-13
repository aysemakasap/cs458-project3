package com.cs458.part1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cs458.part1.model.Survey;
import com.cs458.part1.model.AIModel;
import com.cs458.part1.model.AIType;
import com.cs458.part1.model.Gender;
import com.cs458.part1.service.SurveyService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyController.class)
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Survey survey;

    @BeforeEach
    public void setup() {
        // Create an AIModel
        AIModel aiModel = new AIModel();
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
    public void shouldSaveSurvey() throws Exception {
        given(surveyService.saveSurvey(any(Survey.class))).willReturn(survey);

        mockMvc.perform(post("/api/survey/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(survey.getName())))
                .andExpect(jsonPath("$.surname", is(survey.getSurname())))
                .andExpect(jsonPath("$.city", is(survey.getCity())))
                .andExpect(jsonPath("$.educationLevel", is(survey.getEducationLevel())))
                .andExpect(jsonPath("$.useCaseOfAi", is(survey.getUseCaseOfAi())))
                .andExpect(jsonPath("$.aiModel", hasSize(1)))
                .andExpect(jsonPath("$.aiModel[0].aiType", is(survey.getAiModel().get(0).getAiType().toString())))
                .andExpect(jsonPath("$.aiModel[0].description", is(survey.getAiModel().get(0).getDescription())));
    }

    @Test
    public void shouldGetAllSurveys() throws Exception {
        List<Survey> surveys = Arrays.asList(survey);
        given(surveyService.getAllSurveys()).willReturn(surveys);

        mockMvc.perform(get("/api/survey/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(survey.getName())))
                .andExpect(jsonPath("$[0].surname", is(survey.getSurname())));
    }
}