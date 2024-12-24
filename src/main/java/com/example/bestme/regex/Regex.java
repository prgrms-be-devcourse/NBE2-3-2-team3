package com.example.bestme.regex;

public class Regex {
    public final String expression;

    public Regex(String expression) {
        this.expression = expression;
    }
    
    public boolean regexTest(String text) {
        return text.matches(this.expression);
    }
}
