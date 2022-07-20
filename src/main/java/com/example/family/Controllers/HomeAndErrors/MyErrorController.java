package com.example.family.Controllers.HomeAndErrors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {
    public MyErrorController() {
    }
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}