package com.example.bestme.regex;

public class userRegex {
    private static final Regex email = new Regex("123");
    private static final Regex nickname = new Regex("^([\\da-zA-Z가-힣\\-_]{2,50})$");
    private static final Regex password = new Regex("^({8,20})$");
}
