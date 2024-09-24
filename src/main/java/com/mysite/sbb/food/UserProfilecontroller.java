package com.mysite.sbb.food;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserProfilecontroller {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/food/enter_profile")
    public String showEnterProfilePage(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "enter_profile";  // 프로필 입력 페이지 템플릿
    }

    @PostMapping("/food/save_profile")
    public String saveUserProfile(@RequestParam("user_id") String userId,
                                  @RequestParam("kcal") int kcal,
                                  @RequestParam("protein") int protein,
                                  @RequestParam("fat") float fat,
                                  @RequestParam("carb") int carb,
                                  @RequestParam("preferredCategories") String preferredCategories,
                                  RedirectAttributes redirectAttributes) {

        // 유저 프로필 저장 로직 실행
        userProfileService.saveUserProfile(userId, kcal, protein, fat, carb, preferredCategories);

        // 성공적으로 저장한 후 추천 페이지로 리다이렉트
        redirectAttributes.addAttribute("user_id", userId);
        return "redirect:/food/recommend";
    }
}
