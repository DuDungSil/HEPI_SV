package org.hepi.hepi_sv.web.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseRequest;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseResult;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientRequest;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientResult;

@Mapper
public interface WebDataMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertNutriRequest(@Param("request") NutrientRequest request, String clientIp, String userAgent);

    void insertNutriResult(@Param("result") NutrientResult result, int id);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertExerRequest(@Param("request") ExerciseRequest request, String clientIp, String userAgent);

    void insertExerResult(@Param("result") ExerciseResult result, int id);

    void insertWebClickData(int productId, String clientIp, String userAgent);
}
