package com.example.family.Controllers;

import com.example.family.Interfaces.AgeCalculator;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.MainObjectsFamilyMemberDto.Details;
import com.example.family.MainObjectsFamilyMemberDto.Family;
import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import com.example.family.services.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Controller
@Slf4j
@RequestMapping("test")
public class TestController implements UsernameGetter, AgeCalculator {
    private final MemberService memberService;
    private final FamilyService familyService;

    private final UserDetailsService userDetailsService;
    private Long idToModify;

    private Long idToFind;
    private boolean status;

    public TestController(MemberService memberService, FamilyService familyService, UserDetailsService userDetailsService) {
        this.memberService = memberService;
        this.familyService = familyService;
        this.userDetailsService = userDetailsService;
    }

    @ModelAttribute(name = "family")
    public Family createFamilyAttribute() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMemberAttribute() {
        return new Member();
    }

    @ModelAttribute(name = "memberDto")
    public MemberDto createMemberDtoAttribute() {
        return new MemberDto();
    }

    @GetMapping
    public String getTestView(Model model) {
        userDetailsService.detailsSet(model);
        memberService.getFamilyMembersDtoListAndAddToModel(model);
        if (!familyService.isDoIHaveFamily()) {
            return "wellLog";
        }
        return "test";
    }

    @PostMapping
    public String getTestForm(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);
        memberService.getFamilyMembersDtoListAndAddToModel(model);

        idToModify = userDetails.getId();

        return "test/addRelatives";
    }

    @GetMapping("addRelatives")
    public String getAddMemberView(Model model) {
        userDetailsService.detailsSet(model);
        if (!familyService.isDoIHaveFamily()) {
            return "wellLog";
        }
        return "test/addRelatives";
    }

    @PostMapping("addRelatives")
    public String addNewMemberToFamily(Model model, @Valid Member member, Errors errors) {
        userDetailsService.detailsSet(model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "test/addRelatives";
        }

        log.info("    --- Creating new family member");
        createRelatives(member);
        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("searchResult")
    public String viewFamilyByUser(Model model) {
        userDetailsService.detailsSet(model);

        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);

        if (status) {

            Member member = memberService.getMember(idToFind);
            userDetails.setText(member.getName() + " " + member.getFamilyName());
            model.addAttribute("userDetails", userDetails);
            memberService.getRelativesListAndAddToModel(model, 2L, idToFind);
           memberService.createChildrenListForMemberById(idToFind,model);
            }

        if (!status) {
            userDetails.setStatus(false);
            model.addAttribute("userDetails", userDetails);
            userDetails.setText("Nie jesteś członkiem tej rodziny");
            model.addAttribute("userDetails", userDetails);
        }

        return "test/searchResult";
    }

    @GetMapping("findMember")
    public String findMemberIfIsPartOfFamily(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);

        model.addAttribute("userDetails", userDetails);
        return "test/findMember";
    }

    @PostMapping("findMember")
    public String findMemberIfIsPartOfFamilyPost(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);
        Long id = idToFind = userDetails.getId();
        if (id == null || id == 0) {

            userDetails.setThirdText("Błędne id");
            model.addAttribute("userDetails", userDetails);
            return "test/findMember";

        }

        if (memberService.isMemberExist(id)) {
            Member member = memberService.getMember(id);

            if (memberService.getMembers().contains(member)) {
                status = true;

                return "redirect:/test/searchResult";
            } else


                return "redirect:/test/searchResult";
        }
        userDetails.setThirdText("Osoba o takim Id nie istnieje.");
        model.addAttribute("userDetails", userDetails);
        return "test/findMember";
    }

    private void createRelatives(Member member) {

        Member addRelativesToThisMember = memberService.getMember(idToModify);

        Long option = (member.getUserid());
        memberService.createMember(member);

        if (option == null) {
            addRelativesToThisMember.setFatherId(member.getId());
            return;
        }
        if (option == 1 && member.getFatherId() == null) {
            addRelativesToThisMember.setFatherId(member.getId());
        }
        if (option == 2 && member.getMatherId() == null) {
            addRelativesToThisMember.setMatherId(member.getId());
        }
        if (option == 3) {
            member.setFatherId(idToModify);
        }
    }
}
