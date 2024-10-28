package org.hepi.hepi_sv.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hepi.hepi_sv.model.dto.Exercise.BodyPart;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseAbility;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseProfile;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseRequest;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseResult;
import org.hepi.hepi_sv.model.dto.Exercise.PurposeRecommend;
import org.hepi.hepi_sv.model.vo.NutrientProfile;
import org.hepi.hepi_sv.model.vo.ShopProduct;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class ExerciseService implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(EventImageService.class);

    ExerciseRequest request;
    private HttpServletRequest httpRequest;
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    public ExerciseService(ExerciseRequest request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    private double getRM1(double liftingWeight, int Reps){
        double W1 = liftingWeight * 0.025 * Reps;
        double RM1 = liftingWeight + W1;
        return RM1;
    }

    private String getLevel(double score){
        double[] thresholds = {20, 40, 60, 80, 100, 120};
        String[] levels = {"입문자", "초보자", "중급자", "숙련자", "고급자", "운동선수"};

        String level = levels[levels.length - 1];
        for(int i = 0; i < thresholds.length; i++){
            if(score <= thresholds[i]){
                level = levels[i];
                break;
            }
        }

        return level;
    }

    private ExerciseProfile analyzeExercise(String sex, double bodyWeight, double liftingWeight, int reps, String part, double[][] maleThresholds, double[][] femaleThresholds, boolean isRepBased) {
        ExerciseProfile profile = new ExerciseProfile();
    
        // 성별에 따른 기준 설정
        double[][] thresholds = sex.equals("남성") ? maleThresholds : femaleThresholds;
        double[] scores = {0, 20, 40, 60, 80, 100, 120};
        
        double SP, strength;
        if(isRepBased) {
            SP = reps;
            strength = reps;
        }
        else{
            double RM1 = (reps == 1) ? liftingWeight : getRM1(liftingWeight, reps);
            SP = RM1 / bodyWeight;
            strength = RM1;
        }

        double score = 120;
    
        // SP에 따른 점수와 레벨 계산
        for (int i = 0; i < thresholds.length; i++) {
            if (thresholds[i].length == 1) {
                if (SP == thresholds[i][0]) {
                    score = scores[i];
                    break;
                }                    
            } else {
                if (SP >= thresholds[i][0] && SP < thresholds[i][1]) {
                    score = scores[i] + 20 * (SP - thresholds[i][0]) / (thresholds[i][1] - thresholds[i][0]);
                    break;
                }
            }
        }
    
        if (score >= 120) {
            score = 120;
        }

        String level = getLevel(score); 
    
        // 평균
        double average;
        if(isRepBased){
            average = thresholds[2][0];
        }
        else {
            average = thresholds[2][0] * bodyWeight;
        }
        
        // 결과 저장
        profile.setPart(part);
        profile.setScore((int)score);
        profile.setLevel(level);
        profile.setStrength((int)strength);
        profile.setAverage((int)average); // 몰라서 임의설정
        
        return profile;
    }

    // 벤치 프레스 분석
    private ExerciseProfile analysisBench(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "가슴";
        double[][] maleThresholds = {{0, 0.5}, {0.5, 0.75}, {0.75, 1.25}, {1.25, 1.75}, {1.75, 2.0}, {2.0, 2.5}};
        double[][] femaleThresholds = { { 0, 0.25 }, { 0.25, 0.5 }, { 0.5, 0.75 }, { 0.75, 1.0 }, { 1.0, 1.5 },
                { 1.5, 2.0 } };
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, false);
    }

    // 스쿼트 분석
    private ExerciseProfile analysisSquat(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "하체";
        double[][] maleThresholds = {{0, 0.75}, {0.75, 1.25}, {1.25, 1.5}, {1.5, 2.25}, {2.25, 2.75}, {2.75, 3.25}};
        double[][] femaleThresholds = {{0, 0.5}, {0.5, 0.75}, {0.75, 1.25}, {1.25, 1.5}, {1.5, 2.0}, {2.0, 2.5}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, false);
    }

    // 데드리프트 분석
    private ExerciseProfile analysisDead(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "코어";
        double[][] maleThresholds = {{0, 1.0}, {1.0, 1.5}, {1.5, 2.0}, {2.0, 2.5}, {2.5, 3.0}, {3.0, 3.5}};
        double[][] femaleThresholds = {{0, 0.5}, {0.5, 1.0}, {1.0, 1.25}, {1.25, 1.75}, {1.75, 2.5}, {2.5, 3.0}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, false);
    }

    // 오버헤드 프레스 분석
    private ExerciseProfile analysisOverhead(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "어깨";
        double[][] maleThresholds = {{0, 0.35}, {0.35, 0.55}, {0.55, 0.8}, {0.8, 1.1}, {1.1, 1.4}, {1.4, 1.7}};
        double[][] femaleThresholds = {{0, 0.2}, {0.2, 0.35}, {0.35, 0.5}, {0.5, 0.75}, {0.75, 1.0}, {1.0, 1.25}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, false);
    }

    // 푸쉬업 분석
    private ExerciseProfile analysisPushup(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "팔";
        double[][] maleThresholds = {{0}, {1}, {2, 19}, {19, 42}, {42, 69}, {69, 100}};
        double[][] femaleThresholds = {{0}, {1}, {2, 6}, {6, 20}, {20, 38}, {38, 57}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, true);
    }

    // 풀업 분석
    private ExerciseProfile analysisPullup(String sex, double bodyWeight, double liftingWeight, int reps) {
        String part = "등";
        double[][] maleThresholds = { { 0 }, { 1 }, { 2, 5 }, { 6, 14 }, { 15, 25 }, { 26, 37 } };
        double[][] femaleThresholds = { { 0 }, { 1 }, { 2 }, { 3, 5 }, { 6, 14 }, { 15, 26 } };
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, part, maleThresholds, femaleThresholds, true);
    }

    // 상대적으로 낮은 부위
    private List<BodyPart> getWeekBodyPartList(ExerciseAbility ability) {
        ExerciseProfile[] profiles = new ExerciseProfile[6];

        profiles[0] = ability.getBench();
        profiles[1] = ability.getSquat();
        profiles[2] = ability.getDead();
        profiles[3] = ability.getOverhead();
        profiles[4] = ability.getPushup();
        profiles[5] = ability.getPullup();

        // 점수 기준으로 정렬하여 가장 낮은 점수 두 가지 찾기
        List<ExerciseProfile> sortedProfiles = Arrays.asList(profiles);
        sortedProfiles.sort(Comparator.comparingInt(ExerciseProfile::getScore));

        List<BodyPart> list = new ArrayList<>();
        Set<String> addedParts = new HashSet<>();  // 중복 방지를 위한 Set

        // 가장 낮은 점수 두 가지 프로필 추가
        for (int i = 0; i < 2; i++) {
            ExerciseProfile profile = sortedProfiles.get(i);
            if (!addedParts.contains(profile.getPart())) {  // 이미 추가된 부위인지 확인
                BodyPart part = new BodyPart();

                // db 쿼리 키 : part
                String strength = databaseService.selectStrength(profile.getPart());
                List<String> details = databaseService.selectBodyDetailParts(profile.getPart());

                part.setStrength(strength);
                part.setDetails(details);

                list.add(part);
                addedParts.add(profile.getPart());  // 추가된 부위를 기록
            }
        }

        // 점수가 40보다 낮은 모든 부위 추가 (중복되지 않도록)
        for (ExerciseProfile profile : profiles) {
            if (profile.getScore() < 40 && !addedParts.contains(profile.getPart())) {
                BodyPart part = new BodyPart();

                // db 쿼리 키 : part
                String strength = databaseService.selectStrength(profile.getPart());
                List<String> details = databaseService.selectBodyDetailParts(profile.getPart());

                part.setStrength(strength);
                part.setDetails(details);

                list.add(part);
                addedParts.add(profile.getPart());  // 추가된 부위를 기록
            }
        }

        return list;
    }

    // 운동 수준에 따른 추천
    private List<NutrientProfile> getRecommendNutirientForLevel(int totalScore) {

        int level = (totalScore / 20) + 1;
        if (level >= 7) {
            level = 6;
        }

        // db 쿼리 키 : level
        List<NutrientProfile> list = databaseService.selectNutrientProfilesByLevel(level);

        return list;
    }

    // 운동 목적에 따른 추천
    private List<PurposeRecommend> getRecommendNutirientForPurpose(String[] purposes) {

        List<PurposeRecommend> list = new ArrayList<>();

        for (String purpose : purposes) {

            PurposeRecommend recommend = new PurposeRecommend();

            // db 쿼리 키 : purpose
            List<NutrientProfile> profiles = databaseService.selectNutrientProfilesByPurpose(purpose);

            recommend.setPurpose(purpose);
            recommend.setProfiles(profiles);

            list.add(recommend);
        }

        return list;
    }
    
    // 쿠팡 제품 추천 ( 운동 수준 )
    private List<ShopProduct> getRecommendProductByLevel() {

        List<ShopProduct> list = new ArrayList<>();

        return list;
    }

    // 쿠팡 제품 추천 ( 운동 목적 )
    private List<ShopProduct> getRecommendProductByPurpose() {

        List<ShopProduct> list = new ArrayList<>();

        return list;
    }

    private void recordUserData(ExerciseRequest request, ExerciseResult result) {
        
        databaseService.insertExerRequest(request);
        //logger.info(request.toString());
        databaseService.insertExerResult(result, request.getId());

    }

    @Override
    public String execute() {
        logger.info(request.toString());

        
        ExerciseAbility ability = new ExerciseAbility();
        ability.setBench(analysisBench(request.getSex(), request.getWeight(), request.getBench().getWeight(), request.getBench().getReps()));
        ability.setSquat(analysisSquat(request.getSex(), request.getWeight(), request.getSquat().getWeight(), request.getSquat().getReps()));
        ability.setDead(analysisDead(request.getSex(), request.getWeight(), request.getDead().getWeight(), request.getDead().getReps()));
        ability.setOverhead(analysisOverhead(request.getSex(), request.getWeight(), request.getOverhead().getWeight(), request.getOverhead().getReps()));
        ability.setPushup(analysisPushup(request.getSex(), request.getWeight(), request.getPushup().getWeight(), request.getPushup().getReps()));
        ability.setPullup(analysisPullup(request.getSex(), request.getWeight(), request.getPullup().getWeight(), request.getPullup().getReps()));

        int totalScore = (int)((ability.getBench().getScore() + ability.getSquat().getScore() + ability.getDead().getScore() + ability.getOverhead().getScore() + ability.getPushup().getScore() + ability.getPullup().getScore()) / 6);
        String totalLevel = getLevel(totalScore);
        double topPercent = 100 - ( 99 * totalScore / 120 );
        int bigThree = (int) (ability.getBench().getStrength() + ability.getSquat().getStrength()
                + ability.getDead().getStrength());
        
        List<BodyPart> parts = getWeekBodyPartList(ability);

        List<NutrientProfile> profiles = getRecommendNutirientForLevel(totalScore);
        List<PurposeRecommend> recommend = getRecommendNutirientForPurpose(request.getSupplePurpose());
        List<ShopProduct> levelProducts = getRecommendProductByLevel();
        List<ShopProduct> puporseProducts = getRecommendProductByPurpose();


        ExerciseResult result = new ExerciseResult();
        result.setAbility(ability);
        result.setTotalScore(totalScore);
        result.setTotalLevel(totalLevel);
        result.setTopPercent((int)topPercent);
        result.setBigThree(bigThree);
        result.setParts(parts);
        result.setLevelRecommends(profiles);
        result.setPurposeRecommends(recommend);

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