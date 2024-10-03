package org.hepi.hepi_sv.vo;

import java.util.List;

import lombok.Data;

@Data
public class NutrientResult {
    private double BMI;
    private double BMR;
    private double TDEE;
    private NutrientComposition profile;
    private List<String> mealTimes;
}

