package com.y2game.dubbo.service;

import com.github.pagehelper.Page;
import com.y2game.common.pojo.RestResp;
import com.y2game.dubbo.pojo.UserDO;

import java.util.List;

/**
 * @Auther: chenjiale
 * @Date: 2018/8/16 14:18
 * @Description:
 */
public interface UserService {
    RestResp findByUsername(String username);

    RestResp login(String username,String password);

    RestResp getUserByToken(String token);

    RestResp loginOut(String token);

    List<UserDO> list(int pageNo, int pageSize);

}
