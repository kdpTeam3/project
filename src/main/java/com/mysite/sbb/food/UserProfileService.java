package com.mysite.sbb.food;

import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    
    // 유저 프로필 저장 로직
    public void saveUserProfile(String userId, int kcal, int protein, float fat, int carb, String preferredCategories) {
        // 저장 로직 (데이터베이스 저장 등)
        UserProfile userProfile = new UserProfile(userId, kcal, protein, fat, carb, preferredCategories);
        // 저장하는 실제 로직을 여기에 추가
    }

    // 유저 프로필 불러오기 로직
    public UserProfile getUserProfileByUserId(String userId) {
        // 데이터베이스에서 유저 프로필 가져오는 로직 추가
        // 예시: userProfileRepository.findByUserId(userId);
        return null;  // 실제 로직에서 프로필이 없으면 null 반환
    }
}
