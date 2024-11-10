package org.hepi.hepi_sv.web.dto.exercise;

import java.util.List;

import org.hepi.hepi_sv.nutrition.entity.NutrientProfile;
import org.hepi.hepi_sv.product.entity.ShopProduct;

import lombok.Data;

@Data
public class ExerciseResult {
    int totalScore;
    String totalLevel;
    int topPercent;
    int bigThree;
    ExerciseAbility ability;
    List<BodyPart> parts;               // 약한 부위
    List<NutrientProfile> levelRecommends;        // 운동 수준에 따른 추천
    List<PurposeRecommend> purposeRecommends; // 운동 목적에 따른 추천
    List<ShopProduct> levelProducts;
    List<ShopProduct> puporseProducts;
}
