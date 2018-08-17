package com.y2game.dubbo.service;


import com.y2game.dubbo.pojo.UserDO;

/**
 * @Auther: chenjiale
 * @Date: 2018/8/16 14:18
 * @Description:
 */
public interface UserService {
    UserDO findByUsername(String username);
}
