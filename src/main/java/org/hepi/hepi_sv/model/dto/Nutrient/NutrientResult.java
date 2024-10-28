package org.hepi.hepi_sv.model.dto.Nutrient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NutrientResult {
    @JsonProperty("BMI")
    private double BMI;
    @JsonProperty("BMR")
    private int BMR;
    @JsonProperty("TDEE")
    private int TDEE;
    private int targetCalory;
    private String dietGoal;
    private NutrientComposition composition;
    private String wakeup;
    private String sleep;
    private List<String> mealTimes;
    private RecommendSource sources;
    private RecommendProduct products;
}

