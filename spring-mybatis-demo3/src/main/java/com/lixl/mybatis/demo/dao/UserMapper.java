package com.lixl.mybatis.demo.dao;

import com.lixl.mybatis.demo.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName: UserDao
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:43
 */
public interface UserMapper {

    @Select(" select * from sys_user where user_id = #{userId}")
    User findById(@Param("userId")Long userId);

    @Delete(" delete from sys_user where user_id = #{userId}")
    Integer deleteByPrimaryKey(@Param("userId")Long userId);

    @Insert("")
    Integer insert(User user);

    @Update("")
    Integer update(User user);

}
