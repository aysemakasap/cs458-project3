package com.cs458.part1.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.cs458.part1.model.Survey;
import com.cs458.part1.model.AIModel;
import com.cs458.part1.model.AIType;
import com.cs458.part1.model.Gender;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SurveyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    public void shouldSaveSurvey() {
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
        Survey savedSurvey = surveyRepository.save(survey);

        // Verify the survey was saved correctly
        assertThat(savedSurvey).isNotNull();
        assertThat(savedSurvey.getId()).isNotNull();
        assertThat(savedSurvey.getName()).isEqualTo("Test Name");
        assertThat(savedSurvey.getSurname()).isEqualTo("Test Surname");
        assertThat(savedSurvey.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(savedSurvey.getCity()).isEqualTo("Test City");
        assertThat(savedSurvey.getGender()).isEqualTo(Gender.male);
        assertThat(savedSurvey.getEducationLevel()).isEqualTo("Bachelor Degree");
        assertThat(savedSurvey.getUseCaseOfAi()).isEqualTo("Test use case");
        assertThat(savedSurvey.getAiModel()).hasSize(1);
        assertThat(savedSurvey.getAiModel().get(0).getAiType()).isEqualTo(AIType.chatGPT);
        assertThat(savedSurvey.getAiModel().get(0).getDescription()).isEqualTo("Test description");
    }

    @Test
    public void shouldFindAllSurveys() {
        // Create an AIModel
        AIModel aiModel1 = new AIModel();
        aiModel1.setAiType(AIType.chatGPT);
        aiModel1.setDescription("Test description 1");

        // Create first Survey
        Survey survey1 = Survey.builder()
                .name("Test Name 1")
                .surname("Test Surname 1")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Test City 1")
                .gender(Gender.male)
                .educationLevel("Bachelor Degree")
                .useCaseOfAi("Test use case 1")
                .aiModel(Collections.singletonList(aiModel1))
                .build();

        // Create an AIModel
        AIModel aiModel2 = new AIModel();
        aiModel2.setAiType(AIType.bard);
        aiModel2.setDescription("Test description 2");

        // Create second Survey
        Survey survey2 = Survey.builder()
                .name("Test Name 2")
                .surname("Test Surname 2")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .city("Test City 2")
                .gender(Gender.female)
                .educationLevel("Master Degree")
                .useCaseOfAi("Test use case 2")
                .aiModel(Collections.singletonList(aiModel2))
                .build();

        // Save both surveys
        entityManager.persist(survey1);
        entityManager.persist(survey2);
        entityManager.flush();

        // Get all surveys
        List<Survey> surveys = surveyRepository.findAll();

        // Verify we have two surveys
        assertThat(surveys).hasSize(2);
        
        // Check if both surveys are returned
        assertThat(surveys).extracting(Survey::getName)
                .containsExactlyInAnyOrder("Test Name 1", "Test Name 2");
    }
}