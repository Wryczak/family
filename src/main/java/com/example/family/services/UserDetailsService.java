package com.example.family.services;

import com.example.family.Interfaces.UsernameGetter;
import com.example.family.MainObjectsFamilyMemberDto.Details;
import com.example.family.MainObjectsFamilyMemberDto.Family;
import com.example.family.Repositories.UserRepository;

import com.example.family.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Objects;

@Service
public class UserDetailsService implements UsernameGetter {

    private final UserRepository userRepository;
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUserInfoToFamily(Family family) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        family.setUser(user);
    }
    public Long getUserId() {
        return (userRepository.findByUsername(getUsername()).getId());
    }

    public void detailsSet(Model model) {
        Details details = new Details();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        if (Objects.equals(username, "anonymousUser")) {
            details.setText("Zaloguj się:");
            details.setStatus(false);
            details.setSecondStatus(false);
            model.addAttribute("details", details);
            return;
        }
        details.setText(username);
        details.setStatus(true);
        model.addAttribute("details", details);
        if (userRepository.findByUsername(username).isDoIHaveFamily()) {
            details.setSecondStatus(true);
            model.addAttribute("status", details);
        }
    }
}
