package com.lixl.mybatis.demo.dao;

import com.lixl.mybatis.demo.pojo.User;

/**
 * @ClassName: UserDao
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:43
 */
public interface UserDao {

    User findById(Long userId);

    Integer deleteByPrimaryKey(Long userId);

    Integer insert(User user);

    Integer update(User user);

}
