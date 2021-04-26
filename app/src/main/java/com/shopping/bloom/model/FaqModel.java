package com.shopping.bloom.model;

public class FaqModel {
    String header, question, solution;

    public FaqModel(String header, String question, String solution) {
        this.header = header;
        this.question = question;
        this.solution = solution;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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
