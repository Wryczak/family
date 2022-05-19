package com.example.family.web;

import com.example.family.data.UserRepository;
import com.example.family.family.Details;
import com.example.family.family.DetailsSet;
import com.example.family.family.DoIHaveFamily;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class HomeController implements DetailsSet {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getHomeView(Model model,Details details1, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        details1.setSecondStatus(false);
        model.addAttribute("details1",details1);
//        getMyFamilyStatus(userRepository, username, model);
        String indexView = "index";
        System.out.println(username);
        return getMenuDependsOnAuthentication(indexView, model, username);
    }
}
