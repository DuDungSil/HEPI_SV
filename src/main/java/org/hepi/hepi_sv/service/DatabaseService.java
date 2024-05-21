package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.myBatis.InsertMapper;
import org.hepi.hepi_sv.myBatis.SelectMapper;
import org.hepi.hepi_sv.vo.EventImage;
import org.hepi.hepi_sv.vo.Product;
import org.hepi.hepi_sv.vo.User;
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

    public List<Product> insertUser(User user) {
        List<Product> list = new ArrayList<>();
        try {
            insertMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
        return list;
    }
}