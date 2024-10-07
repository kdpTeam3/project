package com.mysite.sbb.UserMatching;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserMatchingController {
    @GetMapping("/matching")
    public String showMatchingPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // 현재 로그인한 사용자의 ID를 가져옵니다.
        model.addAttribute("userId", userId);
        return "UserMatchingPage";
    }
}