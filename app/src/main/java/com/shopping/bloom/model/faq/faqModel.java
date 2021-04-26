package com.shopping.bloom.model.faq;

public class faqModel {
    String name, question, solution;

    public faqModel(String name, String question, String solution) {
        this.name = name;
        this.question = question;
        this.solution = solution;
    }

    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }

    public String getSolution() {
        return solution;
    }
}
