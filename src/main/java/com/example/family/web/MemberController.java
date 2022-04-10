package com.example.family.web;

import com.example.family.data.MemberRepository;
import com.example.family.family.Family;

import com.example.family.family.Member;
import com.example.family.family.Member.Mature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/create")
@SessionAttributes("family")
@Slf4j
public class MemberController {

    private MemberRepository memberRepository;

    @Autowired
    public MemberController(
            MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @ModelAttribute(name = "family")
    public Family createFamily() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMember() {
        return new Member();
    }

    @GetMapping
    public String showCreateForm() {
        log.info("   --- Creating Member");
        return "create";
    }

    @GetMapping("addYourself")
    public String showCreateFormForYourself() {

        log.info("   --- Creating You");

        return "addYourself";
    }

    @PostMapping("addYourself")
    public String createYourself(
            @Valid Member member,
            Errors errors,
            @ModelAttribute Family family) {


        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "addYourself";
        }

        log.info("    --- Saving Yours data");

        member.setMature(checkMaturity(member));
         memberRepository.save(member);
        family.addFamilyMember(member);

        return "redirect:/family/familyEditor";
    }

    @PostMapping()
    public String processCreate(
            @Valid Member member,
            Errors errors,
            @ModelAttribute Family family) {

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "create";
        }
        log.info("    --- Saving Member");
        Member saved = member;
        member.setMature(checkMaturity(saved));
        saved = memberRepository.save(member);
        family.addFamilyMember(saved);

        return "redirect:/family/current";
    }

    public Mature checkMaturity(Member member) {
        int age=member.getAge();
        if (age >= 0 && age < 4) {
            return Mature.INFANT;
        } else if (age >= 4 && age <= 16) {
            return Mature.CHILD;
        } else
            return Mature.ADULT;
    }

}

