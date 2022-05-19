package com.example.family.security;

import com.example.family.data.UserRepository;
import com.example.family.family.Details;
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

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

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
        details.setName("errorMessage");
        String message;

        if (errors.hasErrors()) {
            message = "Proszę podać poprawne dane";
            details.setText(message);
            details.setStatus(false);
            model.addAttribute("details", details);

            log.info("    --- Try again");
            return "/register";
        }

        User user = userRepo.findByUsername(form.getUsername());
        String forbiddenName="anonymousUser";

        if (user != null || form.getUsername().equals(forbiddenName)) {
            message = "Użytkownik o tym nicku już istnieje!";
            details.setText(message);
            details.setStatus(false);
            model.addAttribute("details", details);

            log.info("    --- User already exist");
            return "/register";
        }

        if (!form.getPassword().equals(form.getCheckpassword())) {
            message = "Hasła muszą być takie same!";
            details.setStatus(true);
            details.setText(message);
            model.addAttribute("details", details);

            log.info("    --- Try again");
            return "/register";
        }

        userRepo.save(form.toUser(passwordEncoder));
        log.info("    --- User Saved");
        return "redirect:/login";
    }

}
