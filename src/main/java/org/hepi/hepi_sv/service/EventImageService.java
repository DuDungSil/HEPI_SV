package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.vo.EventImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EventImageService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(EventImageService.class);
    DatabaseService databaseService = (DatabaseService)ApplicationContextProvider.getBean("databaseService");

    @Override
    public String execute() {
        List<EventImage> list = databaseService.selectEventImage();
        String listJson = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            listJson = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            logger.error("[ERROR] | {}", e);
            throw new ErrorHandler("오류가 발생했습니다");
        }

        logger.info("[이벤트 이미지] 제공 완료");
        return listJson;
    }
}
