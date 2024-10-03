package org.hepi.hepi_sv.controller;

import java.util.HashMap;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.errorHandler.ErrorResponse;
import org.hepi.hepi_sv.service.NutrientService;
import org.hepi.hepi_sv.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/web")
public class WebController {

    RequestService requestService;

    @GetMapping("/calory")
    public String calory(@RequestBody HashMap<String, String> request, HttpServletRequest httpRequest) {
        requestService = new NutrientService(request, httpRequest);
        return requestService.execute();
    }

    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ErrorResponse> handleError(ErrorHandler e) {
        ErrorResponse errorResponse = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
