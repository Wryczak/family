package com.example.family.web;

import com.example.family.data.UserRepository;
import com.example.family.Interfaces.Details;
import com.example.family.Interfaces.DetailsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class HomeController implements DetailsSet {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getHomeView(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username,Details details) {
      detailsSet(userRepository,username,details,model);

        return "index";
    }
}
