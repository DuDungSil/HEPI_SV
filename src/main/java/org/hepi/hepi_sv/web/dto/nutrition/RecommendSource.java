package org.hepi.hepi_sv.web.dto.nutrition;

import java.util.List;

import lombok.Data;

@Data
public class RecommendSource {
    List<String> carbohydrate;
    List<String> protein;
    List<String> fat;
}
