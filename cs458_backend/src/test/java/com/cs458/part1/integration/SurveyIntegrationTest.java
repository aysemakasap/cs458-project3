package com.cs458.part1.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cs458.part1.model.Survey;
import com.cs458.part1.model.AIModel;
import com.cs458.part1.model.AIType;
import com.cs458.part1.model.Gender;
import com.cs458.part1.repository.SurveyRepository;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    public void shouldSaveAndRetrieveSurvey() throws Exception {
        // Clear existing data
        surveyRepository.deleteAll();
        
        // Create an AIModel
        AIModel aiModel = new AIModel();
        aiModel.setAiType(AIType.chatGPT);
        aiModel.setDescription("Test description");

        // Create a Survey
        Survey survey = Survey.builder()
                .name("Test Name")
                .surname("Test Surname")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Test City")
                .gender(Gender.male)
                .educationLevel("Bachelor Degree")
                .useCaseOfAi("Test use case")
                .aiModel(Collections.singletonList(aiModel))
                .build();

        // Save the survey
        ResultActions saveResponse = mockMvc.perform(post("/api/survey/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Name")))
                .andExpect(jsonPath("$.surname", is("Test Surname")));

        // Get all surveys
        mockMvc.perform(get("/api/survey/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Name")))
                .andExpect(jsonPath("$[0].aiModel[0].aiType", is("chatGPT")));
    }
}