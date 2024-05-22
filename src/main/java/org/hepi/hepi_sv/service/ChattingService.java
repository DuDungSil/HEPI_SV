package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.Chatting;
import org.hepi.hepi_sv.vo.EventImage;

import java.util.HashMap;
import java.util.List;

public class ChattingService implements RequestService {
    DatabaseService databaseService = (DatabaseService)ApplicationContextProvider.getBean("databaseService");
    private HashMap<String, String> request;

    public ChattingService(HashMap<String, String> request) {
        this.request = request;
    }

    @Override
    public String execute() {
        List<Chatting> list = databaseService.selectChatting(request.get("gym_id"));
        String listJson = "";
        try {
            System.out.println(list);
            ObjectMapper objectMapper = new ObjectMapper();
            listJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return listJson;
    }
}
