package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.vo.EventImage;
import org.hepi.hepi_sv.vo.MyProduct;
import org.hepi.hepi_sv.vo.Product;
import org.hepi.hepi_sv.vo.User;

import java.util.List;

@Mapper
public interface SelectMapper {

    public List<User> selectUser();
    public List<Product> selectProduct();
    public List<MyProduct> selectMyProduct(String USER_ID);
    public List<EventImage> selectEventImage();

}
