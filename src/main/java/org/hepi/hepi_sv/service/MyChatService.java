package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.Chatting;

import java.util.HashMap;
import java.util.List;

public class MyChatService implements RequestService {
    DatabaseService databaseService = (DatabaseService)ApplicationContextProvider.getBean("databaseService");
    private HashMap<String, String> request;

    public MyChatService(HashMap<String, String> request) {
        this.request = request;
    }

    @Override
    public String execute() {

        try{
            databaseService.insertMyChat(request.get("user_id"), request.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return "";
    }
}
