package com.example.bestme.controller.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityViewController {

    @RequestMapping("/community/createBoardTest")
    public String test() {
        return "createBoardTest";
    }
}
