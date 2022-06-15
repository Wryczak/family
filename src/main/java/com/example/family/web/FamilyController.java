package com.example.family.web;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.family.*;
import com.example.family.security.User;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import com.example.family.services.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/family")
@SessionAttributes("family")
@Slf4j
public class FamilyController implements UsernameGetter {

    private final FamilyService familyService;
    private final MemberService memberService;

    private final ValidatorService validatorService;
    private boolean shouldBeViewRedirected;


    @Autowired
    public FamilyController(FamilyService familyService, MemberService memberService, ValidatorService validatorService) {
        this.familyService = familyService;
        this.memberService = memberService;
        this.validatorService = validatorService;
    }

    @ModelAttribute(name = "family")
    public Family createFamily() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMember() {
        return new Member();
    }

    @GetMapping("/current")
    public String createFamily(@ModelAttribute Family family, Model model, Details details) {
        memberService.detailsSet(details,model);
        return "family";
    }

    @GetMapping("familyEditor")
    public String getCreateForm(@ModelAttribute Family family, Model model, Details details) {
        memberService.detailsSet(details,model);

        if (shouldBeViewRedirected) {
            return "redirect:/family/current";
        }

        return "familyEditor";
    }

    @GetMapping("removing")
    public String removeLastMember(@ModelAttribute Family family, Model model, Details details) {
        memberService.detailsSet(details,model);

        return "removing";
    }

    @PostMapping
    public String processFamily(@Valid Family family, Errors errors, Model model, Details details) {
        memberService.detailsSet(details,model);

        if (errors.hasErrors()) {
            return "familyEditor";
        }

        log.info("    --- Family Saved");
        familyService.createFamily(family);
        shouldBeViewRedirected=true;

        return "family";
    }

    @GetMapping("/success")
    public String viewFamily(Model model, Details details) {
        memberService.detailsSet(details,model);

        familyService.getFamilyNumberAndAddToModel(model);

        return "success";
    }


    @PostMapping("submit")
    public String submitFamily(@Valid Family family, Errors errors, SessionStatus sessionStatus,
                               Model model, Details details) {
        memberService.detailsSet(details,model);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (errors.hasErrors()) {
            return "familyEditor";
        }
        family.setUser(user);

        String memberValidateRedirect = validatorService.validateFamily(family);
        if (memberValidateRedirect != null) return memberValidateRedirect;

        familyService.setFamilyDetails(family);
        shouldBeViewRedirected=false;
        log.info("    --- Family Completed");
        familyService.createFamily(family);

        sessionStatus.setComplete();
        return "redirect:/family/success";
    }
}
