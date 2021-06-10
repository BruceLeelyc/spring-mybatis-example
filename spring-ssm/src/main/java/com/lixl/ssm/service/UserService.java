package com.lixl.ssm.service;

import com.lixl.ssm.pojo.User;

/**
 * @ClassName: UserService
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:30
 */
public interface UserService {

    User findAnnotateById(Long userId);
}
