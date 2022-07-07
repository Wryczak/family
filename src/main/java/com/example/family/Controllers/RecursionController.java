package com.example.family.Controllers;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recursion")
@Slf4j
public class RecursionController {
    private final MemberService memberService;
    private final FamilyService familyService;

    private final UserDetailsService userDetailsService;
    private Long idToFind;
    private Long option;

    public RecursionController(MemberService memberService,
                               FamilyService familyService, UserDetailsService userDetailsService) {
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

    @GetMapping("findRelatives")
    public String getTestView(Model model, Details memberId) {
        userDetailsService.detailsSet(model);

        model.addAttribute("memberId", memberId);
        log.info("   --- Finding Relatives");

        userDetailsService.detailsSet(model);

        familyService.clear();
        clearData();

        return "recursion/findRelatives";
    }

    @PostMapping("findRelatives")
    public String getTestForm(Model model, Details memberId) {
        userDetailsService.detailsSet(model);

        idToFind = memberId.getIdToFind();
        if (idToFind == null || idToFind == 0) {
            log.info("   --- Wrong Id");
            return "redirect:/recursion/findRelatives";
        }

        String findRelatives = findRelativesChooseOne(memberId);
        if (findRelatives != null) return findRelatives;

        log.info("   --- Member not found");
        return "redirect:/recursion/findRelatives";
    }

    @GetMapping("relativesTable")
    public String getRelativesTable(Model model) {
        userDetailsService.detailsSet(model);

        if (!familyService.isDoIHaveFamily()) {
            return "modify/wellLog";
        }
        memberService.getRelativesListAndAddToModel(model, option, idToFind);

        return "recursion/relativesTable";
    }

    private String findRelativesChooseOne(Details memberId) {
        if (memberService.isMemberExist(idToFind)) {
            log.info("   --- Member found! looking for relatives");

            option = memberId.getId();

            if (option == null) {
                System.out.println(option);
                return "redirect:/recursion/findRelatives";
            }
            if (option>0 && option<6) {
                return "redirect:/recursion/relativesTable";
            }
        }
        return null;
    }

    private void clearData() {
        option = null;
        idToFind = null;
    }
}
