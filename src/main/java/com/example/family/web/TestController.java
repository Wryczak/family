package com.example.family.web;

import com.example.family.Interfaces.UsernameGetter;
import com.example.family.Interfaces.Details;
import com.example.family.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("test")
public class TestController implements UsernameGetter {
    private final MemberService memberService;

    public TestController( MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getTestView( Model model,Details details) {
        memberService.detailsSet(details,model);
        memberService.detailsSetter(model, getUsername());
        return "test";
    }
}
