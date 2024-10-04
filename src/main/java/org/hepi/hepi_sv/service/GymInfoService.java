package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.model.vo.Gym;
import org.hepi.hepi_sv.model.vo.User;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class GymInfoService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(GymInfoService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    HashMap<String, String> request;
    private HttpServletRequest httpRequest;

    public GymInfoService(HashMap<String, String> request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    @Override
    public String execute() {
        Gym gym = databaseService.selectGym(request.get("user_id"));

        String userJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            userJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(gym);
        } catch (JsonProcessingException e) {
            logger.error("[ERROR] | {}", e);
            throw new ErrorHandler("오류가 발생했습니다");
        }

        httpRequest.setAttribute("content", userJson);
        return userJson;
    }
}
