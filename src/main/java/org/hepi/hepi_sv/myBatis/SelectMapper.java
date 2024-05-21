package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.vo.EventImage;
import org.hepi.hepi_sv.vo.Product;
import org.hepi.hepi_sv.vo.User;

import java.util.List;
import java.util.Map;

@Mapper
public interface SelectMapper {

    public User selectUser(String USER_ID);
    public List<Product> selectEventProduct();
    public List<Product> selectMyProduct(String USER_ID);
    public List<Product> selectCartProduct(String USER_ID);
    public List<EventImage> selectEventImage();

}
