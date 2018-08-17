package com.y2game.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.y2game.dubbo.dao.UserMapper;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * code your dubbo service, add @Service(import com.alibaba.dubbo.config.annotation.Service) o
 * n your service class, and interfaceClass is the interface which will be published
 */
@Service(interfaceClass = UserService.class,timeout = 5000)
@Component
public class UserServiceImpl implements UserService {

    @Autowired private UserMapper userMapper;

    @Override
    public UserDO findByUsername(String username) {
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        return userMapper.selectOne(userDO);
    }
}
