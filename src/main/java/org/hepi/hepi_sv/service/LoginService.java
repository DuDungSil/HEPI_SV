package org.hepi.hepi_sv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.util.ApplicationContextProvider;
import org.hepi.hepi_sv.util.PasswordEncoder;
import org.hepi.hepi_sv.vo.User;

import java.util.HashMap;

public class LoginService implements RequestService {
    DatabaseService databaseService = (DatabaseService) ApplicationContextProvider.getBean("databaseService");
    private HashMap<String, String> request;

    public LoginService(HashMap<String, String> loginRequest) {
        this.request = loginRequest;
    }

    @Override
    public String execute() {
        User user = databaseService.selectUser(request.get("id"));
        if (user == null) {
            throw new ErrorHandler("존재하지 않는 이메일입니다");
        }

        String userJson = "";
        if (PasswordEncoder.hashPassword(request.get("pwd")).equals(user.getPwd())) {
            try {
                System.out.println(user);
                ObjectMapper objectMapper = new ObjectMapper();
                userJson = objectMapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new ErrorHandler("오류가 발생했습니다");
            }
            return userJson;
        } else {
            throw new ErrorHandler("비밀번호가 일치하지 않습니다");
        }
    }
}
