// src/main/java/com/portal/exam/QuizServiceImpl.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service // Marks this as a Spring Service component
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository; // Inject the QuizRepository

    @Override
    public Quiz addQuiz(Quiz quiz) {
        return this.quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) {
        return this.quizRepository.save(quiz); // save() updates if ID exists, inserts otherwise
    }

    @Override
    public List<Quiz> getQuizzes() {
        return this.quizRepository.findAll();
    }

    @Override
    public Quiz getQuiz(Long quizId) {
        return this.quizRepository.findById(quizId).orElse(null); // Return null if not found
    }

    @Override
    public void deleteQuiz(Long quizId) {
        this.quizRepository.deleteById(quizId);
    }

    @Override
    public List<Quiz> getQuizzesOfCategory(Category category) {
        return this.quizRepository.findByCategory(category);
    }

    @Override
    public List<Quiz> getActiveQuizzes() {
        // Fetch quizzes that are marked as 'active' for users
        return this.quizRepository.findByActive(true);
    }

    @Override
    public List<Quiz> getActiveQuizzesOfCategory(Category category) {
        // Fetch active quizzes filtered by a specific category
        return this.quizRepository.findByCategoryAndActive(category, true);
    }
}
