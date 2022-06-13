package com.example.family.memberControllers;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.DetailsSet;
import com.example.family.Interfaces.MaturityChecker;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.*;

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
public class MemberController implements DetailsSet, MaturityChecker {

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
    public String showCreateForm(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username, Details details) {
        detailsSet(userRepository, username, details, model);
        log.info("   --- Creating Member");
        return "create";
    }

    @GetMapping("addYourself")
    public String showCreateFormForYourself(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);

        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            return "redirect:/modify/getMyFamilyAfterLog";

        }
        if (memberRepository.findByUserid(userRepository.findByUsername(username).getId()) == null) {
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
            @Valid Member member,Errors errors,
            @ModelAttribute Family family, @CurrentSecurityContext(expression = "authentication?.name")
            String username,Details details,Model model) {
        detailsSet(userRepository,username,details,model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "addYourself";
        }

        log.info("    --- Saving Yours data");

        newMemberSaver(member,family,username);
        shouldBeViewRedirected = true;
        return "redirect:/family/familyEditor";
    }

    @PostMapping()
    public String processCreate(
            @Valid Member member, Errors errors, @ModelAttribute Family family,
            @CurrentSecurityContext(expression = "authentication?.name")String username) {
        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "create";
        }
        log.info("    --- Saving Member");

        newMemberSaver(member, family, username);

        return "redirect:/family/current";
    }

    private void newMemberSaver(Member member, Family family, String username) {
        member.setMature(checkMaturity(member));
        member.setUserid(userRepository.findByUsername(username).getId());
        member = memberRepository.saveAndFlush(member);
        family.addFamilyMember(member);
    }
}

