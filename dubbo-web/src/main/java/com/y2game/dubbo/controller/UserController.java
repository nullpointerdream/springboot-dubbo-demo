package com.y2game.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.ImmutableMap;
import com.y2game.common.pojo.RestResp;
import com.y2game.common.util.CookieUtils;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private UserService userService;


    @Autowired
    private DefaultKaptcha captchaProducer;



    @RequestMapping(value="/getCode", method = RequestMethod.POST)
    @ApiOperation(value="获取验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "电话",paramType = "form")})
    public RestResp sendValidateCode(String phone){
        return new RestResp(userService.findByUsername(phone));
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    @ApiOperation(value="登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "账号",paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码",paramType = "form")})
    public RestResp login(String phone, String password, HttpServletRequest request, HttpServletResponse response){
        RestResp restResp = userService.login(phone, password);
       /* if(restResp.getCode() == 200) {
            String token = restResp.getResult().toString();
            //如果登录成功需要把token写入cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }*/
        return restResp;
    }

    @RequestMapping(value="/loginOut", method = RequestMethod.POST)
    @ApiOperation(value="退出登录")
    public RestResp loginOut(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        RestResp restResp = userService.loginOut(token);
        return restResp;
    }
    
    @RequestMapping(value="/list", method = RequestMethod.GET)
    @ApiOperation(value="查询所有")
    public RestResp list(Integer pageNum,Integer pageSize,String token){
        PageInfo<UserDO> pageInfo = new PageInfo<>( userService.list(pageNum, pageSize));
        return new RestResp(pageInfo);
    }

    @GetMapping(value="/captcha")
    @ApiOperation(value="验证码")
    public Map<String,String> captcha(){
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            String capText = captchaProducer.createText();
            String uuid = UUID.randomUUID().toString();
            /*redisTemplate.boundValueOps(uuid).set(capText,60, TimeUnit.SECONDS);*/
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "png", baos);
            String imgBase64 = Base64.encodeBase64String(baos.toByteArray());
            return ImmutableMap.of(uuid,"data:image/jpeg;base64,"+imgBase64);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

}
