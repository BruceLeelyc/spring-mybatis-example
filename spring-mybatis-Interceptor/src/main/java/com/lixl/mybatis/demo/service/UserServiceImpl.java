package com.lixl.mybatis.demo.service;

import com.lixl.mybatis.demo.dao.UserAnnotationDao;
import com.lixl.mybatis.demo.dao.UserDao;
import com.lixl.mybatis.demo.interceptor.PagedParameter;
import com.lixl.mybatis.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private UserAnnotationDao userAnnotationDao;

    @Override
    public User findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public User findAnnotateById(Long userId) {
        return userAnnotationDao.findById(userId);
    }

    @Override
    public List<User> getPage(Long userId) {
        PagedParameter page = new PagedParameter();
        page.setP(1);
        page.setSize(2);
        page.setPageIndex(1);
        page.setPageSize(2);
        return userDao.getPage(page);
    }
    @Override
    public List<User> findPage() {
        PagedParameter page = new PagedParameter();
        page.setP(1);
        page.setSize(2);
        page.setPageIndex(1);
        page.setPageSize(2);
        return userDao.findPage(page);
    }
}
