package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.myBatis.InsertMapper;
import org.hepi.hepi_sv.myBatis.SelectMapper;
import org.hepi.hepi_sv.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private SelectMapper selectMapper;

    @Autowired
    private InsertMapper insertMapper;

    public User selectUser(String id) {
        return selectMapper.selectUser(id);
    }

    public Gym selectGym(String id) {
        return selectMapper.selectGym(id);
    }
    public List<EventImage> selectEventImage() {
        List<EventImage> list = new ArrayList<>();
        selectMapper.selectEventImage().forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectEventProduct() {
        List<Product> list = new ArrayList<>();
        selectMapper.selectEventProduct().forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectMyProduct(String id) {
        List<Product> list = new ArrayList<>();
        selectMapper.selectMyProduct(id).forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectCartProduct(String id) {
        List<Product> list = new ArrayList<>();
        selectMapper.selectCartProduct(id).forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Chatting> selectChatting(String gym_id) {
        List<Chatting> list = new ArrayList<>();
        selectMapper.selectChatting(gym_id).forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public void insertUser(User user) {
        try {
            insertMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public void insertMyChat(String user_id, String message) {
        try {
            insertMapper.insertMyChat(user_id, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public User checkPhone(String phone) {
        try {
            return selectMapper.checkPhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }
}
