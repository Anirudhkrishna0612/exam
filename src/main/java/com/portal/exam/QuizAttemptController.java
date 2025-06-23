// src/main/java/com/portal/exam/QuizAttemptController.java (Assuming this is the file with the error)
package com.portal.exam; // Confirmed: This is your single package

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // For getting the authenticated user's name

// Imports for your custom classes (assuming all are directly in com.portal.exam)
import com.portal.exam.QuizSubmissionRequest; // **CRITICAL FIX: Import top-level class**
import com.portal.exam.QuizResult;          // **CRITICAL FIX: Import top-level class**

// Assuming you have UserNotFoundException if you're throwing it
// import com.portal.exam.exception.UserNotFoundException;


@RestController
@CrossOrigin("*")
@RequestMapping("/quiz-attempt") // Assuming this is the correct request mapping
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    // You might also need UserService if you're directly using it here
    // @Autowired
    // private UserService userService;

    /**
     * Endpoint for users to submit their quiz answers for grading.
     * Accessible by authenticated users (NORMAL or ADMIN).
     * POST http://localhost:8060/quiz-attempt/submit
     * @param submissionRequest DTO containing quiz ID and user answers.
     * @param principal The authenticated user's principal object.
     * @return ResponseEntity with the QuizResult if successful, or an error status.
     */
    @PostMapping("/submit")
    public ResponseEntity<QuizResult> submitQuiz(@RequestBody QuizSubmissionRequest submissionRequest, Principal principal) {
        try {
            String username = principal.getName(); // Get username from authenticated user
            QuizResult result = quizAttemptService.submitAndGradeQuiz(submissionRequest, username);
            return ResponseEntity.ok(result);
        } catch (Exception e) { // Catch more specific exceptions if possible (e.g., UserNotFoundException, QuizNotFoundException)
            e.printStackTrace();
            // Return appropriate error status based on exception type
            return ResponseEntity.status(500).build(); // Internal Server Error for generic exceptions
        }
    }
}
