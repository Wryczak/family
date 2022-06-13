package com.example.family.web;

import com.example.family.data.UserRepository;
import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.DetailsSet;
import com.example.family.family.MemberDto;
import com.example.family.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping("test")
public class TestController implements DetailsSet {
    private final MemberService memberService;
    private final UserRepository userRepository;

    public TestController(UserRepository userRepository, MemberService memberService) {
        this.memberService = memberService;
        this.userRepository = userRepository;
    }

    @ModelAttribute(name = "memberDto")
    public MemberDto createMemberDto() {
        return new MemberDto();
    }


    @GetMapping
    public String getTestView(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username, Details details) {
        detailsSet(userRepository, username, details, model);
        memberService.detailsSetter(model, username);
        return "test";
    }

}
