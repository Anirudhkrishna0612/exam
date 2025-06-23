// src/main/java/com/portal/exam/QuizRepository.java
package com.portal.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Custom query method to find quizzes by category
    List<Quiz> findByCategory(Category category);

    // Custom query method to find active quizzes (for users)
    List<Quiz> findByActive(boolean active);

    // Custom query method to find active quizzes by category (for users, in a specific category)
    List<Quiz> findByCategoryAndActive(Category category, boolean active);
}
