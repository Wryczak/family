package com.example.family.getData;

import com.example.family.data.FamilyRepository;
import com.example.family.family.Family;
import com.example.family.family.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/find")
@SessionAttributes("family")
@Slf4j
public class FamilyGatherController {
    private Long idToFind;
    private FamilyRepository familyRepository;

    public FamilyGatherController(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
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
    public String findFamily() {

        log.info("   --- Finding Family");
        return "find";
    }

    @GetMapping("/myFamily")
    public String showAll(Model model) {
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member",familyRepository.findById(idToFind).get().getMembers());
        return "myFamily";
    }

    @PostMapping
    public String processFamily(Optional<Family> family, Errors errors, Long id) {
        if (errors.hasErrors()) {
            return "find";
        }
        if (id == null) {
            return "find";
        }

        if (familyRepository.existsById(id)) {
            family = familyRepository.findById(id);
            idToFind=family.get().getId();
            log.info("    --- Family Found");
            return "redirect:/find/myFamily";
        }
        return "find";
    }

}