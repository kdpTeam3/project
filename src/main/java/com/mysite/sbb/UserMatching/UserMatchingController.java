package com.mysite.sbb.UserMatching;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserMatchingController {
    @GetMapping("/matching")
    public String showMatchingPage(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String formHtml = restTemplate.getForObject("http://localhost:8000/matching-form", String.class);
        model.addAttribute("matchingForm", formHtml);
        return "UserMatchingPage";
    }
}