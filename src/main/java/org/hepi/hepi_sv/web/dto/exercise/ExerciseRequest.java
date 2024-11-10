package org.hepi.hepi_sv.web.dto.exercise;

import lombok.Data;

@Data
public class ExerciseRequest {
    int id;
    String sex;
    int age;
    int height;
    int weight;
    ExerciseSet bench;
    ExerciseSet squat;
    ExerciseSet dead;
    ExerciseSet overhead;
    ExerciseSet pushup;
    ExerciseSet pullup;
    String[] supplePurpose;
}
