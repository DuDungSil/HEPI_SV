package org.hepi.hepi_sv.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseProfile {
    String part;      // 운동 부위
    int score;
    String level;
    int strength;  // 1RM or Max Reps
    int average;
}
