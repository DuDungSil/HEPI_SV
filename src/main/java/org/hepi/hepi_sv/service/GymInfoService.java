package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.Gym;
import org.hepi.hepi_sv.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class GymInfoService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(GymInfoService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");
    private HttpServletRequest request;

    public GymInfoService(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String execute() {
        Gym gym = databaseService.selectGym(request.getParameter("user_id"));

        String userJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            userJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(gym);
        } catch (JsonProcessingException e) {
            logger.error("[ERROR] | {}", e);
            throw new ErrorHandler("오류가 발생했습니다");
        }

        request.setAttribute("content", userJson);
        return userJson;
    }
}
