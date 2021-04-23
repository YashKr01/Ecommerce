package com.shopping.bloom.model;

public class FaqModel {
    String question, solution;

    public FaqModel(String question, String solution) {
        this.question = question;
        this.solution = solution;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
