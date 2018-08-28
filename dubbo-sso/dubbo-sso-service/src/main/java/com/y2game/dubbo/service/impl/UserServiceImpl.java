package com.y2game.dubbo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.y2game.common.jedis.JedisClient;
import com.y2game.common.pojo.ErrorCodes;
import com.y2game.common.pojo.RestResp;
import com.y2game.common.util.JWTUtil;
import com.y2game.common.util.JsonUtils;
import com.y2game.dubbo.dao.UserMapper;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;


/**
 * code your dubbo service, add @Service(import com.alibaba.dubbo.config.annotation.Service) o
 * n your service class, and interfaceClass is the interface which will be published
 */
// @Service(interfaceClass = UserService.class,timeout = 5000)
@Service
@Component
public class UserServiceImpl implements UserService {

    @Autowired private UserMapper userMapper;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Autowired
    private JedisClient jedisClient;
    @Override
    public UserDO findByUsername(String username) {
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        return userMapper.selectOne(userDO);
    }

    @Override
    public RestResp login(String username, String password) {
        UserDO user  =new UserDO();
        user.setUsername(username);
        UserDO userDO = userMapper.selectOne(user);
        if (userDO == null) {
            return new RestResp(ErrorCodes.USER_NOT_FOUND_ERROR.getErrorCode(),ErrorCodes.USER_NOT_FOUND_ERROR.getInfo());
        }
        //md5加密
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(userDO.getPassword())) {
            return new RestResp(ErrorCodes.USER_ERROR.getErrorCode(),ErrorCodes.USER_ERROR.getInfo());
        }
        String token = JWTUtil.sign(username,DigestUtils.md5DigestAsHex(password.getBytes()));
        userDO.setToken(token);
        userDO.setPassword(null);
       // 用户信息写入redis：key："SESSION:token" value："user"
        jedisClient.set("SESSION:" + token, JsonUtils.toJson(userDO));
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return new RestResp(token);
    }

    @Override
    public RestResp getUserByToken(String token) {
        String json = jedisClient.get("SESSION:" + token);
        if (json==null) {
            return new RestResp(ErrorCodes.LOGIN_ERROR.getErrorCode(),ErrorCodes.LOGIN_ERROR.getInfo());
        }
        //重置过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        UserDO userDO = new Gson().fromJson(json,UserDO.class);
        return new RestResp(userDO);
    }

    @Override
    public RestResp loginOut(String token) {
        jedisClient.del("SESSION:" + token);
        return new RestResp();
    }

    @Override
    public List<UserDO> list(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        return userMapper.getUsers();
    }
}
