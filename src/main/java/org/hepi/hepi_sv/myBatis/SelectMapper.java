package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.vo.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SelectMapper {

    public User selectUser(String USER_ID);
    public Gym selectGym(String USER_ID);
    public List<Product> selectEventProduct();
    public List<Product> selectMyProduct(String USER_ID);
    public List<Product> selectCartProduct(String USER_ID);
    public List<Chatting> selectChatting(String GYM_ID);
    public List<EventImage> selectEventImage();

}
