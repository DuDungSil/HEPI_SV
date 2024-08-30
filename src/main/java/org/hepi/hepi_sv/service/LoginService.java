package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class LoginService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");
    private HttpServletRequest request;

    public LoginService(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String execute() {
        User user = databaseService.selectUser(request.getParameter("id"));
        if (user == null) {
            logger.info("[Login] NOT EXIST E-MAIL | {}", request.getParameter("id"));
            throw new ErrorHandler("존재하지 않는 이메일입니다");
        }

        String userJson = "";
        if (PasswordEncoder.hashPassword(request.getParameter("pwd")).equals(user.getPwd())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                userJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            } catch (Exception e) {
                logger.error("[ERROR] | {}", e);
                throw new ErrorHandler("오류가 발생했습니다");
            }

            request.setAttribute("content", userJson);
            return userJson;
        } else {
            logger.error("[Login] PASSWORD NOT CONSISTENT | {}", request.getParameter("id"));
            throw new ErrorHandler("비밀번호가 일치하지 않습니다");
        }
    }
}
