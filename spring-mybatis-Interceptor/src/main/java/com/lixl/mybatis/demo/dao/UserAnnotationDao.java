package com.lixl.mybatis.demo.dao;

import com.lixl.mybatis.demo.config.SimpleSelectLangDriver;
import com.lixl.mybatis.demo.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * @ClassName: UserDao
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:43
 */
@Mapper
public interface UserAnnotationDao {

    @Select("select * from sys_user where user_id = #{userId}")
    @Lang(SimpleSelectLangDriver.class)
    User findById(Long userId);

    @Delete("delete from sys_user where user_id = #{userId}")
    Integer deleteByPrimaryKey(Long userId);

    @Insert("")
    Integer insert(User user);

    @Update("")
    Integer update(User user);

}
