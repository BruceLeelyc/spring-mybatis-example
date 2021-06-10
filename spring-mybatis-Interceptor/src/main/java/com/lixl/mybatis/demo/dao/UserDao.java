package com.lixl.mybatis.demo.dao;

import com.lixl.mybatis.demo.interceptor.PagedParameter;
import com.lixl.mybatis.demo.pojo.User;

import java.util.List;

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

    List<User> getPage(PagedParameter page);
    List<User> findPage(PagedParameter page);
}
