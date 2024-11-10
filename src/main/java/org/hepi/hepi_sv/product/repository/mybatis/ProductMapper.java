package org.hepi.hepi_sv.product.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.product.entity.ShopProduct;

@Mapper
public interface ProductMapper {
        
    public List<ShopProduct> selectFoodsByNutrientTypeAndDietType(String NUTRIENT_TYPE, String DIET_TYPE);

    public List<ShopProduct> selectSupplementsByNutrientName(String NUTRIENT_NAME);

}
