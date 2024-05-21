package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;

import java.util.HashMap;
import java.util.List;

public class ProductService implements RequestService {
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

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
                list = databaseService.selectEventProduct();
                System.out.println(list);
                break;
            case "mine":
                list = databaseService.selectMyProduct(request.get("id"));
                System.out.println(list);
                break;
            case "cart":
                list = databaseService.selectCartProduct(request.get("id"));
                System.out.println(list);
                break;
            default:
                list = databaseService.selectEventProduct();
                break;
        }

        String productJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return productJson;
    }
}
