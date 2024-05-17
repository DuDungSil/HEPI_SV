package org.hepi.hepi_sv.service;

import org.hepi.hepi_sv.myBatis.SelectMapper;
import org.hepi.hepi_sv.vo.MyProduct;
import org.hepi.hepi_sv.vo.Product;
import org.hepi.hepi_sv.vo.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService implements InitializingBean {

    @Autowired
    private SelectMapper selectMapper;

    private Map<String ,User> userMap;
    private Map<Integer , Product> productMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        userMap = new HashMap<String, User>();
        productMap = new HashMap<Integer, Product>();
        init();
    }

    private void init() {
        loadUserMap();
        loadProductMap();
    }

    private void loadUserMap() {
        System.out.println("Loading User ...");
        synchronized (userMap)
        {
            this.userMap.clear();
            selectMapper.selectUser().forEach(e -> {
                this.userMap.put(e.getId(), e);
            });
        }
        System.out.println("Loading User List Done - [ " + this.userMap.size() + " ]") ;
    }

    private void loadProductMap() {
        System.out.println("Loading Product ...");
        synchronized (productMap)
        {
            this.productMap.clear();
            selectMapper.selectProduct().forEach(e -> {
                this.productMap.put(e.getId(), e);
            });
        }
        System.out.println("Loading Product List Done - [ " + this.productMap.size() + " ]") ;
    }

    public User getUser(String id) {
        return userMap.get(id);
    }

    public List<Product> getEventProduct() {
        List<Product> list = new ArrayList<>();
        for(Product product : productMap.values()) {
            if(product.getEvent() != 0) {
                list.add(product);
            }
        }
        return list;
    }

    public List<MyProduct> getMyProduct(String id) {
        List<MyProduct> list = new ArrayList<>();
        selectMapper.selectMyProduct(id).forEach(e -> {
            list.add(e);
        });
        return list;
    }
}
