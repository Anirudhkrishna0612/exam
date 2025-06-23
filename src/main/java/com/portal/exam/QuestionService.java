// src/main/java/com/portal/exam/QuestionService.java
package com.portal.exam;

import java.util.List;
import java.util.Set; // Added import if you plan to return Sets of questions

public interface QuestionService {

    // Add a new question
    Question addQuestion(Question question);

    // Update an existing question
    Question updateQuestion(Question question);

    // Get all questions (primarily for admin)
    List<Question> getQuestions();

    // Get a single question by ID
    Question getQuestion(Long questionId);

    // Get questions for a specific quiz (for admin to manage)
    List<Question> getQuestionsOfQuiz(Quiz quiz);

    // Get a limited number of questions for a specific quiz (for user to take quiz)
    // The 'numQuestions' here means the *maximum* number to return.
    List<Question> getQuestionsForQuiz(Quiz quiz, int numQuestions);

    // Delete a question by ID
    void deleteQuestion(Long questionId);

    // Get a single question by its ID, for grading purposes (might need to fetch answer)
    // This is useful if you want to ensure the answer is correctly fetched from the DB
    Question getQuestionWithAnswer(Long questionId);
}
