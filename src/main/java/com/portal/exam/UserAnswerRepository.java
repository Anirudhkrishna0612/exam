// src/main/java/com/portal/exam/UserAnswerRepository.java
package com.portal.exam; // Assuming this is your single package

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This repository will manage UserAnswer entities
@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    // You can add custom query methods here if needed, e.g.,
    // List<UserAnswer> findByUserAndQuiz(User user, Quiz quiz);
}
