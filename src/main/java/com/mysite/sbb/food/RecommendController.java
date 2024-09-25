package com.mysite.sbb.food;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/food")
@Controller
public class RecommendController {

    @Autowired
    private UserProfileService userProfileService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_BASE_URL = "http://127.0.0.1:5000"; // Flask 서버 주소

    // GET 요청을 처리하는 메서드 추가
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recommend")
    public String showRecommendationPage() {
        return "food_recommend";  // 추천 페이지 템플릿을 반환
    }

    // user_id 확인 후 페이지 리다이렉트
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check_profile")
    public String checkProfileAndRedirect(@RequestParam("user_id") String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestBody = "user_id=" + userId;
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Flask 서버로 요청 보내서 프로필 확인
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_BASE_URL + "/check_profile", request, String.class);

        // JSON 데이터를 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            boolean profileExists = (boolean) result.get("profile_exists");

            // 프로필이 존재하면 추천 페이지로, 없으면 프로필 입력 페이지로 리다이렉트
            if (profileExists) {
                return "redirect:/food/recommend";
            } else {
                return "redirect:/food/enter_profile";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recommend")
    public String getRecommendations(@RequestParam("user_id") String userId, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestBody = "user_id=" + userId;
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Flask 서버로 추천 요청
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_BASE_URL + "/recommend", request, String.class);

        // JSON 데이터를 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON을 파싱하여 Map 형태로 변환
            Map<String, Object> recommendations = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

            // 모델에 추천 결과 추가
            model.addAttribute("recommendations", recommendations);
            model.addAttribute("user_id", userId); // 사용자 ID 추가
        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 처리
        }

        return "food_recommend"; // 추천 결과를 보여주는 페이지로 이동
    }

    @PostMapping("/submit_rating")
    public String submitRating(
        @RequestParam("user_id") String userId,
        @RequestParam("lunch_food_code[]") String[] lunchFoodCodes,
        @RequestParam("lunch_food_number[]") String[] lunchFoodNumbers,
        @RequestParam("lunch_rating[]") String[] lunchRatings,
        @RequestParam("dinner_food_code[]") String[] dinnerFoodCodes,
        @RequestParam("dinner_food_number[]") String[] dinnerFoodNumbers,
        @RequestParam("dinner_rating[]") String[] dinnerRatings) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String requestBody = "user_id=" + userId;

        for (int i = 0; i < lunchFoodCodes.length; i++) {
            requestBody += "&lunch_food_code[]=" + lunchFoodCodes[i];
            requestBody += "&lunch_food_number[]=" + lunchFoodNumbers[i];
            requestBody += "&lunch_rating[]=" + lunchRatings[i];
        }

        for (int i = 0; i < dinnerFoodCodes.length; i++) {
            requestBody += "&dinner_food_code[]=" + dinnerFoodCodes[i];
            requestBody += "&dinner_food_number[]=" + dinnerFoodNumbers[i];
            requestBody += "&dinner_rating[]=" + dinnerRatings[i];
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity(FLASK_BASE_URL + "/submit_rating", request, String.class);
        return "redirect:/"; // 평점 제출 후 메인 페이지로 리다이렉트
    }
}
