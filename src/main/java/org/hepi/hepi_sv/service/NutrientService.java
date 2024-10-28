package org.hepi.hepi_sv.service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hepi.hepi_sv.model.dto.Nutrient.NutrientComposition;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientInfo;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientRequest;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientResult;
import org.hepi.hepi_sv.model.dto.Nutrient.RecommendProduct;
import org.hepi.hepi_sv.model.dto.Nutrient.RecommendSource;
import org.hepi.hepi_sv.model.vo.ShopProduct;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class NutrientService implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(EventImageService.class);
    
    NutrientRequest request;
    private HttpServletRequest httpRequest;
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    public NutrientService(NutrientRequest request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    private double calculateBMI(double height, double weight) {
        
        double m_height = height / 100;

        double BMI = weight / Math.pow(m_height, 2);
        return BMI;
    }

    private double calculateBMR(String sex, double height, double weight, int age){
        double BMR;
        if(sex.equals("남성")){
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
            case "매우활동적" -> BMR * 1.9;
            default -> BMR * 1.55;
        };
        return TDEE;
    } 

    // 목적에 따른 섭취량 계산
    private double calculateTargetCalory(double TDEE, String dietGoal){
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
    private NutrientComposition getNutrientComposition(double targetCalory, String dietType) {
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
    private List<String> recommendMealTimes(String wakeUpTime, String bedTime) {
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
    private RecommendSource getRecommendSource(String dietType) {

        RecommendSource sources = new RecommendSource();

        if(!dietType.equals("비건")){
            dietType = "일반";
        }

        // db 쿼리 키 : dietType, nutrienttype
        List<String> carbohydrate = databaseService.selectRecommendSource("탄수화물", "일반");
        List<String> protein = databaseService.selectRecommendSource("단백질", dietType);
        List<String> fat = databaseService.selectRecommendSource("지방", "일반");

        sources.setCarbohydrate(carbohydrate);
        sources.setProtein(protein);
        sources.setFat(fat);

        return sources;
    }

    // 쿠팡 제품 추천
    private RecommendProduct getRecommendProduct(String dietType) {
        
        RecommendProduct products = new RecommendProduct();

        if(!dietType.equals("비건")){
            dietType = "일반";
        }
        
        // db 쿼리 키 : dietType, nutrienttype
        List<ShopProduct> carbohydrate = databaseService.selectRecommendProductByType("탄수화물", "일반");
        List<ShopProduct> protein = databaseService.selectRecommendProductByType("단백질", dietType);
        List<ShopProduct> fat = databaseService.selectRecommendProductByType("지방", "일반");

        products.setCarbohydrate(carbohydrate);
        products.setProtein(protein);
        products.setFat(fat);

        return products;
    }

    private void recordUserData(NutrientRequest request, NutrientResult result) {
        
        databaseService.insertNutriRequest(request);
        //logger.info(request.toString());
        databaseService.insertNutriResult(result, request.getId());

    }

    @Override
    public String execute() {
        logger.info(request.toString());

        double BMI = Math.round(calculateBMI(request.getHeight(), request.getWeight()) * 100) / 100.0;
        double BMR = calculateBMR(request.getSex(), request.getHeight(), request.getWeight(), request.getAge());
        double TDEE = calculateTDEE(BMR, request.getPA());
        double targetCalory = calculateTargetCalory(TDEE, request.getDietGoal());
        NutrientComposition composition = getNutrientComposition(targetCalory, request.getDietType());
        List<String> mealTimes = recommendMealTimes(request.getWakeup(), request.getSleep());
        RecommendSource recommendSource = getRecommendSource(request.getDietType());
        RecommendProduct recommendProduct = getRecommendProduct(request.getDietType());

        NutrientResult result = new NutrientResult();
        result.setBMI(BMI);
        result.setBMR((int)BMR);
        result.setTDEE((int)TDEE);
        result.setTargetCalory((int)targetCalory);
        result.setDietGoal(request.getDietGoal());
        result.setComposition(composition);
        result.setWakeup(request.getWakeup());
        result.setSleep(request.getSleep());
        result.setMealTimes(mealTimes);
        result.setSources(recommendSource);
        result.setProducts(recommendProduct);

        // db 인서트
        recordUserData(request, result);

        String resultJson = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            resultJson = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        httpRequest.setAttribute("content", resultJson);

        return resultJson;
    }

}

