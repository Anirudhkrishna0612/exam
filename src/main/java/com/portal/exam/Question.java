// src/main/java/com/portal/exam/Question.java
package com.portal.exam;

// import com.fasterxml.jackson.annotation.JsonIgnore; // NO LONGER NEEDED FOR THIS FILE
import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quesId;

    @Column(length = 5000)
    private String content;

    // Removed: private String image;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private String answer;

    @Transient // This means this field will NOT be persisted in the database
    private String givenAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_qid", referencedColumnName = "qid") // Foreign key column name in questions table
    // *** CRITICAL CHANGE: REMOVED @JsonIgnore from here ***
    private Quiz quiz;

    // Constructors
    public Question() {
        // Initialize the quiz object to prevent NullPointerExceptions later if not set
        this.quiz = new Quiz();
    }

    // Constructor updated to reflect removal of 'image' and inclusion of 'givenAnswer'
    public Question(String content, String option1, String option2, String option3, String option4, String answer, String givenAnswer, Quiz quiz) {
        this.content = content;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.givenAnswer = givenAnswer;
        this.quiz = quiz;
    }

    // Getters and Setters
    public Long getQuesId() {
        return quesId;
    }

    public void setQuesId(Long quesId) {
        this.quesId = quesId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public void setGivenAnswer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "Question{" +
                "quesId=" + quesId +
                ", content='" + content + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", answer='" + answer + '\'' +
                ", givenAnswer='" + givenAnswer + '\'' +
                // Crucially, when printing, check if quiz is null to prevent NPE
                ", quiz=" + (quiz != null && quiz.getQid() != null ? quiz.getQid() : "null") +
                '}';
    }
}
