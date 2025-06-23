// src/main/java/com/portal/exam/QuizAttemptService.java
package com.portal.exam; // Confirmed: This is your single package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;

import com.portal.exam.Question;
import com.portal.exam.Quiz;
import com.portal.exam.User;
import com.portal.exam.QuestionService;
import com.portal.exam.QuizService;
import com.portal.exam.UserService;
import com.portal.exam.QuizSubmissionRequest;
import com.portal.exam.QuizResult;
import com.portal.exam.UserAnswer; // CRITICAL: Import UserAnswer entity
import com.portal.exam.UserAnswerRepository; // CRITICAL: Import UserAnswerRepository

@Service
public class QuizAttemptService {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAnswerRepository userAnswerRepository; // CRITICAL: Inject the new repository

    public QuizResult submitAndGradeQuiz(QuizSubmissionRequest submissionRequest, String username) throws Exception {

        User currentUser = userService.getUserByUsername(username);
        if (currentUser == null) {
            throw new Exception("User not found: " + username);
        }

        Quiz quiz = quizService.getQuiz(submissionRequest.getQuizId());
        if (quiz == null) {
            throw new Exception("Quiz not found with ID: " + submissionRequest.getQuizId());
        }

        AtomicInteger correctAnswers = new AtomicInteger(0);
        AtomicInteger totalQuestionsAttempted = new AtomicInteger(0);
        double scorePerQuestion = 0;

        if (quiz.getMaxMarks() != null && quiz.getNumberOfQuestions() != null && quiz.getNumberOfQuestions() > 0) {
            scorePerQuestion = quiz.getMaxMarks().doubleValue() / quiz.getNumberOfQuestions().doubleValue();
        } else {
            scorePerQuestion = 0;
        }

        if (submissionRequest.getUserAnswers() != null) {
            for (Question submittedQuestion : submissionRequest.getUserAnswers()) {
                Question actualQuestion = questionService.getQuestion(submittedQuestion.getQuesId());

                if (actualQuestion != null) {
                    // CRITICAL: Create and save UserAnswer entity for each question
                    UserAnswer userAnswer = new UserAnswer();
                    userAnswer.setUser(currentUser);
                    userAnswer.setQuiz(quiz);
                    userAnswer.setQuestion(actualQuestion); // Save the actual question entity
                    userAnswer.setSubmittedAnswer(submittedQuestion.getAnswer() != null ? submittedQuestion.getAnswer().trim() : "");

                    // Save the user's answer
                    userAnswerRepository.save(userAnswer);

                    if (submittedQuestion.getAnswer() != null && !submittedQuestion.getAnswer().trim().isEmpty()) {
                        totalQuestionsAttempted.incrementAndGet();

                        if (actualQuestion.getAnswer() != null &&
                            actualQuestion.getAnswer().trim().equalsIgnoreCase(submittedQuestion.getAnswer().trim())) {
                            correctAnswers.incrementAndGet();
                        }
                    }
                }
            }
        }

        double calculatedMarksGot = correctAnswers.get() * scorePerQuestion;

        QuizResult result = new QuizResult();
        result.setMarksGot(calculatedMarksGot);
        result.setCorrectAnswers(correctAnswers.get());
        result.setAttempted(totalQuestionsAttempted.get());
        result.setTotalQuestions(quiz.getNumberOfQuestions() != null ? quiz.getNumberOfQuestions() : 0);

        return result;
    }
}
