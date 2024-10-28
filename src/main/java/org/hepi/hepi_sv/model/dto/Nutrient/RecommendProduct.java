package org.hepi.hepi_sv.model.dto.Nutrient;

import java.util.List;

import org.hepi.hepi_sv.model.vo.ShopProduct;

import lombok.Data;

@Data
public class RecommendProduct {
    List<ShopProduct> carbohydrate;
    List<ShopProduct> protein;
    List<ShopProduct> fat;
}
