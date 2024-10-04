package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.model.dto.Exercise.ExerciseAbility;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseProfile;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseRequest;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class ExerciseService implements RequestService {

    ExerciseRequest request;
    private HttpServletRequest httpRequest;

    public ExerciseService(ExerciseRequest request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    private double getRM1(double liftingWeight, int Reps){
        double W1 = liftingWeight * 0.025 * Reps;
        double RM1 = liftingWeight + W1;
        return RM1;
    }

    private String getLevel(int score){
        double[] thresholds = {20, 40, 60, 80, 100, 120};
        String[] levels = {"입문자", "초급자", "중급자", "숙련자", "고급자", "운동선수"};

        String level = levels[levels.length - 1];
        for(int i = 0; i < thresholds.length; i++){
            if(score <= thresholds[i]){
                level = levels[i];
                break;
            }
        }

        return level;
    }

    private ExerciseProfile analyzeExercise(String sex, double bodyWeight, double liftingWeight, int reps, double[][] maleThresholds, double[][] femaleThresholds, boolean isRepBased) {
        ExerciseProfile profile = new ExerciseProfile();
    
        // 성별에 따른 기준 설정
        double[][] thresholds = sex.equals("남") ? maleThresholds : femaleThresholds;
        
        String[] levels = {"입문자", "초급자", "중급자", "숙련자", "고급자", "운동선수"};
        double[] scores = {20, 40, 60, 80, 100, 120};
        
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

        double score = 0;
    
        // SP에 따른 점수와 레벨 계산
        for (int i = 0; i < thresholds.length; i++) {
            if(thresholds[i].length == 1){
                if(SP == thresholds[i][0]){
                    score = scores[i];
                    break;
                }
            }
            else{
                if (SP >= thresholds[i][0] && SP < thresholds[i][1]) {
                    score = scores[i] * (SP - thresholds[i][0]) / (thresholds[i][1] - thresholds[i][0]);
                    break;
                }
            }
        }
    
        String level = getLevel((int)score); 
        if (SP >= thresholds[thresholds.length - 1][1]) {
            score = scores[scores.length - 1];
        }
    
        // 결과 저장
        profile.setScore(score);
        profile.setLevel(level);
        profile.setStrength(strength);
        profile.setAverage(100); // 몰라서 임의설정
        
        return profile;
    }

    // 벤치 프레스 분석
    private ExerciseProfile analysisBench(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0, 0.5}, {0.5, 0.75}, {0.75, 1.25}, {1.25, 1.75}, {1.75, 2.0}, {2.0, 2.5}};
        double[][] femaleThresholds = {{0, 0.25}, {0.25, 0.5}, {0.5, 0.75}, {0.75, 1.0}, {1.0, 1.5}, {1.5, 2.0}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, false);
    }

    // 스쿼트 분석
    private ExerciseProfile analysisSquat(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0, 0.75}, {0.75, 1.25}, {1.25, 1.5}, {1.5, 2.25}, {2.25, 2.75}, {2.75, 3.25}};
        double[][] femaleThresholds = {{0, 0.5}, {0.5, 0.75}, {0.75, 1.25}, {1.25, 1.5}, {1.5, 2.0}, {2.0, 2.5}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, false);
    }

    // 데드리프트 분석
    private ExerciseProfile analysisDead(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0, 1.0}, {1.0, 1.5}, {1.5, 2.0}, {2.0, 2.5}, {2.5, 3.0}, {3.0, 3.5}};
        double[][] femaleThresholds = {{0, 0.5}, {0.5, 1.0}, {1.0, 1.25}, {1.25, 1.75}, {1.75, 2.5}, {2.5, 3.0}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, false);
    }

    // 오버헤드 프레스 분석
    private ExerciseProfile analysisOverhead(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0, 0.35}, {0.35, 0.55}, {0.55, 0.8}, {0.8, 1.1}, {1.1, 1.4}, {1.4, 1.7}};
        double[][] femaleThresholds = {{0, 0.2}, {0.2, 0.35}, {0.35, 0.5}, {0.5, 0.75}, {0.75, 1.0}, {1.0, 1.25}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, false);
    }

    // 푸쉬업 분석
    private ExerciseProfile analysisPushup(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0}, {1}, {2, 18}, {19, 41}, {42, 68}, {69, 99}};
        double[][] femaleThresholds = {{0}, {1}, {2, 5}, {6, 19}, {20, 37}, {38, 56}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, true);
    }

    // 풀업 분석
    private ExerciseProfile analysisPullup(String sex, double bodyWeight, double liftingWeight, int reps) {
        double[][] maleThresholds = {{0}, {1}, {2, 5}, {6, 14}, {15, 25}, {26, 37}};
        double[][] femaleThresholds = {{0}, {1}, {2}, {3, 5}, {6, 14}, {15, 26}};
        return analyzeExercise(sex, bodyWeight, liftingWeight, reps, maleThresholds, femaleThresholds, true);
    }

    @Override
    public String execute() {
        try{
            ExerciseAbility ability = new ExerciseAbility();
            ability.setBench(analysisBench(request.getSex(), request.getWeight(), request.getBench().getWeight(), request.getBench().getReps()));
            ability.setSquat(analysisSquat(request.getSex(), request.getWeight(), request.getSquat().getWeight(), request.getSquat().getReps()));
            ability.setDead(analysisDead(request.getSex(), request.getWeight(), request.getDead().getWeight(), request.getDead().getReps()));
            ability.setOverhead(analysisOverhead(request.getSex(), request.getWeight(), request.getOverhead().getWeight(), request.getOverhead().getReps()));
            ability.setPushup(analysisPushup(request.getSex(), request.getWeight(), request.getPushup().getWeight(), request.getPushup().getReps()));
            ability.setPullup(analysisPullup(request.getSex(), request.getWeight(), request.getPullup().getWeight(), request.getPullup().getReps()));
    
            int totalScore = (int)(ability.getBench().getScore() + ability.getSquat().getScore() + ability.getDead().getScore() + ability.getOverhead().getScore() + ability.getPushup().getScore() + ability.getPullup().getScore());
            String totalLevel = getLevel(totalScore);
            double topPercent = 100 - ( totalScore / 120 * 99 );
            int bigThree = (int)(ability.getBench().getStrength() + ability.getSquat().getStrength() + ability.getDead().getStrength());
    
            ExerciseResult result = new ExerciseResult();
            result.setAbility(ability);
            result.setTotalScore(totalScore);
            result.setTotalLevel(totalLevel);
            result.setTopPercent(topPercent);
            result.setBigThree(bigThree);

            ObjectMapper objectMapper = new ObjectMapper();
            String resultJson = objectMapper.writeValueAsString(result);

            return resultJson;

        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

}