package org.hepi.hepi_sv.web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hepi.hepi_sv.exercise.service.ExerciseAnalysisService;
import org.hepi.hepi_sv.nutrition.entity.NutrientProfile;
import org.hepi.hepi_sv.product.entity.ShopProduct;
import org.hepi.hepi_sv.product.service.ProductRecommendService;
import org.hepi.hepi_sv.web.dto.exercise.BodyPart;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseAbility;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseRequest;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseResult;
import org.hepi.hepi_sv.web.dto.exercise.PurposeRecommend;
import org.hepi.hepi_sv.web.repository.mybatis.WebDataMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class WebExerciseService {

    private final ExerciseAnalysisService exerciseAnalysisService;
    private final ProductRecommendService productRecommendService;
    private final WebDataMapper webDataMapper;
    private final ObjectMapper objectMapper;

    public WebExerciseService(ExerciseAnalysisService exerciseAnalysisService, ProductRecommendService productRecommendService, WebDataMapper webDataMapper, ObjectMapper objectMapper) {
        this.exerciseAnalysisService = exerciseAnalysisService;
        this.productRecommendService = productRecommendService;
        this.webDataMapper = webDataMapper;
        this.objectMapper = objectMapper;
    }

    private void recordUserExerData(ExerciseRequest request, ExerciseResult result, HttpServletRequest servletRequest) {

        String clientIp = getClientIp(servletRequest);
        String userAgent = servletRequest.getHeader("User-Agent");

        webDataMapper.insertExerRequest(request, clientIp, userAgent);
        webDataMapper.insertExerResult(result, request.getId());
    }

    private List<ShopProduct> getRecommendSupplementByNutrientProfiles(List<NutrientProfile> profiles) {

        List<ShopProduct> list = new ArrayList<>();

        List<String> nutrition_list = new ArrayList<>();
        for (NutrientProfile profile : profiles) {
            nutrition_list.add(profile.getName());
        }

        nutrition_list.stream().distinct().collect(Collectors.toList());

        for (String nutrition : nutrition_list) {

            List<ShopProduct> shopProducts = productRecommendService.getRecommendSupplementByNutrition(nutrition);

            for (ShopProduct shopProduct : shopProducts) {
                list.add(shopProduct);
            }
        }

        Collections.shuffle(list);

        return list;
    }

    private List<ShopProduct> getRecommendSupplementByPurposeRecommends(List<PurposeRecommend> recommends) {
        
            List<ShopProduct> list = new ArrayList<>();

            List<String> nutrition_list = new ArrayList<>();
            for (PurposeRecommend recommend : recommends) {
                for (NutrientProfile profile : recommend.getProfiles()) {
                    nutrition_list.add(profile.getName());
                }
            }
    
            nutrition_list.stream().distinct().collect(Collectors.toList());
    
            for (String nutrition : nutrition_list) {
                List<ShopProduct> shopProducts = productRecommendService.getRecommendSupplementByNutrition(nutrition);
                for (ShopProduct shopProduct : shopProducts) {
                    list.add(shopProduct);
                }
            }
    
            Collections.shuffle(list);

            return list;
    }

    public String getExerciseAnalysis(ExerciseRequest request, HttpServletRequest servletRequest) {
        ExerciseAbility ability = new ExerciseAbility();
        ability.setBench(exerciseAnalysisService.analyzeExercise("벤치 프레스", request.getSex(), request.getWeight(),
                request.getBench().getWeight(), request.getBench().getReps()));
        ability.setSquat(exerciseAnalysisService.analyzeExercise("스쿼트", request.getSex(), request.getWeight(),
                request.getSquat().getWeight(), request.getSquat().getReps()));
        ability.setDead(exerciseAnalysisService.analyzeExercise("데드리프트", request.getSex(), request.getWeight(),
                request.getDead().getWeight(), request.getDead().getReps()));
        ability.setOverhead(exerciseAnalysisService.analyzeExercise("오버헤드 프레스", request.getSex(), request.getWeight(),
                request.getOverhead().getWeight(), request.getOverhead().getReps()));
        ability.setPushup(exerciseAnalysisService.analyzeExercise("푸쉬업", request.getSex(), request.getWeight(),
                request.getPushup().getWeight(), request.getPushup().getReps()));
        ability.setPullup(exerciseAnalysisService.analyzeExercise("풀업", request.getSex(), request.getWeight(),
                request.getPullup().getWeight(), request.getPullup().getReps()));

        int totalScore = (int) ((ability.getBench().getScore() + ability.getSquat().getScore()
                + ability.getDead().getScore() + ability.getOverhead().getScore() + ability.getPushup().getScore()
                + ability.getPullup().getScore()) / 6);
        String totalLevel = exerciseAnalysisService.getLevel(totalScore);
        double topPercent = 100 - (99.0 * totalScore / 120);
        int bigThree = (int) (ability.getBench().getStrength() + ability.getSquat().getStrength()
                + ability.getDead().getStrength());

        List<BodyPart> parts = exerciseAnalysisService.getWeekBodyPartList(ability);

        List<NutrientProfile> profiles = exerciseAnalysisService.getRecommendNutirientForLevel(totalScore);
        List<PurposeRecommend> recommends = exerciseAnalysisService
                        .getRecommendNutirientForPurpose(request.getSupplePurpose());
                
        List<ShopProduct> levelProducts = getRecommendSupplementByNutrientProfiles(profiles);
        List<ShopProduct> purposeProducts = getRecommendSupplementByPurposeRecommends(recommends);

        ExerciseResult result = new ExerciseResult();
        result.setAbility(ability);
        result.setTotalScore(totalScore);
        result.setTotalLevel(totalLevel);
        result.setTopPercent((int) topPercent);
        result.setBigThree(bigThree);
        result.setParts(parts);
        result.setLevelRecommends(profiles);
        result.setPurposeRecommends(recommends);
        result.setLevelProducts(levelProducts);
        result.setPuporseProducts(purposeProducts);

        // DB 인서트
        recordUserExerData(request, result, servletRequest);

        return convertResultToJson(result);
    }

    private String convertResultToJson(ExerciseResult result) {
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
