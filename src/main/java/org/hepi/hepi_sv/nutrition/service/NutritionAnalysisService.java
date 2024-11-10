package org.hepi.hepi_sv.nutrition.service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hepi.hepi_sv.nutrition.repository.mybatis.NutritionMapper;
import org.hepi.hepi_sv.product.entity.ShopProduct;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientComposition;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientInfo;
import org.hepi.hepi_sv.web.dto.nutrition.RecommendProduct;
import org.hepi.hepi_sv.web.dto.nutrition.RecommendSource;
import org.springframework.stereotype.Service;

@Service
public class NutritionAnalysisService {

    private final NutritionMapper nutritionMapper;

    public NutritionAnalysisService(NutritionMapper nutritionMapper) {
        this.nutritionMapper = nutritionMapper;
    }

    public double calculateBMI(double height, double weight) {
        
        double m_height = height / 100;

        double BMI = weight / Math.pow(m_height, 2);
        return BMI;
    }

    public double calculateBMR(String sex, double height, double weight, int age){
        double BMR;
        if(sex.equals("남")){
            BMR = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        }
        else{
            BMR = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        }
        return BMR;
    } 

    public double calculateTDEE(double BMR, String PA){
        // 비활동적 / 저활동적 / 활동적 / 고활동적 / 매우활동적 
        double TDEE;
        TDEE = switch (PA) {
            case "비활동적" -> BMR * 1.2;
            case "저활동적" -> BMR * 1.375;
            case "활동적" -> BMR * 1.55;
            case "고활동적" -> BMR * 1.725;
            case "매우활동적" -> BMR * 1.9;
            default -> BMR * 1.55;
        };
        return TDEE;
    } 

    // 목적에 따른 섭취량 계산
    public double calculateTargetCalory(double TDEE, String dietGoal){
        // 체중 감소 / 체중 유지 / 벌크업
        double dietGoal_calory;
        dietGoal_calory = switch (dietGoal) {
            case "체중 감소" -> TDEE - 500;
            case "체중 유지" -> TDEE;
            case "체중 증가" -> TDEE * 1.1;
            default -> TDEE;
        };
        return dietGoal_calory;
    } 

    // 탄수화물, 단백질, 불포화지방, 포화지방 함량 계산
    public NutrientComposition getNutrientComposition(double targetCalory, String dietType) {
        NutrientInfo carbohydrate = new NutrientInfo();
        NutrientInfo protein = new NutrientInfo();
        NutrientInfo unFat = new NutrientInfo();
        NutrientInfo satFat = new NutrientInfo();

        // 탄수화물, 단백질, 불포화지방, 포화지방
        List<Integer> Ratios = new ArrayList<>(4);

        switch (dietType) {
            case "일반적", "비건" -> Ratios = List.of(50, 30, 12, 8);
            case "저탄수화물" -> Ratios = List.of(20, 40, 25, 15);
            case "고탄수화물" -> Ratios = List.of(60, 30, 15, 5);
            case "저지방" -> Ratios = List.of(50, 35, 10, 5);
            default -> {
            }
        }

        carbohydrate.setRatio(Ratios.get(0));
        carbohydrate.setCalory(Math.round(targetCalory * carbohydrate.getRatio() / 100));
        carbohydrate.setGram(Math.round(carbohydrate.getCalory() * 0.25));

        protein.setRatio(Ratios.get(1));
        protein.setCalory(Math.round(targetCalory * protein.getRatio() / 100));
        protein.setGram(Math.round(protein.getCalory() * 0.25));

        unFat.setRatio(Ratios.get(2));
        unFat.setCalory(Math.round(targetCalory * unFat.getRatio() / 100));
        unFat.setGram(Math.round(unFat.getCalory() / 9));

        satFat.setRatio(Ratios.get(3));
        satFat.setCalory(Math.round(targetCalory * satFat.getRatio() / 100));
        satFat.setGram(Math.round(satFat.getCalory() / 9));

        NutrientComposition composition = new NutrientComposition(carbohydrate, protein, unFat, satFat);

        return composition;
    }
    
    // 식사 시간 추천
    public List<String> recommendMealTimes(String wakeUpTime, String bedTime) {
        List<String> mealTimes = new ArrayList<String>();

        // 시간 형식을 맞추기 위한 포맷터
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 기상 시간, 취침 시간 파싱
        LocalTime wakeUp = LocalTime.parse(wakeUpTime, timeFormatter);
        LocalTime bed = LocalTime.parse(bedTime, timeFormatter);

        // 아침 시간: 기상 후 1시간 후
        LocalTime breakfastTime = wakeUp.plusHours(1);

        // 저녁 시간: 취침 3시간 전
        LocalTime dinnerTime = bed.minusHours(3);

        // 아침 시간과 저녁 시간 사이의 시간 차이 계산
        Duration between = Duration.between(breakfastTime, dinnerTime);
        long totalMinutesBetween = between.toMinutes();

        // 아침 시간 추가
        mealTimes.add(breakfastTime.format(timeFormatter));

        // 아침과 저녁 사이에 추가할 식사 시간 계산
        if (totalMinutesBetween > 240) { // 4시간(240분) 이상일 때
            // 3등분으로 나누기
            long intervalMinutes = totalMinutesBetween / 3;
            
            LocalTime firstAdditionalMealTime = breakfastTime.plusMinutes(intervalMinutes);
            LocalTime secondAdditionalMealTime = breakfastTime.plusMinutes(2 * intervalMinutes);
            
            mealTimes.add(firstAdditionalMealTime.format(timeFormatter));
            mealTimes.add(secondAdditionalMealTime.format(timeFormatter));
        } else if (totalMinutesBetween > 0) {
            // 아침과 저녁 사이에 식사 1회 추가
            LocalTime additionalMealTime = breakfastTime.plusMinutes(totalMinutesBetween / 2);
            mealTimes.add(additionalMealTime.format(timeFormatter));
        }

        // 저녁 시간 추가
        mealTimes.add(dinnerTime.format(timeFormatter));

        return mealTimes;
    }

    // 추천 급원 DB에서 가져오기
    public RecommendSource getRecommendSource(String dietType) {

        RecommendSource sources = new RecommendSource();

        if(!dietType.equals("비건")){
            dietType = "일반";
        }

        // db 쿼리 키 : dietType, nutrienttype
        List<String> carbohydrate = nutritionMapper.selectRecommendSource("탄수화물", "일반");
        List<String> protein = nutritionMapper.selectRecommendSource("단백질", dietType);
        List<String> fat = nutritionMapper.selectRecommendSource("지방", "일반");

        sources.setCarbohydrate(carbohydrate);
        sources.setProtein(protein);
        sources.setFat(fat);

        return sources;
    }

}

