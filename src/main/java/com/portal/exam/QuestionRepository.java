// src/main/java/com/portal/exam/QuestionRepository.java
package com.portal.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
    List<Question> findByQuizOrderByQuesIdAsc(Quiz quiz);
}
