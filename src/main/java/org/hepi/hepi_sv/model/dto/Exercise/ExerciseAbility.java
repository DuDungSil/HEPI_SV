package org.hepi.hepi_sv.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseAbility {
    ExerciseProfile bench;
    ExerciseProfile squat;
    ExerciseProfile dead;
    ExerciseProfile overhead;
    ExerciseProfile pushup;
    ExerciseProfile pullup;
}
