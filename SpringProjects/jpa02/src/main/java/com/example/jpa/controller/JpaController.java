package com.example.jpa.controller;


@RestController
public class JpaController {

    @RequestMapping
    public String create() {
        return "<h3>create</h3>";
    }
}
