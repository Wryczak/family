package com.example.family.Controllers.HomeAndErrors;

import com.example.family.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class HomeController {

private final UserDetailsService userDetailsService;
    @Autowired
    public HomeController( UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String getHomeView(Model model) {
        userDetailsService.detailsSet(model);
        return "index";
    }

}
