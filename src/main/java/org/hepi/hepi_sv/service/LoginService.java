package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.User;

import java.util.HashMap;

public class LoginService implements RequestService {
    MainService mainService = (MainService)ApplicationContextProvider.getBean("mainService");
    private HashMap<String, String> request;

    public LoginService(HashMap<String, String> loginRequest) {
        this.request = loginRequest;
    }

    @Override
    public String execute() {
        User user = mainService.getUser(request.get("id"));
        if(request.get("pwd").equals(user.getPwd()))
        {
            String userJson = "";
            System.out.println(user);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                userJson = objectMapper.writeValueAsString(user);
            } catch (Exception e) {
                System.out.println("에러 발생");
                userJson = "에러 발생";
            }
            return userJson;
        }
        else
        {
            return "로그인실패";
        }
    }
}
