package com.example.family.security;

import com.example.family.Repositories.UserRepository;
import com.example.family.MainObjectsFamilyMemberDto.Details;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute(name = "details")
    public Details createDetails() {
        return new Details();
    }

    @GetMapping
    public String registerForm() {
        return "/register";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationForm form, Errors errors, Model model) {
        Details details = new Details();
        String message;

        if (errors.hasErrors()) {
            message = "Podaj poprawne dane!";
            errorMessageSetter(model, details, message);

            log.info("    --- Try again");
            return "/register";
        }

        User user = userRepo.findByUsername(form.getUsername());
        String forbiddenName="anonymousUser";

        if (user != null || form.getUsername().equals(forbiddenName)) {
            message = "Użytkownik o tym nicku już istnieje!";
            errorMessageSetter(model, details, message);

            log.info("    --- User already exist");
            return "/register";
        }

        if (!form.getPassword().equals(form.getCheckpassword())) {
            message = "Hasła muszą być takie same!";
           errorMessageSetter(model,details,message);
            details.setStatus(true);
            log.info("    --- Try again");
            return "/register";
        }

        userRepo.save(form.toUser(passwordEncoder));
        log.info("    --- User Saved");
        return "redirect:/login";
    }

    private void errorMessageSetter(Model model, Details details, String message) {
        details.setText(message);
        details.setStatus(false);
        model.addAttribute("details", details);
    }
}
