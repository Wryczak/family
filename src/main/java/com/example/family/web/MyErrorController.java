package com.example.family.web;

import com.example.family.Interfaces.Details;
import com.example.family.services.MemberService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {
    private final MemberService memberService;

    public MyErrorController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping("/error")
    public String handleError(Model model, Details details) {
        memberService.detailsSet(details, model);
        return "error";
    }
}