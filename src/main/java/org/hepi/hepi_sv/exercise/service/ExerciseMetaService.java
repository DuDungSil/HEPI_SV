package org.hepi.hepi_sv.exercise.service;

import java.util.List;

import org.hepi.hepi_sv.exercise.repository.mybatis.ExerciseMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExerciseMetaService {

    private final ExerciseMapper exerciseMapper;
    private final ObjectMapper objectMapper;

    public ExerciseMetaService(ExerciseMapper exerciseMapper, ObjectMapper objectMapper) {
        this.exerciseMapper = exerciseMapper;
        this.objectMapper = objectMapper;
    }

    public String getExercisePurpose() {
        List<String> list = exerciseMapper.selectTags();

        return convertResultToJson(list);
    }

    private String convertResultToJson(List<String> result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            throw new RuntimeException("Error converting result to JSON", e);
        }
    }
}
