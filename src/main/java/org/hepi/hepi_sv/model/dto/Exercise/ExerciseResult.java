package org.hepi.hepi_sv.model.dto.Exercise;

import java.util.List;

import org.hepi.hepi_sv.model.vo.NutrientProfile;
import org.hepi.hepi_sv.model.vo.ShopProduct;

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
