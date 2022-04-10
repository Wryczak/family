package com.example.family.web;

import com.example.family.data.FamilyRepository;
import com.example.family.family.Family;
import com.example.family.family.Member;
import com.example.family.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/family")
@SessionAttributes("family")
@Slf4j
public class FamilyController {

    private FamilyRepository familyRepository;

    public FamilyController(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
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
    public String createFamily(@ModelAttribute Family family) {

        return "family";
    }

    @GetMapping("familyEditor")
    public String getCreateForm(@ModelAttribute Family family) {

        return "familyEditor";
    }

    @PostMapping
    public String processFamily(@Valid Family family, Errors errors) {


        if (errors.hasErrors()) {
            return "familyEditor";
        }

        log.info("    --- Family Saved");
        familyRepository.save(family);
        return "family";
    }

    @GetMapping("/well")
    public String viewFamily(@ModelAttribute Family family) {
        return "/well";
    }

    @PostMapping("submit")
    public String submitFamily(@Valid Family family, Errors errors, SessionStatus sessionStatus) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (errors.hasErrors()) {
            return "familyEditor";
        }
        family.setUser(user);


        if (family.getInfants()!=family.getNrOfInfants()){
            log.info("    --- addAnotherInfant");
            return "family";
        }
        if (family.getChildren() !=family.getNrOfChildren()){
            log.info("    --- addAnotherChild");
            return "family";
        }
        if (family.getAdults() != family.getNrOfAdults()){
            log.info("    --- addAnotherAdult");
            return "family";
        }
            log.info("    --- Family Completed");
        System.out.println(family.getInfants());
        familyRepository.save(family);
//
        sessionStatus.setComplete();
        return "/well";
    }

}
