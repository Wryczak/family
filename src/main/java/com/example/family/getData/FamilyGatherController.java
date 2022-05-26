package com.example.family.getData;

import com.example.family.data.FamilyRepository;
import com.example.family.data.UserRepository;
import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.DetailsSet;
import com.example.family.family.Family;
import com.example.family.family.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/find")
@Slf4j
public class FamilyGatherController implements DetailsSet {
    private Long idToFind;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    @Autowired
    public FamilyGatherController(FamilyRepository familyRepository, UserRepository userRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
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
    public String findFamily(@ModelAttribute Family family, Model model,
                             @CurrentSecurityContext(expression = "authentication?.name") String username, Details details) {
        detailsSet(userRepository, username, details, model);

        log.info("   --- Finding Family");

        return "find";
    }

    @GetMapping("/myFamily")
    public String showAll(Model model, @CurrentSecurityContext(expression = "authentication?.name")
    String username, Details details) {
        detailsSet(userRepository, username, details, model);
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member", familyRepository.findById(idToFind).get().getMembers());
        return "myFamily";
    }

    @PostMapping
    public String processFamily(Optional<Family> family, Model model,
                                @CurrentSecurityContext(expression = "authentication?.name")
                                String username, Details details) {
        detailsSet(userRepository, username, details, model);
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
