package com.cs458.part1.controller;

import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.model.Question;
import com.cs458.part1.Enums.QuestionType;
import com.cs458.part1.service.CustomSurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomSurveyController.class)
public class CustomSurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomSurveyService customSurveyService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomSurvey survey;

    @BeforeEach
    public void setup() {
        survey = new CustomSurvey();
        survey.setId(1);

        Question q1 = new Question();
        q1.setType(QuestionType.MultipleChoice);
        q1.setQuestion("Which fruits?");
        q1.setOptions(List.of("Apple", "Banana"));
        q1.setOpenAnswer("Apple, Banana");

        Question q2 = new Question();
        q2.setType(QuestionType.Checkbox);
        q2.setQuestion("Accept terms?");
        q2.setOptions(List.of("Yes", "No"));
        q2.setOpenAnswer("Yes");

        Question q3 = new Question();
        q3.setType(QuestionType.Dropdown);
        q3.setQuestion("Choose country");
        q3.setOptions(List.of("Turkey", "USA"));
        q3.setOpenAnswer("Turkey");

        Question q4 = new Question();
        q4.setType(QuestionType.RatingScale);
        q4.setQuestion("Rate service");
        q4.setRating(8);

        Question q5 = new Question();
        q5.setType(QuestionType.OpenEnded);
        q5.setQuestion("Feedback");
        q5.setOpenAnswer("Great UI");

        survey.setQuestions(List.of(q1, q2, q3, q4, q5));
    }

    @Test
    public void shouldCreateSurvey() throws Exception {
        given(customSurveyService.createSurvey(any(CustomSurvey.class))).willReturn(survey);

        mockMvc.perform(post("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questions", hasSize(5)))
                .andExpect(jsonPath("$.questions[0].question", is("Which fruits?")))
                .andExpect(jsonPath("$.questions[3].rating", is(8)));
    }

    @Test
    public void shouldGetSurveyById() throws Exception {
        given(customSurveyService.getSurveyById(1)).willReturn(survey);

        mockMvc.perform(get("/api/surveys/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.questions", hasSize(5)))
                .andExpect(jsonPath("$.questions[1].openAnswer", is("Yes")));
    }

    @Test
    public void shouldGetAllSurveys() throws Exception {
        given(customSurveyService.getAllSurveys()).willReturn(List.of(survey));

        mockMvc.perform(get("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].questions", hasSize(5)));
    }

    @Test
    public void shouldUpdateSurvey() throws Exception {
        given(customSurveyService.updateSurvey(eq(1), any(CustomSurvey.class))).willReturn(survey);

        mockMvc.perform(put("/api/surveys/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions", hasSize(5)))
                .andExpect(jsonPath("$.questions[2].openAnswer", is("Turkey")));
    }

    @Test
    public void shouldDeleteSurvey() throws Exception {
        mockMvc.perform(delete("/api/surveys/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
} 
