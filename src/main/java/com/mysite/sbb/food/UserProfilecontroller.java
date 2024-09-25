package com.mysite.sbb.food;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/food")

@Controller
public class UserProfilecontroller {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_BASE_URL = "http://127.0.0.1:5000"; // Flask 서버 주소

    // 프로필 입력 페이지를 제공하는 GET 매핑
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/enter_profile")
    public String enterUserProfile() {
        return "enter_profile"; // Thymeleaf 템플릿 파일이 "enter_profile.html"임을 가정
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/save_profile")
    public String saveUserProfile(@RequestParam("user_id") String userId,
                                  @RequestParam("age") int age,
                                  @RequestParam("height") float height,
                                  @RequestParam("weight") float weight,
                                  @RequestParam("gender") String gender,  // 성별 추가
                                  @RequestParam(value = "preferredCategories", required = false) List<Integer> preferredCategories,
                                  RedirectAttributes redirectAttributes) {

        // 유저 프로필 정보 및 카테고리 데이터 처리
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 기본적인 프로필 정보
        StringBuilder requestBody = new StringBuilder("user_id=" + userId
                + "&age=" + age
                + "&height=" + height
                + "&weight=" + weight
                + "&gender=" + gender);

        // 선택된 카테고리 처리 (리스트로 받아 처리)
        if (preferredCategories != null) {
            for (Integer category : preferredCategories) {
                requestBody.append("&preferredCategories=").append(category);
            }
        }

        // 요청 데이터 생성
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        // Flask 서버로 POST 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_BASE_URL + "/save_profile", request, String.class);

        // 성공적으로 저장한 후 추천 페이지로 리다이렉트
        redirectAttributes.addAttribute("user_id", userId);
        return "redirect:/food/recommend";
    }
}