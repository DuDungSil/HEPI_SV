package org.hepi.hepi_sv.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseProfile {
    double score;
    String level;
    double strength;  // 1RM or Max Reps
    double average;
}
