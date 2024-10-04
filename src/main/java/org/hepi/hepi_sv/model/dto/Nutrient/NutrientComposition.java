package org.hepi.hepi_sv.model.dto.Nutrient;

import lombok.Data;

@Data
public class NutrientComposition {
    NutrientProfile carbohydrate;
    NutrientProfile protein;
    NutrientProfile unFat;
    NutrientProfile satFat;

    public NutrientComposition(NutrientProfile carbohydrate_, NutrientProfile protein_, NutrientProfile unFat_ , NutrientProfile satFat_){
        this.carbohydrate = carbohydrate_;
        this.protein = protein_;
        this.unFat = unFat_;
        this.satFat = satFat_;
    }
}
