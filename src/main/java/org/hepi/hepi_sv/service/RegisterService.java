package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class RegisterService implements RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    private HashMap<String, String> request;
    public RegisterService(HashMap<String, String> request) {
        this.request = request;
    }

    @Override
    public String execute() {

        if(databaseService.selectUser(request.get("id")) != null) {
            logger.info("[회원가입] 이미 존재하는 이메일 | {}", request.get("id"));
            throw new ErrorHandler("이미 존재하는 이메일입니다");
        }

        User user = new User(request.get("name"),request.get("id"), PasswordEncoder.hashPassword(request.get("pwd")),request.get("phone"), 0);
        databaseService.insertUser(user);
        logger.info("[회원가입] 완료 | {}", user.toString());
        return "";
    }
}
