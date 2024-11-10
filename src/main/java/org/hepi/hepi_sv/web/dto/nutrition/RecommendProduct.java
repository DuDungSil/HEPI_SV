package org.hepi.hepi_sv.web.dto.nutrition;

import java.util.List;

import org.hepi.hepi_sv.product.entity.ShopProduct;

import lombok.Data;

@Data
public class RecommendProduct {
    List<ShopProduct> carbohydrate;
    List<ShopProduct> protein;
    List<ShopProduct> fat;
}
