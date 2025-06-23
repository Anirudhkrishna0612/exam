// src/main/java/com/portal/exam/QuestionRepository.java
package com.portal.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set; // Added import for Set if needed

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Custom query method to find questions by a specific quiz
    List<Question> findByQuiz(Quiz quiz);

    // Custom query method to find questions for a quiz, limiting the number (e.g., for user quiz taking)
    // You might need to adjust this to order by random or specific criteria
    // @Query(value = "SELECT * FROM questions q WHERE q.quiz_q_id = ?1 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    // Set<Question> findQuestionsByQuizAndLimit(Long quizId, int limit); // Use Set to avoid duplicates if RAND() isn't distinct

    // Example of finding a certain number of questions by quiz (can be used for user quiz)
    // This will fetch all and then the service can randomize/limit
    List<Question> findByQuizOrderByQuesIdAsc(Quiz quiz); // Order by ID for consistent retrieval
}
