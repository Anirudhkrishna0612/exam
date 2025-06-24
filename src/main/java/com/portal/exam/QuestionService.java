// src/main/java/com/portal/exam/QuestionService.java
package com.portal.exam;

import java.util.List;
import java.util.Set;

public interface QuestionService {

    Question addQuestion(Question question);
    Question updateQuestion(Question question);
    List<Question> getQuestions(); // Changed from Set<Question> to List<Question> for consistency
    Question getQuestion(Long questionId);
    List<Question> getQuestionsOfQuiz(Quiz quiz);
    List<Question> getQuestionsForQuiz(Quiz quiz, int numQuestions);
    void deleteQuestion(Long questionId);
    Question getQuestionWithAnswer(Long questionId);
}
