package com.cs458.part1.service;

import com.cs458.part1.exception.SurveyNotFoundException;
import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.model.Question;
import com.cs458.part1.Enums.QuestionType;
import com.cs458.part1.repository.CustomSurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomSurveyServiceTest {

    @Mock
    private CustomSurveyRepository customSurveyRepository;

    @InjectMocks
    private CustomSurveyService customSurveyService;

    private CustomSurvey survey;

    @BeforeEach
    public void setup() {
        survey = new CustomSurvey();
        survey.setId(1);

        Question q1 = new Question();
        q1.setType(QuestionType.MultipleChoice);
        q1.setQuestion("Select all fruits you like");
        q1.setOptions(List.of("Apple", "Banana"));
        q1.setOpenAnswer("Apple, Banana");

        Question q2 = new Question();
        q2.setType(QuestionType.Checkbox);
        q2.setQuestion("Accept terms?");
        q2.setOptions(List.of("Yes", "No"));
        q2.setOpenAnswer("Yes");

        Question q3 = new Question();
        q3.setType(QuestionType.Dropdown);
        q3.setQuestion("Select country");
        q3.setOptions(List.of("USA", "Turkey"));
        q3.setOpenAnswer("Turkey");

        Question q4 = new Question();
        q4.setType(QuestionType.RatingScale);
        q4.setQuestion("Rate experience");
        q4.setRating(9);

        Question q5 = new Question();
        q5.setType(QuestionType.OpenEnded);
        q5.setQuestion("Suggestions?");
        q5.setOpenAnswer("Add more color");

        survey.setQuestions(List.of(q1, q2, q3, q4, q5));
    }

    @Test
    public void shouldGetAllSurveys() {
        List<CustomSurvey> list = List.of(survey);
        given(customSurveyRepository.findAll()).willReturn(list);

        List<CustomSurvey> result = customSurveyService.getAllSurveys();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuestions()).hasSize(5);
    }

    @Test
    public void shouldGetSurveyById() {
        given(customSurveyRepository.findById(1)).willReturn(Optional.of(survey));

        CustomSurvey result = customSurveyService.getSurveyById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getQuestions().get(0).getType()).isEqualTo(QuestionType.MultipleChoice);
    }

    @Test
    public void shouldThrowWhenSurveyNotFound() {
        given(customSurveyRepository.findById(99)).willReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> customSurveyService.getSurveyById(99));
    }

    @Test
    public void shouldCreateSurvey() {
        given(customSurveyRepository.save(any(CustomSurvey.class))).willReturn(survey);

        CustomSurvey created = customSurveyService.createSurvey(survey);

        assertThat(created).isNotNull();
        assertThat(created.getQuestions()).hasSize(5);
    }

    @Test
    public void shouldUpdateSurvey() {
        given(customSurveyRepository.findById(1)).willReturn(Optional.of(survey));
        given(customSurveyRepository.save(any(CustomSurvey.class))).willReturn(survey);

        CustomSurvey updated = customSurveyService.updateSurvey(1, survey);

        assertThat(updated.getQuestions()).hasSize(5);
    }

    @Test
    public void shouldDeleteSurvey() {
        given(customSurveyRepository.findById(1)).willReturn(Optional.of(survey));

        customSurveyService.deleteSurvey(1);

        verify(customSurveyRepository, times(1)).delete(survey); // not deleteById
    }

    @Test
    public void shouldThrowWhenDeleteSurveyNotFound() {
        given(customSurveyRepository.findById(999)).willReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> customSurveyService.deleteSurvey(999));
    }
} 
