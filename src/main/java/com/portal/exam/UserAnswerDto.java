// src/main/java/com/portal/exam/UserAnswerDto.java
package com.portal.exam;

// This class acts as a DTO (Data Transfer Object) for a single user answer.
// It will be part of the list in QuizSubmissionRequest.
// It contains only the essential data needed from the frontend for grading.
public class UserAnswerDto {

    private Long questionId;
    private String selectedOption;

    // --- Constructors ---
    public UserAnswerDto() {
    }

    public UserAnswerDto(Long questionId, String selectedOption) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    // --- Getters and Setters ---
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    @Override
    public String toString() {
        return "UserAnswerDto{" +
               "questionId=" + questionId +
               ", selectedOption='" + selectedOption + '\'' +
               '}';
    }
}
