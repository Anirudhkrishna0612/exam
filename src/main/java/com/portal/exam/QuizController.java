// src/main/java/com/portal/exam/QuizController.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid; // For @Valid annotation

import java.util.List;

@RestController
@RequestMapping("/quiz") // Base path for quiz-related endpoints
// @CrossOrigin("*") // Removed @CrossOrigin as it's handled globally in SecurityConfig
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private CategoryService categoryService; // Needed to fetch category for quiz

    // Add quiz
    // POST http://localhost:8060/quiz/
    @PostMapping("/")
    public ResponseEntity<Quiz> addQuiz(@Valid @RequestBody Quiz quiz) {
        try {
            // Ensure the category linked to the quiz exists
            if (quiz.getCategory() == null || quiz.getCategory().getCid() == null) {
                return ResponseEntity.badRequest().body(null); // Category ID is required
            }
            Category existingCategory = categoryService.getCategory(quiz.getCategory().getCid());
            if (existingCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Category not found
            }
            quiz.setCategory(existingCategory); // Set the managed category object
            
            Quiz addedQuiz = this.quizService.addQuiz(quiz);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedQuiz);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update quiz
    // PUT http://localhost:8060/quiz/
    @PutMapping("/")
    public ResponseEntity<Quiz> updateQuiz(@Valid @RequestBody Quiz quiz) {
        try {
            if (quiz.getQid() == null) {
                return ResponseEntity.badRequest().build(); // Quiz ID is required for update
            }
            // Ensure the category linked to the quiz exists, if provided
            if (quiz.getCategory() != null && quiz.getCategory().getCid() != null) {
                 Category existingCategory = categoryService.getCategory(quiz.getCategory().getCid());
                 if (existingCategory == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Category not found
                 }
                 quiz.setCategory(existingCategory); // Set the managed category object
            }
            Quiz updatedQuiz = this.quizService.updateQuiz(quiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all quizzes (for admin panel)
    // GET http://localhost:8060/quiz/
    @GetMapping("/")
    public ResponseEntity<List<Quiz>> getQuizzes() {
        try {
            List<Quiz> quizzes = this.quizService.getQuizzes();
            if (quizzes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get single quiz by ID
    // GET http://localhost:8060/quiz/{qId}
    @GetMapping("/{qId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable("qId") Long qId) {
        try {
            Quiz quiz = this.quizService.getQuiz(qId);
            if (quiz == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete quiz by ID
    // DELETE http://localhost:8060/quiz/{qId}
    @DeleteMapping("/{qId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable("qId") Long qId) {
        try {
            this.quizService.deleteQuiz(qId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get quizzes by category (for admin to see quizzes in a category)
    // GET http://localhost:8060/quiz/category/{cid}
    @GetMapping("/category/{cid}")
    public ResponseEntity<List<Quiz>> getQuizzesOfCategory(@PathVariable("cid") Long cid) {
        try {
            Category category = this.categoryService.getCategory(cid);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<Quiz> quizzes = this.quizService.getQuizzesOfCategory(category);
            if (quizzes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get active quizzes (for user panel)
    // GET http://localhost:8060/quiz/active
    @GetMapping("/active")
    public ResponseEntity<List<Quiz>> getActiveQuizzes() {
        try {
            List<Quiz> activeQuizzes = this.quizService.getActiveQuizzes();
            if (activeQuizzes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(activeQuizzes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get active quizzes by category (for user panel)
    // GET http://localhost:8060/quiz/category/active/{cid}
    @GetMapping("/category/active/{cid}")
    public ResponseEntity<List<Quiz>> getActiveQuizzesOfCategory(@PathVariable("cid") Long cid) {
        try {
            Category category = this.categoryService.getCategory(cid);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<Quiz> activeQuizzes = this.quizService.getActiveQuizzesOfCategory(category);
            if (activeQuizzes.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(activeQuizzes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
