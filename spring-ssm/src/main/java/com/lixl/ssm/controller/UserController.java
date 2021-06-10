package com.lixl.ssm.controller;

import com.lixl.ssm.pojo.User;
import com.lixl.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Description:
 * @Author: lixl
 * @Date: 2021/6/9 15:28
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findAnnotateById")
    public Object findAnnotateById(@RequestParam("userId")Long userId) {
        User user = userService.findAnnotateById(userId);
        return user;
    }
}
