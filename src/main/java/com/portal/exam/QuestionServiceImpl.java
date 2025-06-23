// src/main/java/com/portal/exam/QuestionServiceImpl.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional; // Changed to Optional for clarity with findById
import java.util.stream.Collectors; // For stream operations

@Service // Marks this as a Spring Service component
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question addQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public List<Question> getQuestions() {
        return this.questionRepository.findAll();
    }

    @Override
    public Question getQuestion(Long questionId) {
        return this.questionRepository.findById(questionId).orElse(null);
    }

    @Override
    public List<Question> getQuestionsOfQuiz(Quiz quiz) {
        return this.questionRepository.findByQuiz(quiz);
    }

    @Override
    public List<Question> getQuestionsForQuiz(Quiz quiz, int numQuestions) {
        List<Question> questions = this.questionRepository.findByQuizOrderByQuesIdAsc(quiz); // Get all for the quiz

        // Shuffle the questions to randomize order
        Collections.shuffle(questions);

        // Return a sublist up to the requested number of questions
        // Ensure not to exceed the available questions
        return questions.stream()
                .limit(numQuestions)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteQuestion(Long questionId) {
        this.questionRepository.deleteById(questionId);
    }

    @Override
    public Question getQuestionWithAnswer(Long questionId) {
        // This method is designed to fetch the full question including the answer
        // It's primarily used for grading on the backend.
        return this.questionRepository.findById(questionId).orElse(null);
    }
}
