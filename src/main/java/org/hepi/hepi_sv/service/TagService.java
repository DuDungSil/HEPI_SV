package org.hepi.hepi_sv.service;

import java.util.List;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class TagService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(EventImageService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");
    private HttpServletRequest httpRequest;
    
    public TagService(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public String execute() {
        List<String> list = databaseService.getTags();

        String listJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            listJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (Exception e) {
            logger.error("[ERROR] | {}", e);
            throw new ErrorHandler("오류가 발생했습니다");
        }

        httpRequest.setAttribute("content", listJson);
        return listJson;
    }
}
