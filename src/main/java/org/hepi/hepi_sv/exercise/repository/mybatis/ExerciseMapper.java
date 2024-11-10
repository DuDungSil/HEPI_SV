package org.hepi.hepi_sv.exercise.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.nutrition.entity.NutrientProfile;

@Mapper
public interface ExerciseMapper {
    
    public List<String> selectTags();

    public List<Double> selectExerThreshold(String EXER_NAME, String GENDER);

    public String selectExerSPType(String EXER_NAME);

    public String selectExerPart(String EXER_NAME);

    public String selectStrength(String PART);

    public List<String> selectBodyDetailParts(String PART);

    public List<NutrientProfile> selectNutrientProfilesByLevel(String LEVEL);

    public List<NutrientProfile> selectNutrientProfilesByPurpose(String PURPOSE);

}
