package org.hepi.hepi_sv.web.controller;

import org.hepi.hepi_sv.exercise.service.ExerciseMetaService;
import org.hepi.hepi_sv.web.dto.exercise.ExerciseRequest;
import org.hepi.hepi_sv.web.dto.nutrition.NutrientRequest;
import org.hepi.hepi_sv.web.service.WebExerciseService;
import org.hepi.hepi_sv.web.service.WebNutritionService;
import org.hepi.hepi_sv.web.service.WebProductClickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/web")
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private final WebNutritionService webNutritionService;
    private final WebExerciseService webExerciseService;
    private final ExerciseMetaService exerMetaService;
    private final WebProductClickService webProductClickService;

    public WebController(WebNutritionService webNutritionService, WebExerciseService webExerciseService, ExerciseMetaService exerMetaService, WebProductClickService webProductClickService) {
        this.webNutritionService = webNutritionService;
        this.webExerciseService = webExerciseService;
        this.exerMetaService = exerMetaService;
        this.webProductClickService = webProductClickService;
    }

    @PostMapping("/nutrient")
    public ResponseEntity<String> nutrient(@RequestBody NutrientRequest request, HttpServletRequest servletRequest) {
        try {
            String result = webNutritionService.getNutritionAnalysis(request, servletRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error processing nutrient request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the nutrient request.");
        }
    }

    @PostMapping("/exercise")
    public ResponseEntity<String> exercise(@RequestBody ExerciseRequest request, HttpServletRequest servletRequest) {
        try {
            String result = webExerciseService.getExerciseAnalysis(request, servletRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error processing exercise request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the exercise request.");
        }
    }

    @GetMapping("/tag")
    public ResponseEntity<String> getTag() {
        try {
            String tagResult = exerMetaService.getExercisePurpose();
            return ResponseEntity.ok(tagResult);
        } catch (Exception e) {
            logger.error("Error retrieving tags", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the tags.");
        }
    }
    
    @PostMapping("/productClick/{id}")
    public ResponseEntity<String> insertProductClick(@PathVariable int id, HttpServletRequest servletRequest) {
        try {
            webProductClickService.recordUserProductClickData(id, servletRequest);
            return ResponseEntity.ok("성공적으로 데이터를 등록하였습니다.");
        } catch (Exception e) {
            logger.error("Error insert click data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("데이터 등록에 실패하였습니다.");
        }
    }   

}
