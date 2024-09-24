package com.mysite.sbb.food;

public class UserProfile {
    private String userId;
    private int kcal;
    private int protein;
    private float fat;
    private int carb;
    private String preferredCategories;

    // 생성자
    public UserProfile(String userId, int kcal, int protein, float fat, int carb, String preferredCategories) {
        this.userId = userId;
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.preferredCategories = preferredCategories;
    }

    // 게터와 세터
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public int getCarb() {
        return carb;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    public String getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(String preferredCategories) {
        this.preferredCategories = preferredCategories;
    }
}
