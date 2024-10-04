package org.hepi.hepi_sv.model.dto.Nutrient;

import lombok.Data;

@Data
public class NutrientRequest {
    String sex;
    int age;
    double height;
    double weight;
    String wakeup;
    String sleep;
    String PA;
    String dietGoal;
    String dietType;
}
