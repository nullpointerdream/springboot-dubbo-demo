package com.y2game.dubbo.service;

import com.y2game.common.pojo.RestResp;
import com.y2game.dubbo.pojo.UserDO;

/**
 * @Auther: chenjiale
 * @Date: 2018/8/16 14:18
 * @Description:
 */
public interface UserService {
    RestResp findByUsername(String username);

    RestResp login(String username,String password);

    RestResp getUserByToken(String token);
}
