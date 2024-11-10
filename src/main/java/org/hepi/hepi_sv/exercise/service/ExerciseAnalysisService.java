package org.hepi.hepi_sv.exercise.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hepi.hepi_sv.exercise.repository.mybatis.ExerciseMapper;
import org.hepi.hepi_sv.nutrition.entity.NutrientProfile;
import org.hepi.hepi_sv.web.dto.exercise.BodyPart;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseAbility;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseProfile;
import org.hepi.hepi_sv.web.dto.exercise.PurposeRecommend;
import org.springframework.stereotype.Service;

@Service
public class ExerciseAnalysisService {

    private final ExerciseMapper exerciseMapper;

    public ExerciseAnalysisService(ExerciseMapper exerciseMapper) {
        this.exerciseMapper = exerciseMapper;
    }

    private double getRM1(double liftingWeight, int Reps){
        double W1 = liftingWeight * 0.025 * Reps;
        double RM1 = liftingWeight + W1;
        return RM1;
    }

    public String getLevel(double score){
        double[] thresholds = {20, 40, 60, 80, 100, 120};
        String[] levels = {"입문자", "초보자", "중급자", "숙련자", "고급자", "운동선수"};

        String level = levels[levels.length - 1];
        for(int i = 0; i < thresholds.length; i++){
            if(score < thresholds[i]){
                level = levels[i];
                break;
            }
        }

        return level;
    }

    public ExerciseProfile analyzeExercise(String exercise, String sex, double bodyWeight, double liftingWeight, int reps) {
        ExerciseProfile profile = new ExerciseProfile();
    
        String part = exerciseMapper.selectExerPart(exercise);

        // 성별에 따른 기준 설정
        List<Double> thresholds = exerciseMapper.selectExerThreshold(exercise, sex);
        double[] scores = {0, 20, 40, 60, 80, 100, 120};
        
        double SP, strength;
        String SP_type = exerciseMapper.selectExerSPType(exercise);
        if("REP".equals(SP_type)) { // db
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
        for (int i = 0; i < thresholds.size() - 1; i++) {
            if (SP >= thresholds.get(i) && SP < thresholds.get(i + 1)) {
                score = scores[i] + 20 * (SP - thresholds.get(i)) / (thresholds.get(i + 1) - thresholds.get(i));
                break;
            }
        }
    
        if (score >= 120) {
            score = 120;
        }

        String level = getLevel(score); 
    
        // 평균
        double average;
        if("REP".equals(SP_type)){
            average = thresholds.get(2);
        }
        else {
            average = thresholds.get(2) * bodyWeight;
        }
        
        // 결과 저장
        profile.setPart(part);
        profile.setScore((int)score);
        profile.setLevel(level);
        profile.setStrength((int)strength);
        profile.setAverage((int)average); // 몰라서 임의설정
        
        return profile;
    }

    // 상대적으로 낮은 부위
    public List<BodyPart> getWeekBodyPartList(ExerciseAbility ability) {
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
                String strength = exerciseMapper.selectStrength(profile.getPart());
                List<String> details = exerciseMapper.selectBodyDetailParts(profile.getPart());

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
                String strength = exerciseMapper.selectStrength(profile.getPart());
                List<String> details = exerciseMapper.selectBodyDetailParts(profile.getPart());

                part.setStrength(strength);
                part.setDetails(details);

                list.add(part);
                addedParts.add(profile.getPart());  // 추가된 부위를 기록
            }
        }

        return list;
    }

    // 운동 수준에 따른 추천
    public List<NutrientProfile> getRecommendNutirientForLevel(int totalScore) {

        int level = (totalScore / 20) + 1;
        if (level >= 7) {
            level = 6;
        }

        // db 쿼리 키 : level
        List<NutrientProfile> list = exerciseMapper.selectNutrientProfilesByLevel(String.valueOf(level));

        return list;
    }

    // 운동 목적에 따른 추천
    public List<PurposeRecommend> getRecommendNutirientForPurpose(String[] purposes) {

        List<PurposeRecommend> list = new ArrayList<>();

        for (String purpose : purposes) {

            PurposeRecommend recommend = new PurposeRecommend();

            // db 쿼리 키 : purpose
            List<NutrientProfile> profiles = exerciseMapper.selectNutrientProfilesByPurpose(purpose);

            recommend.setPurpose(purpose);
            recommend.setProfiles(profiles);

            list.add(recommend);
        }

        return list;
    }

}