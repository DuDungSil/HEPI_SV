package org.hepi.hepi_sv.service;

import java.util.ArrayList;
import java.util.List;

import org.hepi.hepi_sv.model.dto.Nutrient.NutrientComposition;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientProfile;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientRequest;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class NutrientService implements RequestService {
    
    NutrientRequest request;
    private HttpServletRequest httpRequest;

    public NutrientService(NutrientRequest request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    private String calculateBMI(double height, double weight){
        String BMI_string;
        double BMI = weight / Math.pow(height, 2);
        if(BMI < 18.5){
            BMI_string = "저체중";
        }
        else if(BMI < 23){
            BMI_string = "정상";
        }
        else if(BMI < 25){
            BMI_string = "과체중";
        }
        else if(BMI < 30){
            BMI_string = "비만 1단계";
        }
        else if(BMI < 35){
            BMI_string = "비만 2단계";
        }
        else{
            BMI_string = "비만 3단계";
        }
        return BMI_string;
    }

    private double calculateBMR(String sex, double height, double weight, int age){
        double BMR;
        if(sex.equals("남")){
            BMR = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        }
        else{
            BMR = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        }
        return BMR;
    } 

    private double calculateTDEE(double BMR, String PA){
        // 비활동적 / 저활동적 / 활동적 / 고활동적 / 매우활동적 
        double TDEE;
        TDEE = switch (PA) {
            case "비활동적" -> BMR * 1.2;
            case "저활동적" -> BMR * 1.375;
            case "활동적" -> BMR * 1.55;
            case "고활동적" -> BMR * 1.725;
            default -> BMR * 1.9;
        };
        return TDEE;
    } 

    // 목적에 따른 섭취량 계산
    private double calculateDietGoal(double TDEE, String dietGoal){
        // 체중 감소 / 체중 유지 / 벌크업
        double dietGoal_calory;
        dietGoal_calory = switch (dietGoal) {
            case "체중감소" -> TDEE - 500;
            case "체중유지" -> TDEE;
            default -> TDEE * 1.1;
        };
        return dietGoal_calory;
    } 

    // 탄수화물, 단백질, 불포화지방, 포화지방 함량 계산
    private NutrientComposition getNutrientComposition(double TDEE, String dietType){
        NutrientProfile carbohydrate = new NutrientProfile();
        NutrientProfile protein = new NutrientProfile();
        NutrientProfile unFat = new NutrientProfile();
        NutrientProfile satFat = new NutrientProfile();

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
        
        carbohydrate.setRatio((int)(Ratios.get(0)));
        carbohydrate.setCalory((int)(TDEE * carbohydrate.getRatio() / 100));
        carbohydrate.setGram((int)(carbohydrate.getCalory() * 0.25));

        protein.setRatio((int)(Ratios.get(1)));
        protein.setCalory((int)(TDEE * protein.getRatio() / 100));
        protein.setGram((int)(protein.getCalory() * 0.25));

        unFat.setRatio((int)(Ratios.get(2)));
        unFat.setCalory((int)(TDEE * unFat.getRatio() / 100));
        unFat.setGram((int)(unFat.getCalory() / 9));   

        satFat.setRatio((int)(Ratios.get(3)));
        satFat.setCalory((int)(TDEE * satFat.getRatio() / 100));
        satFat.setGram((int)(satFat.getCalory() / 9));  

        NutrientComposition composition = new NutrientComposition(carbohydrate, protein, unFat, satFat);

        return composition;
    }

    // 식사 시간 추천
    private List<String> mealTime_calculate(String wakeup, String sleep){
        List<String> mealTimes = new ArrayList<String>(4);
        // 기상 1시간 후 아침 , 자기 4시간 전 

        // 완성 필요

        return mealTimes;
    }

    // 추천 급원 DB에서 가져오기

    @Override
    public String execute() {
        try {
            String BMI = calculateBMI(request.getHeight(), request.getWeight());
            double BMR = calculateBMR(request.getSex(), request.getHeight(), request.getWeight(), request.getAge());
            double TDEE = calculateTDEE(BMR, request.getPA());
            NutrientComposition composition = getNutrientComposition(TDEE, request.getDietType());
    
            NutrientResult result = new NutrientResult();
            result.setBMI(BMI);
            result.setBMR(BMR);
            result.setTDEE(TDEE);
            result.setComposition(composition);
    
            ObjectMapper objectMapper = new ObjectMapper();
            String resultJson = objectMapper.writeValueAsString(result);
            
            return resultJson;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}

