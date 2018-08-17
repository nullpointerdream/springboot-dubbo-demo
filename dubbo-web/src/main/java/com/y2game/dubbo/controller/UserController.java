package com.y2game.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.y2game.common.pojo.Result;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: chenjiale
 * @Date: 2018/8/8 11:08
 * @Description:
 */

@RestController
@Api(description = "用户模块")
@RequestMapping("/api")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping(value="/getCode", method = RequestMethod.POST)
    @ApiOperation(value="获取验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "游戏id 测试appid1001",paramType = "form")})
    public Result sendValidateCode(String phone){
        UserDO byUsername = userService.findByUsername(phone);
        return new Result(byUsername);
    }



}
