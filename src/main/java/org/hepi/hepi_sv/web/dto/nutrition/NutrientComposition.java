package org.hepi.hepi_sv.web.dto.nutrition;

import lombok.Data;

@Data
public class NutrientComposition {
    NutrientInfo carbohydrate;
    NutrientInfo protein;
    NutrientInfo unFat;
    NutrientInfo satFat;

    public NutrientComposition(NutrientInfo carbohydrate_, NutrientInfo protein_, NutrientInfo unFat_ , NutrientInfo satFat_){
        this.carbohydrate = carbohydrate_;
        this.protein = protein_;
        this.unFat = unFat_;
        this.satFat = satFat_;
    }
}
