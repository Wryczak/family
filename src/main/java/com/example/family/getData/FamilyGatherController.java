package com.example.family.getData;

import com.example.family.data.FamilyRepository;
import com.example.family.data.UserRepository;
import com.example.family.family.Details;
import com.example.family.family.DetailsSet;
import com.example.family.family.Family;
import com.example.family.family.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
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
    public String findFamily(@ModelAttribute Family family, Model model, Details details,
                             @CurrentSecurityContext(expression = "authentication?.name") String username) {

        log.info("   --- Finding Family");
        String findView = "find";
        return getMenuDependsOnAuthentication(userRepository,findView, model, username);
    }

    @GetMapping("/myFamily")
    public String showAll(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        model.addAttribute("families", familyRepository.findAllById(Collections.singleton(idToFind)));
        model.addAttribute("member", familyRepository.findById(idToFind).get().getMembers());
        String myFamilyView = "myFamily";
        return getMenuDependsOnAuthentication(userRepository,myFamilyView, model, username);
    }

    @PostMapping
    public String processFamily(Optional<Family> family, Errors errors, Long id, Model model,Details details,
                                @CurrentSecurityContext(expression = "authentication?.name") String username) {
        if (errors.hasErrors()) {
            String findView = "find";
            return getMenuDependsOnAuthentication(userRepository,findView, model, username);
        }
        if (id == null) {
            String findView = "find";
            return getMenuDependsOnAuthentication(userRepository,findView, model, username);
        }

        if (familyRepository.existsById(id)) {
            family = familyRepository.findById(id);
            idToFind = family.get().getId();
            log.info("    --- Family Found");
            return "redirect:/find/myFamily";
        }
        String findView = "find";
        return getMenuDependsOnAuthentication(userRepository,findView, model, username);
    }
}
