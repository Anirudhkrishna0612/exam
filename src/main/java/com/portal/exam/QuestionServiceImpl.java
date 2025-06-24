// src/main/java/com/portal/exam/QuestionServiceImpl.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
        List<Question> questions = this.questionRepository.findByQuizOrderByQuesIdAsc(quiz);

        Collections.shuffle(questions);

        // Ensure we don't try to get more questions than available
        return questions.stream()
                .limit(numQuestions > 0 ? numQuestions : questions.size())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteQuestion(Long questionId) {
        this.questionRepository.deleteById(questionId);
    }

    @Override
    public Question getQuestionWithAnswer(Long questionId) {
        return this.questionRepository.findById(questionId).orElse(null);
    }
}
