package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hepi.hepi_sv.vo.EventImage;
import org.hepi.hepi_sv.vo.Product;
import org.hepi.hepi_sv.vo.User;

import java.util.List;

@Mapper
public interface InsertMapper {

    void insertUser(@Param("user") User user);
    void insertMyChat(@Param("user_id") String user_id, @Param("message") String message);

}
