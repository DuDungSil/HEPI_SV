package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.Product;

import java.util.HashMap;
import java.util.List;

public class ProductService implements RequestService {
    MainService mainService = (MainService) ApplicationContextProvider.getBean("mainService");

    String type;
    HashMap<String, String> request;

    public ProductService(String type, HashMap<String, String> request) {
        this.type = type;
        this.request = request;
    }

    @Override
    public String execute() {

        List list;
        switch (type)
        {
            case "event":
                list = mainService.getEventProduct();
                System.out.println(list);
                break;
            case "mine":
                list = mainService.getMyProduct(request.get("id"));
                System.out.println(list);
                break;
            default:
                list = mainService.getEventProduct();
                break;
        }

        String productJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            System.out.println("에러 발생");
            productJson = "에러 발생";
        }
        return productJson;
    }
}
