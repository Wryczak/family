package com.example.family.Controllers;

import com.example.family.Interfaces.DtoConverter;
import com.example.family.MainObjectsFamilyMemberDto.Details;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.MainObjectsFamilyMemberDto.*;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import com.example.family.services.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@Transactional
@Controller
@RequestMapping("/modify")
@Slf4j
public class MemberDataController implements UsernameGetter, DtoConverter {
    private final MemberService memberService;
    private final FamilyService familyService;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;
    private Long idToModify;
    private Long idToFind;

    @Autowired
    public MemberDataController(MemberService memberService, FamilyService familyService,
                                UserDetailsService userDetailsService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.familyService = familyService;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
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

    @GetMapping("addFamily")
    public String addFamily(Model model) {
        userDetailsService.detailsSet(model);
        if (familyService.isDoIHaveFamily()) {
            return "redirect:/modify/getMyFamilyAfterLog";
        }
        return "modify/addFamily";
    }

    @PostMapping("addFamily")
    public String addFamily(Model model, @Valid Family family, Errors errors) {
        userDetailsService.detailsSet(model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "modify/addFamily";
        }
        userDetailsService.addUserInfoToFamily(family);
        log.info("    --- Creating new family");

        familyService.createFamily(family);
        familyService.setFamilyDetails(family);

        return "redirect:/modify/addMember";
    }

    @GetMapping("memberUpdate")
    public String getMemberUpdateView(Model model) {
        userDetailsService.detailsSet(model);
        if (familyService.isFamilyEmpty()) {
            return "redirect:/index";
        }

        memberService.getFamilyMembersDtoListAndAddToModel(model);

        return "modify/memberUpdate";
    }

    @PostMapping("memberUpdate")
    public String MemberDataUpdate(Details userDetails, Model model) {
        userDetailsService.detailsSet(model);

        idToModify = userDetails.getId();

        if (idToModify == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/memberUpdate";
        }

        List<Long> allFamilyMembersId = memberService.getMembersIdList();

        if (allFamilyMembersId.contains(idToModify)) {

            log.info("    --- Member found");
            return "redirect:/modify/updateData";
        }

        log.info("    --- No member");
        return "redirect:/modify/memberUpdate";
    }

    @GetMapping("addMember")
    public String getAddMemberView(Model model) {
        userDetailsService.detailsSet(model);

        return "modify/addMember";
    }

    @PostMapping("addMember")
    public String addMemberToFamily(Model model, @Valid MemberDto member, Errors errors) {
        userDetailsService.detailsSet(model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "modify/addMember";
        }

        log.info("    --- Creating new family member");
        Member newMember = convertToEntity(member, modelMapper);
        memberService.createMember(newMember);

        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("addMember2")
    public String getAddMemberView2(Model model) {
        userDetailsService.detailsSet(model);

        return "modify/addMember2";
    }

    @PostMapping("addMember2")
    public String addMemberToFamily2(Model model, @Valid MemberDto member, Errors errors) {
        userDetailsService.detailsSet(model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "modify/addMember2";
        }

        log.info("    --- Creating new family member");
        Member newMember = convertToEntity(member, modelMapper);
        memberService.createMember(newMember);

        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("removeFamily")
    public String getHomeView(Model model) {
        userDetailsService.detailsSet(model);

        if (!familyService.isDoIHaveFamily()) {
            return "redirect:/index";
        }

        if (getUsername().equals("anonymousUser")) {
            log.info("    --- No Family found");
            return "redirect:/index";
        }

        return "modify/removeFamily";

    }

    @PostMapping("removeFamily")
    public String DeleteFamilyFromDatabase(Model model) {
        userDetailsService.detailsSet(model);

        memberService.deleteAllMembersFromFamily();
        familyService.repositoryDeleteSetter();

        log.info("    --- Family Deleted");
        return "redirect:/index";
    }


    @GetMapping("removeMember")
    public String getDeleteMemberView(Model model, @ModelAttribute Member member1) {
        userDetailsService.detailsSet(model);

        memberService.getFamilyMembersDtoListAndAddToModel(model);
        if (memberService.getMembers().isEmpty()) {
            familyService.repositoryDeleteSetter();

            return "redirect:/modify/removeMember";
        }
        return "modify/removeMember";
    }

    @PostMapping("removeMember")
    public String createYourself(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);

        if (userDetails.getId() == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/removeMember";
        }

        Long idToRemove = userDetails.getId();
        List<Long> allFamilyMembersId = memberService.getMembersIdList();

        if (allFamilyMembersId.contains(idToRemove)) {
            memberService.deleteMember(idToRemove);

            log.info("    --- Member deleted");
            return "redirect:/modify/redirectControlPass";
        }

        log.info("    --- No member");
        return "redirect:/modify/removeMember";
    }

    @GetMapping("redirectControlPass")
    public String RedirectDataChecker(Model model) {
        userDetailsService.detailsSet(model);

        if (memberService.getMembers().isEmpty()) {
            familyService.repositoryDeleteSetter();

            log.info("    --- Verify yours data");
            return "redirect:/index";
        }
        log.info("    --- Verify yours data");
        return "redirect:/index";

    }

    @GetMapping("updateData")
    public String showUpdateForm(Model model) {
        userDetailsService.detailsSet(model);

        if (idToModify == null || idToModify == 0L) {
            return "redirect:/index";
        }
        Details userDetails = new Details();
        Member memberToUpdate = memberService.getMember(idToModify);
        String memberData = memberToUpdate.getName() + "   " +
                memberToUpdate.getFamilyName() + " ID: " + idToModify;

        userDetails.setText(memberData);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("memberToUpdate", memberToUpdate);

        log.info("   --- Updating Member");
        return "modify/updateData";
    }

    @PostMapping("updateData")
    public String postUpdateForm(Model model, @Valid MemberDto memberToUpdate,
                                 Errors errors, Details userDetails) {
        userDetailsService.detailsSet(model);

        if (idToModify == 0L) {
            return "/modify/memberUpdate";
        }
        if (errors.hasErrors()) {
            userDetails.setText("");
            model.addAttribute("userDetails", userDetails);
            log.info("    --- Try again");
            return "/modify/updateData";
        }
        memberService.updateMemberData(idToModify, memberToUpdate);
        idToModify = null;
        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("wellLog")
    public String viewFamilyByUser(Model model) {
        userDetailsService.detailsSet(model);
        return "modify/wellLog";

    }

    @GetMapping("getMyFamilyAfterLog")
    public String viewFamilyByUser2(Model model) {
        userDetailsService.detailsSet(model);

        if (!familyService.isDoIHaveFamily()) {
            return "modify/wellLog";
        }
        memberService.getFamilyMembersDtoListAndAddToModel(model);
        return "modify/getMyFamilyAfterLog";
    }

    @GetMapping("find")
    public String findFamily(Model model, @ModelAttribute Details familyId) {
        userDetailsService.detailsSet(model);
        model.addAttribute("familyId", familyId);
        log.info("   --- Finding Family");

        return "modify/find";
    }

    @GetMapping("myFamily")
    public String getFamilyIfExists(Model model) {
        userDetailsService.detailsSet(model);
        Family family = familyService.getFamilyByGivenId(idToFind);
        model.addAttribute("families", family);
        model.addAttribute("member", family.getMembers());
        return "modify/myFamily";
    }

    @PostMapping("find")
    public String findMyFamily(Model model, Details familyId) {
        userDetailsService.detailsSet(model);
        model.addAttribute("familyId", familyId);
        idToFind = familyId.getIdToFind();
        if (idToFind == null) {
            return "modify/find";
        }
        log.info("   --- Family found!");
        if (familyService.isFamilyExists(idToFind)) {
            return "redirect:/modify/myFamily";
        }
        return "modify/find";
    }
}

