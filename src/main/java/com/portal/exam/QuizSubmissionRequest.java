// src/main/java/com/portal/exam/QuizSubmissionRequest.java
package com.portal.exam; // Assuming this is your single package

import java.util.List;

// This class will be used as the @RequestBody for quiz submissions
public class QuizSubmissionRequest {
    private Long quizId;
    private List<Question> userAnswers; // Questions submitted by user, including givenAnswer

    // Constructor
    public QuizSubmissionRequest() {
    }

    public QuizSubmissionRequest(Long quizId, List<Question> userAnswers) {
        this.quizId = quizId;
        this.userAnswers = userAnswers;
    }

    // Getters and Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<Question> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<Question> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
