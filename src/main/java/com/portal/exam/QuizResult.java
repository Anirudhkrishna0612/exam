// src/main/java/com/portal/exam/QuizResult.java
package com.portal.exam; // Assuming this is your single package

// This class will represent the result of a quiz evaluation
public class QuizResult {
    private double marksGot;
    private int correctAnswers;
    private int attempted;
    private int totalQuestions;
    // You can add more fields here like user, quiz, timestamp etc.

    // Constructor
    public QuizResult() {
    }

    public QuizResult(double marksGot, int correctAnswers, int attempted, int totalQuestions) {
        this.marksGot = marksGot;
        this.correctAnswers = correctAnswers;
        this.attempted = attempted;
        this.totalQuestions = totalQuestions;
    }

    // Getters and Setters
    public double getMarksGot() { return marksGot; }
    public void setMarksGot(double marksGot) { this.marksGot = marksGot; }
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    public int getAttempted() { return attempted; }
    public void setAttempted(int attempted) { this.attempted = attempted; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
}
