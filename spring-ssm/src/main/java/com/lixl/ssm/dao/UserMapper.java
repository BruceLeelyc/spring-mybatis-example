package com.lixl.ssm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lixl.ssm.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName: UserDao
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:43
 */
public interface UserMapper extends BaseMapper<User> {

    @Select(" select * from sys_user where user_id = #{userId}")
    User findById(@Param("userId") Long userId);

    @Delete(" delete from sys_user where user_id = #{userId}")
    Integer deleteByPrimaryKey(@Param("userId") Long userId);

}
