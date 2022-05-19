package com.example.family.web;

import com.example.family.data.UserRepository;
import com.example.family.family.Details;
import com.example.family.family.DetailsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/wellLog")
@Slf4j
public class TempController implements DetailsSet {

    private final UserRepository userRepository;

    public TempController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String viewFamilyByUser(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        Details userDetails = new Details();

        if (Objects.equals(username, "anonymousUser")) {
            userDetails.setSecondText("nie masz jeszcze rodziny");
            model.addAttribute("userDetails", userDetails);
            String test = "wellLog";
            return getMenuDependsOnAuthentication(test, model, username);
        }
        Long myFamilyNumber = userRepository.findByUsername(username).getMyFamilyNr();

        if (myFamilyNumber == 0L) {
            userDetails.setName("family number");
            userDetails.setSecondText("nie masz jeszcze rodziny");
            model.addAttribute("userDetails", userDetails);
            String test = "wellLog";
            return getMenuDependsOnAuthentication(test, model, username);
        }
        userDetails.setName("family number");
        userDetails.setSecondText(String.valueOf(myFamilyNumber));
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("text", myFamilyNumber);

        String test = "wellLog";
        return getMenuDependsOnAuthentication(test, model, username);
    }
}

