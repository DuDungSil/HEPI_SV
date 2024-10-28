package org.hepi.hepi_sv.model.dto.Exercise;

import java.util.List;

import org.hepi.hepi_sv.model.vo.NutrientProfile;

import lombok.Data;

@Data
public class PurposeRecommend{
    String purpose;
    List<NutrientProfile> profiles;
}
