package com.example.family.Controllers;

import com.example.family.MainObjectsFamilyMemberDto.autocompleteList.ListWithDtoObject;
import com.example.family.MainObjectsFamilyMemberDto.Details;
import com.example.family.MainObjectsFamilyMemberDto.Member;
import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import com.example.family.services.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping({"/", "/findRelatives"})
public class SearchingController {

    private final MemberService memberService;
    private final UserDetailsService userDetailsService;
    private final FamilyService familyService;
    private Long idToFind;
    private Long option;
    private boolean status;

    public SearchingController(MemberService memberService, UserDetailsService userDetailsService, FamilyService familyService) {
        this.memberService = memberService;
        this.userDetailsService = userDetailsService;
        this.familyService = familyService;
    }

    @ModelAttribute(name = "memberDto")
    public MemberDto createMemberDtoAttribute() {
        return new MemberDto();
    }

    @ModelAttribute(name = "list")
    public ListWithDtoObject createListAttribute() {
        return new ListWithDtoObject();
    }


    @GetMapping
    public String main(Model model, Details memberId, Details split) {
        userDetailsService.detailsSet(model);
        model.addAttribute("list", new ListWithDtoObject());
        model.addAttribute("memberId", memberId);
        model.addAttribute("split", split);
        log.info("   --- Finding Relatives");

        Long id = idToFind;
        if (id != null) {
            split.setStatus(true);
            Member memberToUpdate = memberService.getMember(idToFind);
            String memberData = memberToUpdate.getName() + "   " +
                    memberToUpdate.getFamilyName();
            split.setText(memberData);
            model.addAttribute("split", split);
        }
        familyService.clear();

        return "findRelatives";
    }

    @PostMapping
    public String getTestForm(Model model, ListWithDtoObject list) {
        userDetailsService.detailsSet(model);
        Long id = list.getMemberDto().getId();
        idToFind = id;
        return "redirect:/findRelatives";
    }

    @PostMapping("find")
    public String findRel(Model model, Details memberId) {
        model.addAttribute("memberId", memberId);

        if (idToFind == null || idToFind == 0) {
            log.info("   --- Wrong Id");
            return "redirect:findRelatives";
        }
        String findRelatives = findRelativesChooseOne(memberId);
        if (findRelatives != null) return findRelatives;
        log.info("   --- Member not found");
        return "redirect:/index";
    }

    @GetMapping("relativesTable")
    public String getRelativesTable(Model model) {
        userDetailsService.detailsSet(model);

        if (!familyService.isDoIHaveFamily()) {
            return "modify/wellLog";
        }
        memberService.getRelativesListAndAddToModel(model, option, idToFind);
        clearData();
        return "relativesTable";
    }


    private String findRelativesChooseOne(Details memberId) {
        if (memberService.isMemberExist(idToFind)) {
            log.info("   --- Member found! looking for relatives");

            option = memberId.getId();

            if (option == null) {
                return "redirect:findRelatives";
            }
            if (option > 0 && option < 6) {
                return "redirect:/relativesTable";
            }
        }
        return null;
    }
    @GetMapping("findMember")
    public String findMemberIfIsPartOfFamily2(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);
        model.addAttribute("list", new ListWithDtoObject());
        model.addAttribute("userDetails", userDetails);

        return "findMember";
    }
    @PostMapping("findMember")
    public String findMemberIfIsPartOfFamilyPost(Model model, Details userDetails,ListWithDtoObject list) {
        userDetailsService.detailsSet(model);
        Long id = idToFind=list.getMemberDto().getId();
        if (id == null || id == 0) {

            userDetails.setThirdText("Błędne id");
            model.addAttribute("userDetails", userDetails);
            return "findMember";
        }

        if (memberService.isMemberExist(id)) {
            Member member = memberService.getMember(id);

            if (memberService.getMembers().contains(member)) {
                status = true;

                return "redirect:/searchResult";
            } else

                return "redirect:/searchResult";
        }
        userDetails.setThirdText("Osoba o takim Id nie istnieje.");
        model.addAttribute("userDetails", userDetails);
        return "findMember";
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
            if (member.getFather().getId() != null || member.getMother().getId()!=null) {
                memberService.getRelativesListAndAddToModel(model, 2L, idToFind);
            }
            memberService.createChildrenListForMemberById(idToFind, model);
        }

        if (!status) {
            userDetails.setStatus(false);
            model.addAttribute("userDetails", userDetails);
            userDetails.setText("Nie jesteś członkiem tej rodziny");
            model.addAttribute("userDetails", userDetails);
        }
        idToFind=null;

        return "searchResult";
    }

    @PostMapping("cancelForm")
    public String addNewMemberToFamilyFrame() {
        idToFind = null;
        return "redirect:/findRelatives";
    }

    private void clearData() {
        option = null;
        idToFind = null;
    }

}

