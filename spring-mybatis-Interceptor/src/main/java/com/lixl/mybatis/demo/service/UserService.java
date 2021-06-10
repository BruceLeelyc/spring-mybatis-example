package com.lixl.mybatis.demo.service;

import com.lixl.mybatis.demo.pojo.User;

import java.util.List;

/**
 * @ClassName: UserService
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:30
 */
public interface UserService {

    User findById(Long userId);

    User findAnnotateById(Long userId);

    List<User> getPage(Long userId);
    List<User> findPage();
}
