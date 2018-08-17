package com.y2game.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.y2game.common.pojo.RestResp;
import com.y2game.common.util.CookieUtils;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.alibaba.dubbo.common.Constants.TOKEN_KEY;

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
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "电话",paramType = "form")})
    public RestResp sendValidateCode(String phone){
        return userService.findByUsername(phone);
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ApiOperation(value="登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "账号",paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码",paramType = "form")})
    public RestResp sendValidateCode(String phone, String password, HttpServletRequest request, HttpServletResponse response){
        RestResp restResp = userService.login(phone, password);
        if(restResp.getCode() == 200) {
            String token = restResp.getResult().toString();
            //如果登录成功需要把token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }
        return restResp;
    }

    @RequestMapping(value="/loginOut", method = RequestMethod.POST)
    @ApiOperation(value="退出登录")
    public RestResp loginOut(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        RestResp restResp = userService.loginOut(token);
        return restResp;
    }


}
