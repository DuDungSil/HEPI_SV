package org.hepi.hepi_sv.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.errorHandler.ErrorResponse;
import org.hepi.hepi_sv.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flt")
public class FltController {

    RequestService requestService;

    @RequestMapping("/register")
    public String register(HttpServletRequest request) {
        requestService = new RegisterService(request);
        return requestService.execute();
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        requestService = new LoginService(request);
        return requestService.execute();
    }

    @RequestMapping("/eventImage")
    public String eventImage(HttpServletRequest request) {
        requestService = new EventImageService(request);
        return requestService.execute();
    }

    @RequestMapping("/gymInfo")
    public String gymInfo(HttpServletRequest request) {
        requestService = new GymInfoService(request);
        return requestService.execute();
    }

    @RequestMapping("/product/{type}")
    public String product(@PathVariable String type, HttpServletRequest request) {
        requestService = new ProductService(type, request);
        return requestService.execute();
    }

    @RequestMapping("/smsAuth")
    public String smsAuth(HttpServletRequest request) {
        requestService = new SmsService(request);
        return requestService.execute();
    }

    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ErrorResponse> handleError(ErrorHandler e) {
        ErrorResponse errorResponse = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
