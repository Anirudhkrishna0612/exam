// src/main/java/com/portal/exam/QuestionController.java
package com.portal.exam; // This is THE single package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// Imports for your custom classes (assuming all are directly in com.portal.exam)
import com.portal.exam.Question;
import com.portal.exam.Quiz;
import com.portal.exam.QuestionService;
import com.portal.exam.QuizService;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    // Add Question
    @PostMapping("/")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        Question addedQuestion = this.questionService.addQuestion(question);
        return ResponseEntity.ok(addedQuestion);
    }

    // Update Question
    @PutMapping("/")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question) {
        Question updatedQuestion = this.questionService.updateQuestion(question);
        return ResponseEntity.ok(updatedQuestion);
    }

    // Get Questions of any Quiz (Admin side - includes answers)
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("quizId") Long quizId) {
        Quiz quiz = this.quizService.getQuiz(quizId);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        Set<Question> questions = quiz.getQuestions();
        return ResponseEntity.ok(questions);
    }

    // Get Questions for user (only active quizzes, no answers - shuffled)
    @GetMapping("/quiz/active/{quizId}")
    public ResponseEntity<?> getQuestionsOfQuizForUser(@PathVariable("quizId") Long quizId) {
        Quiz quiz = this.quizService.getQuiz(quizId);
        if (quiz == null || !quiz.isActive()) {
            return ResponseEntity.notFound().build();
        }
        List<Question> questions = this.questionService.getQuestionsOfQuiz(quiz);

        // **FIX CONFIRMED IN QUIZ.JAVA**: quiz.getNumberOfQuestions() now returns Integer
        if (quiz.getNumberOfQuestions() != null && questions.size() > quiz.getNumberOfQuestions()) {
            List<Question> list = new ArrayList<>(questions);
            Collections.shuffle(list);
            questions = list.subList(0, quiz.getNumberOfQuestions());
        }

        questions.forEach(q -> q.setAnswer(""));
        return ResponseEntity.ok(questions);
    }

    // Get single Question
    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable("questionId") Long questionId) {
        Question question = this.questionService.getQuestion(questionId);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    // Delete Question
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("questionId") Long questionId) {
        this.questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    // Evaluate Quiz endpoint for users
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions) {
        System.out.println("Evaluating quiz for " + questions.size() + " questions.");

        double marksGot = 0;
        int correctAnswers = 0;
        int attempted = 0;

        for (Question q : questions) {
            Question actualQuestion = this.questionService.getQuestion(q.getQuesId());

            if (actualQuestion != null) {
                if (actualQuestion.getAnswer() != null && q.getAnswer() != null &&
                    actualQuestion.getAnswer().trim().equals(q.getAnswer().trim())) {
                    correctAnswers++;

                    Quiz quiz = actualQuestion.getQuiz();
                    // **FIX CONFIRMED IN QUIZ.JAVA**: quiz.getMaxMarks() and quiz.getNumberOfQuestions() now return Integer
                    if (quiz != null && quiz.getMaxMarks() != null && quiz.getNumberOfQuestions() != null && quiz.getNumberOfQuestions() > 0) {
                        double marksPerQuestion = (double) quiz.getMaxMarks() / quiz.getNumberOfQuestions();
                        marksGot += marksPerQuestion;
                    }
                }
                if (q.getAnswer() != null && !q.getAnswer().trim().isEmpty()) {
                    attempted++;
                }
            }
        }

        Map<String, Object> results = new HashMap<>();
        results.put("marksGot", marksGot);
        results.put("correctAnswers", correctAnswers);
        results.put("attempted", attempted);

        System.out.println("Quiz evaluation complete. Results: " + results);
        return ResponseEntity.ok(results);
    }
}
