package org.hepi.hepi_sv.controller;

import org.hepi.hepi_sv.service.LoginService;
import org.hepi.hepi_sv.service.MainService;
import org.hepi.hepi_sv.service.ProductService;
import org.hepi.hepi_sv.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/flt")
public class FltController {

    @Autowired
    MainService mainService;

    RequestService requestService;

    @RequestMapping("/login")
    public String login(@RequestBody HashMap<String, String> request){
        requestService = new LoginService(request);
        return requestService.execute();
    }

    @RequestMapping("/product/{type}")
    public String product(@PathVariable String type, @RequestBody HashMap<String, String> request){
        requestService = new ProductService(type, request);
        return requestService.execute();
    }
}
