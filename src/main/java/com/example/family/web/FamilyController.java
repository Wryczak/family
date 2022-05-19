package com.example.family.web;

import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.*;
import com.example.family.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/family")
@SessionAttributes("family")
@Slf4j
public class FamilyController implements matureChecker, DetailsSet {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    private int numberOfViewCalls;

    public FamilyController(FamilyRepository familyRepository, MemberRepository memberRepository, UserRepository userRepository) {
        this.familyRepository = familyRepository;
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

    @GetMapping("/current")
    public String createFamily(@ModelAttribute Family family, Model model,
                               @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String currentFamilyView = "family";
        return getMenuDependsOnAuthentication(currentFamilyView, model, username);
    }

    @GetMapping("familyEditor")
    public String getCreateForm(@ModelAttribute Family family, Model model,
                                @CurrentSecurityContext(expression = "authentication?.name") String username) {

        if (numberOfViewCalls != 0) {
            return "redirect:/family/current";
        }

        String familyEditorView = "familyEditor";
        return getMenuDependsOnAuthentication(familyEditorView, model, username);
    }

    @GetMapping("removing")
    public String removeLastMember(@ModelAttribute Family family, Model model,
                                   @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String removeFamilyMemberView = "removing";
        return getMenuDependsOnAuthentication(removeFamilyMemberView, model, username);
    }

    @PostMapping
    public String processFamily(@Valid Family family, Errors errors,
                                Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {

        if (errors.hasErrors()) {
            return "familyEditor";
        }

        log.info("    --- Family Saved");
        familyRepository.save(family);
        numberOfViewCalls = 1;

        String familyView = "family";
        return getMenuDependsOnAuthentication(familyView, model, username);
    }

    @GetMapping("/success")
    public String viewFamily(@ModelAttribute Family family, Model model,
                             @CurrentSecurityContext(expression = "authentication?.name") String username) {

        String success = "success";
        return getMenuDependsOnAuthentication(success, model, username);
    }

    @PostMapping("submit")
    public String submitFamily(@Valid Family family, Errors errors, SessionStatus sessionStatus,
                               Model model, @CurrentSecurityContext(expression = "authentication?.name")
                               String username) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (errors.hasErrors()) {
            return "familyEditor";
        }
        family.setUser(user);

        String memberValidateRedirect;
        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfInfants(), Member.Mature.INFANT);
        if (memberValidateRedirect != null) {
            return memberValidateRedirect;
        }

        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfChildren(), Member.Mature.CHILD);
        if (memberValidateRedirect != null) {
            return memberValidateRedirect;
        }

        memberValidateRedirect = checkFamilyMembersByMaturity(family, family.getNrOfAdults(), Member.Mature.ADULT);
        if (memberValidateRedirect != null) {
            return memberValidateRedirect;
        }

        System.out.println(username);
        userRepository.findByUsername(username).setDoIHaveFamily(true);
        userRepository.findByUsername(username).setMyFamilyNr(family.getId());
        numberOfViewCalls = 0;
        setIdToRemoveForAllFamilyMembers(family);
        log.info("    --- Family Completed");
        familyRepository.saveAndFlush(family);

        sessionStatus.setComplete();
        String success = "success";
        return getMenuDependsOnAuthentication(success, model, username);
    }

    private String checkFamilyMembersByMaturity(Family family, int nrOfMember, Member.Mature mature) {
        if (nrOfMember > checkMature(mature, family)) {
            log.info("    --- addAnother " + mature);
            return "family";
        }
        if (checkMature(mature, family) > nrOfMember) {
            System.out.println("I`m removing last " + mature);
            Long id = getMemberIdFromSublist(mature, family);
            family.deleteFamilyMember(memberRepository.getById(id));
            memberRepository.deleteById(id);

            log.info("    --- ToMany " + mature + " --deleting the last one");
            return "removing";
        }
        return null;
    }

    @Override
    public int checkMature(Member.Mature mature, Family family) {
        int nrOfMembers = 0;
        List<Member> members = family.getMembers();
        for (Member member : members) {
            if (member.getMature() == mature) {
                nrOfMembers++;
            }
        }
        return nrOfMembers;
    }

    private Long getMemberIdFromSublist(Member.Mature mature, Family family) {
        List<Member> members = family.getMembers();
        List<Member> tempMember = new ArrayList<>();
        for (Member member : members) {
            if (member.getMature() == mature) {
                tempMember.add(member);
            }
        }
        if (tempMember.isEmpty()) {
            return null;
        }
        return tempMember.get(tempMember.size() - 1).getId();
    }

    private void setIdToRemoveForAllFamilyMembers(Family family) {

        List<Member> members = family.getMembers();
        for (Member member : members
        ) {member.setIdToRemove(member.getId());
            memberRepository.save(member);

        }
    }
}
