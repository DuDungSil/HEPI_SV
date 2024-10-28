package org.hepi.hepi_sv.model.dto.Nutrient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NutrientRequest {
    int id;
    String sex;
    int age;
    double height;
    double weight;
    String wakeup;
    String sleep;
    @JsonProperty("PA")
    String PA;
    String dietGoal;
    String dietType;
}
