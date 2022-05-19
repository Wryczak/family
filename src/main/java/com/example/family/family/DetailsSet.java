package com.example.family.family;

import com.example.family.data.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public interface DetailsSet {
    default String getMenuDependsOnAuthentication(String viewName, Model model, String username) {
        Details details = new Details();
        if (username == "anonymousUser") {
            details.setName("username");
            details.setText("Zaloguj się:");
            details.setStatus(false);
            model.addAttribute("details", details);
            return viewName;
        }

        details.setName("username");
        details.setText(username);
        details.setStatus(true);
        model.addAttribute("details", details);

        return viewName;
    }

    default void getMyFamilyStatus(UserRepository userRepository, String username, Model model) {
        Details details = new Details();

        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            details.setSecondStatus(true);
            model.addAttribute("details", details);
            return;
        }

        details.setSecondStatus(false);
        model.addAttribute("details", details);
    }

}


//        if (username.equals("anonymousUser")){
//                details.setSecondStatus(false);
//                model.addAttribute("details", details);
//                }