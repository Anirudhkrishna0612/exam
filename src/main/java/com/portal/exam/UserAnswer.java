// src/main/java/com/portal/exam/UserAnswer.java
package com.portal.exam; // Assuming this is your single package

// Assuming other necessary imports like User, Question, Quiz, etc.
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAnswerId; // Example ID field

    // Link to User
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch for User
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Assuming User entity has 'id' as PK
    private User user;

    // Link to Quiz
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch for Quiz
    // CRITICAL FIX: Explicitly specify referencedColumnName to match Quiz's primary key
    @JoinColumn(name = "quiz_qid", referencedColumnName = "qid") // Ensure 'quiz_qid' is the column name for the FK, 'qid' for Quiz PK
    private Quiz quiz;

    // Link to Question
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch for Question
    @JoinColumn(name = "question_quesId", referencedColumnName = "quesId") // Assuming Question entity has 'quesId' as PK
    private Question question;

    private String submittedAnswer; // The answer submitted by the user for this question

    // Constructors, Getters, Setters
    public UserAnswer() {
    }

    public UserAnswer(User user, Quiz quiz, Question question, String submittedAnswer) {
        this.user = user;
        this.quiz = quiz;
        this.question = question;
        this.submittedAnswer = submittedAnswer;
    }

    public Long getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(Long userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSubmittedAnswer() {
        return submittedAnswer;
    }

    public void setSubmittedAnswer(String submittedAnswer) {
        this.submittedAnswer = submittedAnswer;
    }
}
