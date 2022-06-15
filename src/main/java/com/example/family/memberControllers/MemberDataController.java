package com.example.family.memberControllers;

import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.MaturityChecker;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.family.*;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
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
public class MemberDataController implements MaturityChecker, UsernameGetter {
    private final MemberService memberService;
    private final FamilyService familyService;
    private Long idToModify;


    @Autowired
    public MemberDataController(MemberService memberService, FamilyService familyService) {

        this.memberService = memberService;
        this.familyService = familyService;
    }

    @ModelAttribute(name = "family")
    public Family createFamily() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMember() {
        return new Member();
    }

    @ModelAttribute(name = "memberDto")
    public MemberDto createMemberDto() {
        return new MemberDto();
    }

    @GetMapping("memberUpdate")
    public String getMemberUpdateView(Model model, Details details) {
        memberService.detailsSet(details, model);
        if (familyService.isFamilyEmpty()) {
            return "redirect:/index";
        }

        memberService.getFamilyMemberDtoListAndAddToModel(model,getUsername());

        return "modify/memberUpdate";
    }

    @PostMapping("memberUpdate")
    public String MemberUpdate(Details userDetails,Model model, Details details) {
        memberService.detailsSet(details, model);
        if (userDetails.getId() == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/memberUpdate";
        }

        Long memberIdToUpdate = userDetails.getId();
        idToModify = memberIdToUpdate;

        List<Long> allFamilyMembersId = memberService.getMembersIdList(getUsername());

        if (allFamilyMembersId.contains(memberIdToUpdate)) {

            log.info("    --- Member found");
            return "redirect:/modify/updateData";
        }

        log.info("    --- No member");
        return "redirect:/modify/memberUpdate";
    }

    @GetMapping("addMember")
    public String getAddMemberView(Model model, Details details) {
        memberService.detailsSet(details, model);

        return "modify/addMember";
    }

    @PostMapping("addMember")
    public String addNewMemberToFamily(Model model, @Valid Member member, Errors errors, Details details) {
        memberService.detailsSet(details, model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "modify/addMember";
        }

        log.info("    --- Creating new family member");

        memberService.newMemberSaver(member, familyService.getFamily());


        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("removeFamily")
    public String getHomeView(Model model, Details details) {
        memberService.detailsSet(details, model);

        if (!familyService.isDoIHaveFamily()) {
            return "redirect:/index";
        }

        if (getUsername().equals("anonymousUser")) {
            log.info("    --- No Family found");
            return "redirect:/404";
        }

        return "modify/removeFamily";

    }

    @PostMapping("removeFamily")
    public String DeleteFamilyFromDatabase(Model model, Details details) {
        memberService.detailsSet(details, model);

        memberService.deleteAllMembersFromFamily(memberService.getMembers());
        familyService.repositoryDeleteSetter(getUsername(), familyService.getUserFamilyNumber());

        log.info("    --- Family Deleted");
        return "redirect:/index";
    }


    @GetMapping("removeMember")
    public String getDeleteMemberView(Model model, @ModelAttribute Member member1, Details details) {
        memberService.detailsSet(details, model);

        memberService.getFamilyMemberDtoListAndAddToModel(model,getUsername());
        Long myFamilyId = familyService.getUserFamilyNumber();
        if (memberService.getMembers().isEmpty()) {
            familyService.repositoryDeleteSetter(getUsername(), myFamilyId);

            return "redirect:/modify/removeMember";
        }

        return "modify/removeMember";
    }

    @PostMapping("removeMember")
    public String createYourself(Model model, Details userDetails, Details details) {
        memberService.detailsSet(details, model);

        if (userDetails.getId() == null) {
            log.info("    ---Id is null");
            return "redirect:/modify/removeMember";
        }

        Long idToRemove = userDetails.getId();
        List<Long> allFamilyMembersId = memberService.getMembersIdList(getUsername());

        if (allFamilyMembersId.contains(idToRemove)) {
            memberService.deleteMember(idToRemove);

            log.info("    --- Member deleted");
            return "redirect:/modify/redirectControlPass";
        }

        log.info("    --- No member");
        return "redirect:/modify/removeMember";
    }

    @GetMapping("redirectControlPass")
    public String RedirectDataChecker(Model model, Details details) {
        memberService.detailsSet(details, model);


        if (memberService.getMembers().isEmpty()) {
            familyService.repositoryDeleteSetter(getUsername(),
                    familyService.getUserFamilyNumber());

            log.info("    --- Verify yours data");
            return "redirect:/index";
        }
        log.info("    --- Verify yours data");
        return "redirect:/index";

    }

    @GetMapping("updateData")
    public String showUpdateForm(Model model, Details details) {
        memberService.detailsSet(details, model);

        if (idToModify == null || idToModify == 0L) {
            return "redirect:/index";
        }
        Details userDetails = new Details();
        Member memberToUpdate = memberService.getMember(idToModify);
        String memberData = memberToUpdate.getName() + "   " + memberToUpdate.getFamilyName() + " ID: " + idToModify;
        userDetails.setText(memberData);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("memberToUpdate", memberToUpdate);

        log.info("   --- Updating Member");
        return "modify/updateData";
    }

    @PostMapping("updateData")
    public String postUpdateForm(Model model, @Valid MemberDto memberToUpdate, Errors errors, Details userDetails,
                                 Details details) {
        memberService.detailsSet(details, model);

        if (idToModify == 0L) {
            return "/modify/memberUpdate";
        }
        if (errors.hasErrors()) {
            userDetails.setText("");
            model.addAttribute("userDetails", userDetails);
            log.info("    --- Try again");
            return "/modify/updateData";
        }

        memberService.updateMember(idToModify, memberToUpdate);
        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @GetMapping("wellLog")
    public String viewFamilyByUser(Model model, Details details) {
        memberService.detailsSet(details, model);
        return "modify/wellLog";

    }

    @GetMapping("getMyFamilyAfterLog")
    public String viewFamilyByUser2(Model model, Details details) {
        memberService.detailsSet(details, model);

        if (!familyService.isDoIHaveFamily()) {
            return "modify/wellLog";
        }
       memberService.getFamilyMemberDtoListAndAddToModel(model,getUsername());
        return "modify/getMyFamilyAfterLog";
    }
}