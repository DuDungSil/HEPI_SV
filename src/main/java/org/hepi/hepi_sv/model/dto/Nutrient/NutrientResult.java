package org.hepi.hepi_sv.model.dto.Nutrient;

import java.util.List;

import lombok.Data;

@Data
public class NutrientResult {
    private String BMI;
    private double BMR;
    private double TDEE;
    private NutrientComposition composition;
    private List<String> mealTimes;
}

