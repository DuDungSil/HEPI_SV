package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseRequest;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseResult;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientRequest;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientResult;
import org.hepi.hepi_sv.model.vo.User;

@Mapper
public interface InsertMapper {

    void insertUser(@Param("user") User user);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertNutriRequest(@Param("request") NutrientRequest request);

    void insertNutriResult(@Param("result") NutrientResult result, int id);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertExerRequest(@Param("request") ExerciseRequest request);

    void insertExerResult(@Param("result") ExerciseResult result, int id);
}
