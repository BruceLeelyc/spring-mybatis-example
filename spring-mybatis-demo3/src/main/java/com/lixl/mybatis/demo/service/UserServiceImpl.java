package com.lixl.mybatis.demo.service;

import com.lixl.mybatis.demo.dao.UserDao;
import com.lixl.mybatis.demo.dao.UserMapper;
import com.lixl.mybatis.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserServiceImpl
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:30
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public User findAnnotateById(Long userId) {
        return userMapper.findById(userId);
    }
}
