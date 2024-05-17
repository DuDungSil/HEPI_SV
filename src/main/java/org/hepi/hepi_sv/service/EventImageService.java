package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.EventImage;
import org.hepi.hepi_sv.vo.User;

import java.util.HashMap;
import java.util.List;

public class EventImageService implements RequestService {
    MainService mainService = (MainService)ApplicationContextProvider.getBean("mainService");

    @Override
    public String execute() {
        List<EventImage> list = mainService.getEventImage();
        String listJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            listJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            System.out.println("에러 발생");
            listJson = "에러 발생";
        }
        return listJson;
    }
}
