package com.example.family.family;

import com.example.family.data.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public interface DoIHaveFamily {

    default void getMyFamilyStatus(UserRepository userRepository, String username, Model model) {
        Details details = new Details();
        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            details.setSecondStatus(true);
            model.addAttribute("details", details);
        }

        details.setSecondStatus(false);
        model.addAttribute("details", details);

    }
}
