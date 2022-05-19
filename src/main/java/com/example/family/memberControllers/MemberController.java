package com.example.family.memberControllers;

import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Details;
import com.example.family.family.DetailsSet;
import com.example.family.family.Family;

import com.example.family.family.Member;
import com.example.family.family.Member.Mature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/create")
@SessionAttributes("family")
@Slf4j
public class MemberController implements DetailsSet {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private boolean shouldBeViewRedirected;

    @Autowired
    public MemberController(
            MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
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
    public String showCreateForm(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        log.info("   --- Creating Member");
        String create = "create";
        return getMenuDependsOnAuthentication(create, model, username);
    }

    @GetMapping("error")
    public String redirectIfUserHaveFamily(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        log.info("   --- You already have family");
        String error = "404";

        return getMenuDependsOnAuthentication(error, model, username);
    }

    @GetMapping("addYourself")
    public String showCreateFormForYourself(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username) {

        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            String redirect ="redirect:/wellLog";
            return getMenuDependsOnAuthentication(redirect, model, username);
        }
        if (memberRepository.findByUserid(userRepository.findByUsername(username).getId())==null){
            shouldBeViewRedirected=false;
            log.info("   --- Creating You");
            String addYourself = "addYourself";
            return getMenuDependsOnAuthentication(addYourself, model, username);

        }

        if (shouldBeViewRedirected) {
            String redirect ="redirect:/family/familyEditor";
            return getMenuDependsOnAuthentication(redirect, model, username);
        }

            log.info("   --- Creating You");
        String addYourself = "addYourself";
        return getMenuDependsOnAuthentication(addYourself, model, username);
    }

    @PostMapping("addYourself")
    public String createYourself(
            @Valid Member member,
            Errors errors,
            @ModelAttribute Family family, @CurrentSecurityContext(expression = "authentication?.name")
            String username) {

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "addYourself";
        }

        log.info("    --- Saving Yours data");

        member.setMature(checkMaturity(member));
        member.setUserid(userRepository.findByUsername(username).getId());
        memberRepository.saveAndFlush(member);
        family.addFamilyMember(member);
        shouldBeViewRedirected=true;
        return "redirect:/family/familyEditor";
    }

    @PostMapping()
    public String processCreate(
            @Valid Member member, Errors errors, @ModelAttribute Family family,
            @CurrentSecurityContext(expression = "authentication?.name")
            String username) {
        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "create";
        }
        log.info("    --- Saving Member");
        Member saved = member;
        member.setMature(checkMaturity(saved));
        member.setUserid(userRepository.findByUsername(username).getId());
        saved = memberRepository.saveAndFlush(member);
        family.addFamilyMember(saved);

        return "redirect:/family/current";
    }

    private Mature checkMaturity(Member member) {
        int age = member.getAge();
        if (age >= 0 && age < 4) {
            return Mature.INFANT;
        } else if (age >= 4 && age <= 16) {
            return Mature.CHILD;
        } else
            return Mature.ADULT;
    }

}

