package com.mysite.sbb.UserMatching;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserMatchingController {

    private final RestTemplate restTemplate;

    @Autowired
    public UserMatchingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/matching")
    public String showMatchingPage(Model model, Principal principal) {
        String username = principal.getName();

        Map<String, String> userData = new HashMap<>();
        userData.put("user_id", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(userData, headers);

        String formHtml = restTemplate.postForObject("http://localhost:8000/matching-form", request, String.class);

        model.addAttribute("matchingForm", formHtml);
        return "UserMatchingPage";
    }
}