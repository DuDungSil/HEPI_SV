package org.hepi.hepi_sv.myBatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hepi.hepi_sv.model.vo.User;

@Mapper
public interface InsertMapper {

    void insertUser(@Param("user") User user);
}
