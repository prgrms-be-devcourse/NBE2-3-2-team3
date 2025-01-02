package com.example.bestme.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResultViewController {

    //퍼스널컬러 진단 페이지
    @GetMapping("/personal")
    public String personal() {
        return "personal";
    }

    //퍼스널컬러 결과 페이지
    @GetMapping("/personal/result")
    public String personalResult() {
        return "personal_result";
    }

    @GetMapping("/style/guide")
    public String guide() {
        return "guide";
    }

    @GetMapping("/style/guide/items")
    public String guideItem() {
        return "guide_item";
    }
}
