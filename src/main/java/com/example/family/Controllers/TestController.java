package com.example.family.Controllers;

import com.example.family.Interfaces.AgeCalculator;
import com.example.family.Interfaces.DtoConverter;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.MainObjectsFamilyMemberDto.*;
import com.example.family.MainObjectsFamilyMemberDto.autocompleteList.ListWithDtoObject;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import com.example.family.services.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;

@Transactional
@Controller
@Slf4j
@RequestMapping({"/", "/test"})
public class TestController implements UsernameGetter, AgeCalculator, DtoConverter {
    private final MemberService memberService;
    private final FamilyService familyService;
    private final UserDetailsService userDetailsService;

    private final ModelMapper modelMapper;
    private Long idToModify;

    public TestController(MemberService memberService,
                          FamilyService familyService, UserDetailsService userDetailsService, ModelMapper modelMapper) {
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

    @ModelAttribute(name = "list")
    public ListWithDtoObject createListAttribute() {
        return new ListWithDtoObject();
    }

    @GetMapping("split")
    public String getSplitView(Model model, Details split, MemberDto memberToSave) {
        model.addAttribute("memberToSave", memberToSave);
        userDetailsService.detailsSet(model);
        if (!familyService.isDoIHaveFamily()) {
            return "wellLog";
        }
        Details userDetails = new Details();
        model.addAttribute("userDetails", userDetails);
        memberService.getFamilyMembersDtoListAndAddToModel(model);
        model.addAttribute("split", split);

        Long id = idToModify;
        if (id != null) {
            split.setStatus(true);
        }

        if (idToModify != null) {
            Member memberToUpdate = memberService.getMember(idToModify);
            String memberData = memberToUpdate.getName() + "   " +
                    memberToUpdate.getFamilyName();
            split.setText(memberData);
            model.addAttribute("split", split);
        }

        return "test/split";
    }

    @PostMapping("split")
    public String getSplitForm(Model model, Details userDetails) {
        userDetailsService.detailsSet(model);
        memberService.getFamilyMembersDtoListAndAddToModel(model);

        idToModify = userDetails.getId();

        return "redirect:/test/split";
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
    public String addNewMemberToFamily(Model model, MemberDto member) {
        userDetailsService.detailsSet(model);

        log.info("    --- Creating new family member");
        createRelatives(member);
        idToModify = null;
        return "redirect:/modify/getMyFamilyAfterLog";
    }

    @PostMapping("splitCancelForm")
    public String addNewMemberToFamilyFrame() {
        idToModify = null;
        return "redirect:/test/split";

    }


    private void createRelatives(MemberDto member) {

        Member addRelativesToThisMember = memberService.getMember(idToModify);
        Long option = (member.getId());

        log.info("    --- Creating new family member");
        Member newMember = convertToEntity(member, modelMapper);
        memberService.createMember(newMember);

        if (option == null) {
            return;
        }
        if (option == 1 && newMember.getFatherId() == null) {
            newMember.setGender(Gender.M);
            addRelativesToThisMember.setFatherId(newMember.getId());

        }
        if (option == 2 && newMember.getMatherId() == null) {
            newMember.setGender(Gender.F);
            addRelativesToThisMember.setMatherId(newMember.getId());

        }
        if (option == 3) {
            newMember.setGender(Gender.M);
            if (addRelativesToThisMember.getGender().equals(Gender.M)) {
                newMember.setFatherId(idToModify);
            } else newMember.setMatherId(idToModify);
        }
        if (option == 4) {
            newMember.setGender(Gender.F);
            if (addRelativesToThisMember.getGender().equals(Gender.M)) {
                newMember.setFatherId(idToModify);
            } else newMember.setMatherId(idToModify);
        }

    }
}