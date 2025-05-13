package com.cs458.part1.repository;

import com.cs458.part1.model.CustomSurvey;
import com.cs458.part1.model.Question;
import com.cs458.part1.Enums.QuestionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomSurveyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomSurveyRepository customSurveyRepository;

    @Test
    public void shouldSaveSurvey() {
        CustomSurvey survey = buildSurvey();
        CustomSurvey saved = customSurveyRepository.save(survey);

        assertThat(saved).isNotNull();
        assertThat(saved.getQuestions()).hasSize(5);
    }

    @Test
    public void shouldFindSurveyById() {
        CustomSurvey survey = buildSurvey();
        entityManager.persistAndFlush(survey);

        Optional<CustomSurvey> found = customSurveyRepository.findById(survey.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getQuestions()).hasSize(5);
        assertThat(found.get().getQuestions().get(0).getQuestion()).isEqualTo("Which fruits?");
    }

    @Test
    public void shouldFindAllSurveys() {
        CustomSurvey s1 = buildSurvey();
        CustomSurvey s2 = buildSurvey();

        entityManager.persist(s1);
        entityManager.persist(s2);
        entityManager.flush();

        List<CustomSurvey> result = customSurveyRepository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    public void shouldDeleteSurvey() {
        CustomSurvey survey = buildSurvey();
        entityManager.persistAndFlush(survey);

        customSurveyRepository.deleteById(survey.getId());

        Optional<CustomSurvey> deleted = customSurveyRepository.findById(survey.getId());
        assertThat(deleted).isEmpty();
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
