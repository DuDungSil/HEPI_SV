package org.hepi.hepi_sv.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseResult {
    int totalScore;
    String totalLevel;
    double topPercent;
    int bigThree;
    ExerciseAbility ability;
    // 약한 부위
}