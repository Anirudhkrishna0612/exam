// src/main/java/com/portal/exam/Quiz.java
package com.portal.exam; // Confirmed: This is your single package

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid; // Use qid as confirmed consistently

    private String title;

    @Column(length = 5000) // Description can be long
    private String description;

    // CRITICAL FIX: Changed type from String to Integer
    private Integer maxMarks;
    private Integer numberOfQuestions;

    private boolean active = false; // Default to false

    @ManyToOne(fetch = FetchType.EAGER) // EAGERly fetch category with quiz
    private Category category; // Category also needs to be in com.portal.exam

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore // To prevent infinite recursion when serializing Quiz
    private Set<Question> questions = new HashSet<>(); // Questions also need to be in com.portal.exam

    // Constructors
    public Quiz() {
    }

    public Quiz(Long qid, String title, String description, Integer maxMarks, Integer numberOfQuestions, boolean active, Category category) {
        this.qid = qid;
        this.title = title;
        this.description = description;
        this.maxMarks = maxMarks;
        this.numberOfQuestions = numberOfQuestions;
        this.active = active;
        this.category = category;
    }

    // Getters and Setters
    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxMarks() { // Now returns Integer
        return maxMarks;
    }

    public void setMaxMarks(Integer maxMarks) { // Now accepts Integer
        this.maxMarks = maxMarks;
    }

    public Integer getNumberOfQuestions() { // Now returns Integer
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) { // Now accepts Integer
        this.numberOfQuestions = numberOfQuestions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
               "qid=" + qid +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", maxMarks=" + maxMarks +
               ", numberOfQuestions=" + numberOfQuestions +
               ", active=" + active +
               ", category=" + (category != null ? category.getCid() : "null") +
               '}';
    }
}
