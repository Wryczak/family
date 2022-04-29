package com.example.family.web;

import com.example.family.data.FamilyRepository;
import com.example.family.data.MemberRepository;
import com.example.family.family.Family;
import com.example.family.family.Member;
import com.example.family.family.matureChecker;
import com.example.family.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class FamilyController implements matureChecker {

    private FamilyRepository familyRepository;
    private MemberRepository memberRepository;

    public FamilyController(FamilyRepository familyRepository, MemberRepository memberRepository) {
        this.familyRepository = familyRepository;
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

    @GetMapping("/current")
    public String createFamily(@ModelAttribute Family family) {

        return "family";
    }

    @GetMapping("familyEditor")
    public String getCreateForm(@ModelAttribute Family family) {

        return "familyEditor";
    }

    @GetMapping("removing")
    public String removeLastMember(@ModelAttribute Family family) {

        return "removing";
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

        log.info("    --- Family Completed");
        familyRepository.saveAndFlush(family);

        sessionStatus.setComplete();
        return "/well";
    }

    private String checkFamilyMembersByMaturity(Family family, int nrOfMember, Member.Mature mature) {
        if (nrOfMember > checkMature(mature, family)) {
            log.info("    --- addAnother " + mature);
            return "family";
        }
        if (checkMature(mature, family) > nrOfMember) {
            System.out.println("I`m removing last " + mature);
            Long id = getMemberIdFromSublist(mature,family);
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
        if (tempMember.isEmpty()){
            return null;
        }
        return tempMember.get(tempMember.size() - 1).getId();
    }
}
