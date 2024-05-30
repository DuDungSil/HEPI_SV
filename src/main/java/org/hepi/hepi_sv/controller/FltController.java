package org.hepi.hepi_sv.controller;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.errorHandler.ErrorResponse;
import org.hepi.hepi_sv.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("/flt")
public class FltController {

    RequestService requestService;

    @RequestMapping("/register")
    public String register(@RequestBody HashMap<String, String> request) {
        requestService = new RegisterService(request);
        return requestService.execute();
    }

    @RequestMapping("/login")
    public String login(@RequestBody HashMap<String, String> request) {
        requestService = new LoginService(request);
        return requestService.execute();
    }

    @RequestMapping("/eventImage")
    public String eventImage() {
        requestService = new EventImageService();
        return requestService.execute();
    }

    @RequestMapping("/gymInfo")
    public String gymInfo(@RequestBody HashMap<String, String> request) {
        requestService = new GymInfoService(request);
        return requestService.execute();
    }

    @RequestMapping("/chatting")
    public String chatting(@RequestBody HashMap<String, String> request) {
        requestService = new ChattingService(request);
        return requestService.execute();
    }

    @RequestMapping("/myChat")
    public String myChat(@RequestBody HashMap<String, String> request) {
        requestService = new MyChatService(request);
        return requestService.execute();
    }

    @RequestMapping("/product/{type}")
    public String product(@PathVariable String type, @RequestBody HashMap<String, String> request) {
        requestService = new ProductService(type, request);
        return requestService.execute();
    }

    @RequestMapping("/smsAuth")
    public String smsAuth(@RequestBody HashMap<String, String> request) {
        requestService = new SmsService(request);
        return requestService.execute();
    }

    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ErrorResponse> handleError(ErrorHandler e) {
        ErrorResponse errorResponse = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
