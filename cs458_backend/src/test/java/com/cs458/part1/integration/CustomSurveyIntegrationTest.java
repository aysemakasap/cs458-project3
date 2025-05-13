package com.cs458.part1.integration;

import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.model.Question;
import com.cs458.part1.Enums.QuestionType;
import com.cs458.part1.repository.CustomSurveyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomSurveyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomSurveyRepository customSurveyRepository;

    @BeforeEach
    public void setup() {
        customSurveyRepository.deleteAll();
    }

    @Test
    public void shouldCreateSurveyAndRetrieveIt() throws Exception {
        CustomSurvey survey = buildSurvey();

        // Create survey
        mockMvc.perform(post("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questions", hasSize(5)))
                .andExpect(jsonPath("$.questions[3].rating", is(8)));

        // Retrieve all surveys
        mockMvc.perform(get("/api/surveys")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].questions[0].question", is("Which fruits?")));
    }

    @Test
    public void shouldUpdateSurvey() throws Exception {
        CustomSurvey survey = customSurveyRepository.save(buildSurvey());
        survey.getQuestions().get(0).setOpenAnswer("Banana");

        mockMvc.perform(put("/api/surveys/" + survey.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(survey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions[0].openAnswer", is("Banana")));
    }

    @Test
    public void shouldDeleteSurvey() throws Exception {
        CustomSurvey survey = customSurveyRepository.save(buildSurvey());

        mockMvc.perform(delete("/api/surveys/" + survey.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/surveys/" + survey.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private CustomSurvey buildSurvey() {
        CustomSurvey survey = new CustomSurvey();

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
        return survey;
    }
}
