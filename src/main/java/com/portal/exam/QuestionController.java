package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    @PostMapping("/")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        System.out.println("Received Question payload: " + question);

        if (question.getQuiz() != null && question.getQuiz().getQid() != null) {
            Quiz quiz = this.quizService.getQuiz(question.getQuiz().getQid());
            if (quiz != null) {
                question.setQuiz(quiz);
            } else {
                System.err.println("Error: Quiz not found for QID: " + question.getQuiz().getQid() + ". Cannot add question.");
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            System.err.println("Error: Quiz ID is missing or quiz object is null in question payload. Cannot add question.");
            return ResponseEntity.badRequest().body(null);
        }

        Question addedQuestion = this.questionService.addQuestion(question);
        return ResponseEntity.ok(addedQuestion);
    }

    @PutMapping("/")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question) {
        System.out.println("Received Question payload for update: " + question);
        if (question.getQuiz() != null && question.getQuiz().getQid() != null) {
            Quiz quiz = this.quizService.getQuiz(question.getQuiz().getQid());
            if (quiz != null) {
                question.setQuiz(quiz);
            } else {
                System.err.println("Error: Quiz not found for QID: " + question.getQuiz().getQid() + " during update. Cannot update question.");
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            System.err.println("Error: Quiz ID missing or quiz object null in question payload during update. Cannot update question.");
            return ResponseEntity.badRequest().body(null);
        }

        Question updatedQuestion = this.questionService.updateQuestion(question);
        return ResponseEntity.ok(updatedQuestion);
    }

    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("qid") Long qid) {
        Quiz quiz = this.quizService.getQuiz(qid);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        Set<Question> questions = quiz.getQuestions();
        if (questions == null) {
            questions = Collections.emptySet();
        }
        List<Question> list = new ArrayList<>(questions);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/quiz/active/{quizId}")
    public ResponseEntity<?> getQuestionsOfQuizForUser(@PathVariable("quizId") Long quizId) {
        Quiz quiz = this.quizService.getQuiz(quizId);
        if (quiz == null || !quiz.isActive()) {
            return ResponseEntity.notFound().build();
        }
        
        List<Question> questions = this.questionService.getQuestionsForQuiz(quiz, quiz.getNumberOfQuestions());

        questions.forEach(q -> q.setAnswer("")); // Do not send answers to frontend for user quiz
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable("questionId") Long questionId) {
        Question question = this.questionService.getQuestion(questionId);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("questionId") Long questionId) {
        this.questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    // FIX: Updated evalQuiz to accept quizId as path variable and return total marks/questions
    @PostMapping("/eval-quiz/{quizId}")
    public ResponseEntity<?> evalQuiz(@PathVariable("quizId") Long quizId, @RequestBody List<Question> questions) {
        System.out.println("Evaluating quiz for Quiz ID: " + quizId + " with " + questions.size() + " submitted questions.");

        Quiz mainQuiz = this.quizService.getQuiz(quizId); // Fetch the main quiz object
        if (mainQuiz == null) {
            System.err.println("Error: Quiz not found for evaluation with QID: " + quizId);
            return ResponseEntity.badRequest().body(Map.of("message", "Quiz not found for evaluation."));
        }

        double marksGot = 0;
        int correctAnswers = 0;
        int attempted = 0;

        // Calculate marks per question based on the main quiz's configuration
        double marksPerQuestion = 0;
        if (mainQuiz.getMaxMarks() != null && mainQuiz.getNumberOfQuestions() != null && mainQuiz.getNumberOfQuestions() > 0) {
            marksPerQuestion = (double) mainQuiz.getMaxMarks() / mainQuiz.getNumberOfQuestions();
            System.out.println("DEBUG: Marks per question: " + marksPerQuestion);
        } else {
             System.err.println("WARNING: Quiz has invalid maxMarks or numberOfQuestions for QID: " + quizId);
        }


        for (Question q : questions) {
            Question actualQuestion = this.questionService.getQuestionWithAnswer(q.getQuesId()); // Fetch question with its answer

            if (actualQuestion != null) {
                // Check if given answer matches actual answer (case-insensitive, trimmed)
                if (q.getGivenAnswer() != null && actualQuestion.getAnswer() != null &&
                    actualQuestion.getAnswer().trim().equalsIgnoreCase(q.getGivenAnswer().trim())) {
                    correctAnswers++;
                    marksGot += marksPerQuestion; // Add calculated marks per question
                }
                // Count attempted questions (any question with a non-empty given answer)
                if (q.getGivenAnswer() != null && !q.getGivenAnswer().trim().isEmpty()) {
                    attempted++;
                }
            } else {
                System.err.println("WARNING: Question with ID " + q.getQuesId() + " not found in DB during evaluation.");
            }
        }

        Map<String, Object> results = new HashMap<>();
        results.put("marksGot", String.format("%.2f", marksGot)); // Format to two decimal places
        results.put("correctAnswers", correctAnswers);
        results.put("attempted", attempted);
        results.put("totalQuestions", mainQuiz.getNumberOfQuestions()); // FIX: Return total questions
        results.put("totalMarks", mainQuiz.getMaxMarks()); // FIX: Return total marks

        System.out.println("Quiz evaluation complete. Results: " + results);
        return ResponseEntity.ok(results);
    }
}
