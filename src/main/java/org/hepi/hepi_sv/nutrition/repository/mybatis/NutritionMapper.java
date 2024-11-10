package org.hepi.hepi_sv.nutrition.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NutritionMapper {

    // nutrient 부분
    
    public List<String> selectRecommendSource(String NUTRIENT_TYPE, String DIET_TYPE);

}
