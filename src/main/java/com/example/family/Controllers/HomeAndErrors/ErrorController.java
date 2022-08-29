package com.example.family.Controllers.HomeAndErrors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    public ErrorController() {
    }
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}