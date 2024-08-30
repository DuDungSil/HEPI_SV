package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Random;

public class SmsService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    HashMap<String, String> request;
    private HttpServletRequest httpRequest;

    public SmsService(HashMap<String, String> request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    @Override
    public String execute() {
        String phone = request.get("phone");

        if (databaseService.checkPhone(phone) != null) {
            logger.info("[Sms] ALREADY EXIST PHONE NUMBER | {}", phone);
            throw new ErrorHandler("이미 가입되어 있는 핸드폰 번호입니다");
        }

        try {
            String authCode = String.format("%06d", 100000 + new Random().nextInt(900000));
            new SmsUtil().sendOne(phone, authCode);

            String userJson = "";
            ObjectMapper objectMapper = new ObjectMapper();
            userJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authCode);

            httpRequest.setAttribute("content", userJson);
            return userJson;
        } catch (Exception e) {
            logger.error("[ERROR] | {}", e);
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }
}
