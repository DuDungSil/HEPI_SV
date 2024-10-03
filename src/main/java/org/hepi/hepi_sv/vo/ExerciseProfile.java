package org.hepi.hepi_sv.vo;

import lombok.Data;

@Data
public class ExerciseProfile {
    double score;
    String level;
    int strength;  // 1RM or Max Reps
    int average;
}
