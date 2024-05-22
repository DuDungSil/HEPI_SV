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
        try {
            requestService = new EventImageService();
            return requestService.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    @RequestMapping("/gymInfo")
    public String gymInfo(@RequestBody HashMap<String, String> request) {
        try {
            requestService = new GymInfoService(request);
            return requestService.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    @RequestMapping("/chatting")
    public String chatting(@RequestBody HashMap<String, String> request) {
        try {
            requestService = new ChattingService(request);
            return requestService.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    @RequestMapping("/myChat")
    public String myChat(@RequestBody HashMap<String, String> request) {
        try {
            requestService = new MyChatService(request);
            return requestService.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    @RequestMapping("/product/{type}")
    public String product(@PathVariable String type, @RequestBody HashMap<String, String> request) {
        try {
            requestService = new ProductService(type, request);
            return requestService.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
        }
    }

    @ExceptionHandler(ErrorHandler.class)
    public ResponseEntity<ErrorResponse> handleError(ErrorHandler e) {
        ErrorResponse errorResponse = new ErrorResponse("ERROR", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
