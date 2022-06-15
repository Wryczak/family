package com.example.family.memberControllers;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.MaturityChecker;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.family.*;

import com.example.family.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/create")
@SessionAttributes("family")
@Slf4j
public class MemberController implements MaturityChecker, UsernameGetter {
    private final MemberService memberService;
    private boolean shouldBeViewRedirected;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
    public String showCreateForm(Model model, Details details) {
        memberService.detailsSet(details, model);
        log.info("   --- Creating Member");
        return "create";
    }

    @GetMapping("addYourself")
    public String showCreateFormForYourself(Model model, Details details) {
        memberService.detailsSet(details, model);

        if (memberService.isDoIHaveFamily()) {
            return "redirect:/modify/getMyFamilyAfterLog";

        }
        if (memberService.isAnyMemberCreatedByUser()) {
            shouldBeViewRedirected = false;
            log.info("   --- Creating You");
            return "addYourself";
        }

        if (shouldBeViewRedirected) {
            return "redirect:/family/familyEditor";
        }

        log.info("   --- Creating You");
        return "addYourself";
    }

    @PostMapping("addYourself")
    public String createYourself(
            @Valid Member member, Errors errors, Family family, Details details, Model model) {
        memberService.detailsSet(details, model);


        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "addYourself";
        }

        log.info("    --- Saving Yours data");

        memberService.newMemberSaver(member, family);
        shouldBeViewRedirected = true;
        return "redirect:/family/familyEditor";
    }

    @PostMapping()
    public String processCreate(
            @Valid Member member, Errors errors, Family family,Details details, Model model) {
        memberService.detailsSet(details, model);
        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "create";
        }
        log.info("    --- Saving Member");

        memberService.newMemberSaver(member, family);

        return "redirect:/family/current";
    }
}

