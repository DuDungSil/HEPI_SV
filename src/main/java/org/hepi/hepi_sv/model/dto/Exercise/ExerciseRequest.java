package org.hepi.hepi_sv.model.dto.Exercise;

import lombok.Data;

@Data
public class ExerciseRequest {
    String sex;
    int age;
    double height;
    double weight;
    ExerciseSet bench;
    ExerciseSet squat;
    ExerciseSet dead;
    ExerciseSet overhead;
    ExerciseSet pushup;
    ExerciseSet pullup;
    String[] supplePupose;
}
