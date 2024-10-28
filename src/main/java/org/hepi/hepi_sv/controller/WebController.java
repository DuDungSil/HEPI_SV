package org.hepi.hepi_sv.controller;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.errorHandler.ErrorResponse;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseRequest;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientRequest;
import org.hepi.hepi_sv.service.ExerciseService;
import org.hepi.hepi_sv.service.NutrientService;
import org.hepi.hepi_sv.service.RequestService;
import org.hepi.hepi_sv.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/web")
public class WebController {

    RequestService requestService;

    @PostMapping("/nutrient")
    public String nutrient(@RequestBody NutrientRequest request, HttpServletRequest httpRequest) {
        requestService = new NutrientService(request, httpRequest);
        return requestService.execute();
    }

    @PostMapping("/exercise")
    public String exercise(@RequestBody ExerciseRequest request, HttpServletRequest httpRequest) {
        requestService = new ExerciseService(request, httpRequest);
        return requestService.execute();
    }

    @GetMapping("/tag")
    public String getTag(HttpServletRequest httpRequest) {
        requestService = new TagService(httpRequest);
        return requestService.execute();
    }

    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ErrorResponse> handleError(ErrorHandler e) {
        ErrorResponse errorResponse = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
