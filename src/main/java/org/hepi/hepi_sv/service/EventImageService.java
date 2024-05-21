package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.EventImage;

import java.util.List;

public class EventImageService implements RequestService {
    DatabaseService databaseService = (DatabaseService)ApplicationContextProvider.getBean("databaseService");

    @Override
    public String execute() {
        List<EventImage> list = databaseService.selectEventImage();
        String listJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            listJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return listJson;
    }
}
