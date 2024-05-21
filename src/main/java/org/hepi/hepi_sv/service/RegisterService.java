package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.User;

import java.util.HashMap;

public class RegisterService implements RequestService {
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");

    private HashMap<String, String> request;
    public RegisterService(HashMap<String, String> registerRequest) {
        this.request = registerRequest;
    }

    @Override
    public String execute() {

        if(databaseService.selectUser(request.get("id")) != null) {
            throw new ErrorHandler("이미 존재하는 이메일입니다");
        }

        databaseService.insertUser(new User(request.get("name"),request.get("id"), PasswordEncoder.hashPassword(request.get("pwd")),request.get("phone"),""));
        return "";
    }
}
