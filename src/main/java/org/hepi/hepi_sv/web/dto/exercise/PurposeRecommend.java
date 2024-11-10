package org.hepi.hepi_sv.web.dto.exercise;

import java.util.List;

import org.hepi.hepi_sv.nutrition.entity.NutrientProfile;

import lombok.Data;

@Data
public class PurposeRecommend{
    String purpose;
    List<NutrientProfile> profiles;
}
