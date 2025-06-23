// src/main/java/com/portal/exam/QuizService.java
package com.portal.exam;

import java.util.List;
import java.util.Set;

public interface QuizService {

    // Add a new quiz
    Quiz addQuiz(Quiz quiz);

    // Update an existing quiz
    Quiz updateQuiz(Quiz quiz);

    // Get all quizzes (for admin)
    List<Quiz> getQuizzes();

    // Get a single quiz by ID
    Quiz getQuiz(Long quizId);

    // Delete a quiz by ID
    void deleteQuiz(Long quizId);

    // Get quizzes of a specific category (for admin)
    List<Quiz> getQuizzesOfCategory(Category category);
    
    // Get active quizzes (for users)
    List<Quiz> getActiveQuizzes();

    // Get active quizzes of a specific category (for users)
    List<Quiz> getActiveQuizzesOfCategory(Category category);
}
