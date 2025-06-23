// src/main/java/com/portal/exam/Question.java
package com.portal.exam; // Confirmed: This is your single package

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn; // Make sure this is imported
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quesId;

    @Column(length = 5000)
    private String content;

    private String image;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    private String answer;

    @ManyToOne(fetch = FetchType.EAGER)
    // CRITICAL FIX: Explicitly specify referencedColumnName to match Quiz's primary key
    @JoinColumn(name = "quiz_qid", referencedColumnName = "qid") // Ensure 'quiz_qid' is the column you want for the FK, 'qid' for the PK
    @JsonIgnore
    private Quiz quiz;

    // Constructors
    public Question() {
    }

    public Question(String content, String image, String option1, String option2, String option3, String option4, String answer, Quiz quiz) {
        this.content = content;
        this.image = image;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
               ", image='" + image + '\'' +
               ", option1='" + option1 + '\'' +
               ", option2='" + option2 + '\'' +
               ", option3='" + option3 + '\'' +
               ", option4='" + option4 + '\'' +
               ", answer='" + answer + '\'' +
               ", quiz=" + (quiz != null ? quiz.getQid() : "null") +
               '}';
    }
}
