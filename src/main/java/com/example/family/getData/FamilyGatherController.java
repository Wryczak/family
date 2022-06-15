package com.example.family.getData;

import com.example.family.data.FamilyRepository;
import com.example.family.Interfaces.Details;
import com.example.family.family.Family;
import com.example.family.family.Member;
import com.example.family.services.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/find")
@Slf4j
public class FamilyGatherController {
    private Long idToFind;
    private final FamilyRepository familyRepository;
    private final MemberService memberService;

    @Autowired
    public FamilyGatherController(FamilyRepository familyRepository, MemberService memberService) {
        this.familyRepository = familyRepository;
        this.memberService = memberService;
    }

    @ModelAttribute(name = "family")
    public Family getFamily() {
        return new Family();
    }

    @ModelAttribute(name = "member")
    public Member createMember() {
        return new Member();
    }

    @GetMapping
    public String findFamily(@ModelAttribute Family family, Model model,Details details) {
        memberService.detailsSet(details,model);

        log.info("   --- Finding Family");

        return "find";
    }

    @GetMapping("/myFamily")
    public String showAll(Model model, Details details) {
        memberService.detailsSet(details,model);
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member", familyRepository.findById(idToFind).get().getMembers());
        return "myFamily";
    }

    @PostMapping
    public String processFamily(Optional<Family> family, Model model, Details details) {
        memberService.detailsSet(details,model);
        Long id=family.get().getId();
        if (id == null) {
            return  "find";
        }

        if (familyRepository.existsById(id)){
            idToFind =id;
            log.info("    --- Family Found");
            return "redirect:/find/myFamily";
        }
        return  "find";
    }
}
