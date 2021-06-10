package com.lixl.ssm.service;

import com.lixl.ssm.dao.UserMapper;
import com.lixl.ssm.pojo.User;
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
    private UserMapper userMapper;

    @Override
    public User findAnnotateById(Long userId) {
        return userMapper.findById(userId);
    }
}
