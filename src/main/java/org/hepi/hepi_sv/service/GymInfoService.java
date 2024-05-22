package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.Gym;
import org.hepi.hepi_sv.vo.User;

import java.util.HashMap;

public class GymInfoService implements RequestService {
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");
    private HashMap<String, String> request;

    public GymInfoService(HashMap<String, String> request) {
        this.request = request;
    }

    @Override
    public String execute() {
        Gym gym = databaseService.selectGym(request.get("user_id"));

        String userJson = "";
        try {
            System.out.println(gym);
            ObjectMapper objectMapper = new ObjectMapper();
            userJson = objectMapper.writeValueAsString(gym);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return userJson;
    }
}
