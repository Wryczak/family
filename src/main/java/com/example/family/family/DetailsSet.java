package com.example.family.family;

import com.example.family.data.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public interface DetailsSet {
    default Details detailsSet(UserRepository userRepository,String username,Details details,Model model) {
        if (username == "anonymousUser") {
            details.setText("Zaloguj się:");
            details.setStatus(false);
            model.addAttribute("details", details);
            return details;
        }
        details.setText(username);
        details.setStatus(true);
        model.addAttribute("details", details);
        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            details.setSecondStatus(true);
            model.addAttribute("status", details);
        }
        return details;
    }
}
