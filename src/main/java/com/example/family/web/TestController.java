package com.example.family.web;

import com.example.family.Interfaces.AgeCalculator;
import com.example.family.Interfaces.UsernameGetter;
import com.example.family.Interfaces.Details;
import com.example.family.family.Family;
import com.example.family.family.Member;
import com.example.family.family.MemberDto;
import com.example.family.services.FamilyService;
import com.example.family.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@Controller
@Slf4j
@RequestMapping("test")
public class TestController implements UsernameGetter, AgeCalculator {
    private final MemberService memberService;

    private final FamilyService familyService;

    private Long idToModify;

    private final ModelMapper modelMapper;

    public TestController(MemberService memberService, FamilyService familyService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.familyService = familyService;
        this.modelMapper = modelMapper;
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
    public MemberDto creaateFamily() {
        return new MemberDto();
    }

    @GetMapping
    public String getTestView(Model model, Details details) {
        memberService.detailsSet(details, model);
        memberService.detailsSetter(model, getUsername());
        return "test";
    }

    @PostMapping
    public String getTestForm(Model model, Details details, Details userDetails) {
        memberService.detailsSet(details, model);
        memberService.detailsSetter(model, getUsername());


        Long id = (userDetails.getId());
        idToModify = id;
        System.out.println(id);
        return "test/addParent";
    }


    @GetMapping("addMother")
    public String getAddParentView(Model model, Details details) {
        memberService.detailsSet(details, model);
        memberService.detailsSetter(model, getUsername());
        return "modify/addMember";
    }

    @PostMapping("addMother")
    public String AddParentForm(Model model, Details details, Details userDetails, MemberDto memberDto) {
        memberService.detailsSet(details, model);
        memberService.detailsSetter(model, getUsername());


        Long id = (userDetails.getId());
        idToModify = id;
        System.out.println(id);
        return "test";
    }


    @GetMapping("addParent")
    public String getAddMemberView(Model model, Details details) {
        memberService.detailsSet(details, model);

        return "test/addParent";
    }

    @PostMapping("addParent")
    public String addNewMemberToFamily(Model model, @Valid Member member, Errors errors, Details details) {
        memberService.detailsSet(details, model);

        if (errors.hasErrors()) {
            log.info("    --- Try again");
            return "test/addParent";
        }

        log.info("    --- Creating new family member");
        Member child = memberService.getMember(idToModify);
        memberService.newMemberSaver(member, familyService.getFamily());
        child.setFatherId(member.getId());
        System.out.println(member.getId());
        return "redirect:/test/familyTree";
    }

    @GetMapping("familyTree")
    public String viewFamilyByUser(Model model, Details details) {
        memberService.detailsSet(details, model);

        if (!familyService.isDoIHaveFamily()) {
            return "modify/wellLog";
        }
//        familyService.getParentTree(idToModify);
        memberService.getFamilyMemberDtoListAndAddToModel(model, getUsername());
        return "test/familyTree";
    }


    @GetMapping("kurs")
    public String getkurs(Model model, Details details) {
        memberService.detailsSet(details, model);

        return "test/kurs";
    }
}
//    @GetMapping("recursion")
//    public String recursion(Model model, Details details){
//        Details userDetails = new Details();
//        model.addAttribute("userDetails", userDetails);
//        model.addAttribute("memberDto", getMemberDTOList());
//
//        return "test/recursion";
//    }





//    private List<MemberDto> getMemberDTOList() {
//        List<Member> members= familyService.getParentTree(42L);
//        List<MemberDto> memberDtoList = Arrays.asList(modelMapper.
//                map(members, MemberDto[].class));
//        for (MemberDto member : memberDtoList) {
//            member.setAge(calculateAge(member.getBirthday()));
//        }
//        return memberDtoList;
//    }
//
//}
