package com.example.family.web;

import com.example.family.Interfaces.Details;
import com.example.family.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class HomeController  {

    private final MemberService memberService;

    @Autowired
    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getHomeView(Model model,Details details) {
        memberService.detailsSet(details,model);
        return "index";
    }
}
