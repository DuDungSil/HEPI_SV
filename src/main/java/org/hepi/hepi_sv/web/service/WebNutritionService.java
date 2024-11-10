package org.hepi.hepi_sv.web.service;

import java.util.Collections;
import java.util.List;

import org.hepi.hepi_sv.nutrition.service.NutritionAnalysisService;
import org.hepi.hepi_sv.product.entity.ShopProduct;
import org.hepi.hepi_sv.product.service.ProductRecommendService;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientComposition;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientRequest;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientResult;
import org.hepi.hepi_sv.web.dto.nutrition.RecommendProduct;
import org.hepi.hepi_sv.web.dto.nutrition.RecommendSource;
import org.hepi.hepi_sv.web.repository.mybatis.WebDataMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class WebNutritionService {

    private final NutritionAnalysisService nutrientAnalysisService;
    private final ProductRecommendService productRecommendService;
    private final WebDataMapper webDataMapper;
    private final ObjectMapper objectMapper;

    public WebNutritionService(NutritionAnalysisService nutrientAnalysisService, ProductRecommendService productRecommendService, WebDataMapper webDataMapper, ObjectMapper objectMapper) {
        this.nutrientAnalysisService = nutrientAnalysisService;
        this.productRecommendService = productRecommendService;
        this.webDataMapper = webDataMapper;
        this.objectMapper = objectMapper;
    }

    private void recordUserNutriData(NutrientRequest request, NutrientResult result,
            HttpServletRequest servletRequest) {

        String clientIp = getClientIp(servletRequest);
        String userAgent = servletRequest.getHeader("User-Agent");

        webDataMapper.insertNutriRequest(request, clientIp, userAgent);
        webDataMapper.insertNutriResult(result, request.getId());
    }
    
    // 3대영양소에 따른 음식 추천
    private RecommendProduct getRecommendFoodByMajorNutrient(String dietType) {
        RecommendProduct products = new RecommendProduct();

        if (!dietType.equals("비건")) {
            dietType = "일반";
        }

        List<ShopProduct> carbohydrate = productRecommendService.getRecommendFoodByMajorNutrient("탄수화물", "일반");
        List<ShopProduct> protein = productRecommendService.getRecommendFoodByMajorNutrient("단백질", dietType);
        List<ShopProduct> fat = productRecommendService.getRecommendFoodByMajorNutrient("지방", "일반");

        Collections.shuffle(carbohydrate);
        Collections.shuffle(protein);
        Collections.shuffle(fat);

        products.setCarbohydrate(carbohydrate);
        products.setProtein(protein);
        products.setFat(fat);

        return products;
    }

    public String getNutritionAnalysis(NutrientRequest request, HttpServletRequest servletRequest) {
        double BMI = Math.round(nutrientAnalysisService.calculateBMI(request.getHeight(), request.getWeight()) * 100) / 100.0;
        double BMR = nutrientAnalysisService.calculateBMR(request.getSex(), request.getHeight(), request.getWeight(), request.getAge());
        double TDEE = nutrientAnalysisService.calculateTDEE(BMR, request.getPA());
        double targetCalory = nutrientAnalysisService.calculateTargetCalory(TDEE, request.getDietGoal());
        NutrientComposition composition = nutrientAnalysisService.getNutrientComposition(targetCalory, request.getDietType());
        List<String> mealTimes = nutrientAnalysisService.recommendMealTimes(request.getWakeup(), request.getSleep());
        RecommendSource recommendSource = nutrientAnalysisService.getRecommendSource(request.getDietType());

        RecommendProduct recommendProduct = getRecommendFoodByMajorNutrient(request.getDietType());

        NutrientResult result = new NutrientResult();
        result.setBMI(BMI);
        result.setBMR((int) BMR);
        result.setTDEE((int) TDEE);
        result.setTargetCalory((int) targetCalory);
        result.setDietGoal(request.getDietGoal());
        result.setComposition(composition);
        result.setWakeup(request.getWakeup());
        result.setSleep(request.getSleep());
        result.setMealTimes(mealTimes);
        result.setSources(recommendSource);
        result.setProducts(recommendProduct);

        // 데이터베이스에 기록
        recordUserNutriData(request, result, servletRequest);

        return convertResultToJson(result);
    }

    private String convertResultToJson(NutrientResult result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            throw new RuntimeException("Error converting result to JSON", e);
        }
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = null;
    
        // 가장 흔히 사용되는 헤더들을 순차적으로 검사
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "CF-Connecting-IP",        // Cloudflare
            "True-Client-IP",          // Akamai
            "X-Client-IP",
            "X-Cluster-Client-IP",
            "Forwarded-For",
            "Forwarded"
        };
    
        for (String header : headers) {
            clientIp = request.getHeader(header);
            if (clientIp != null && !clientIp.isEmpty() && !"unknown".equalsIgnoreCase(clientIp)) {
                // 여러 개의 IP가 포함된 경우, 첫 번째 IP가 클라이언트 IP임
                clientIp = clientIp.split(",")[0].trim();
                return clientIp;
            }
        }
    
        // 헤더에서 찾지 못했을 때 fallback
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
    
        return clientIp;
    }
}
