package com.y2game.dubbo.shiro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y2game.common.pojo.ErrorCodes;
import com.y2game.common.pojo.RestResp;
import com.y2game.dubbo.shiro.jwt.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AuthenticatingFilter {

    private static final String TOKEN = "token";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 先从Header里面获取
        String token = httpRequest.getHeader(TOKEN);
        if(StringUtils.isEmpty(token)){
            // 获取不到再从Parameter中拿
            token = httpRequest.getParameter(TOKEN);
            // 还是获取不到再从Cookie中拿
            if(StringUtils.isEmpty(token)){
                Cookie[] cookies = httpRequest.getCookies();
                if(cookies != null){
                    for (Cookie cookie : cookies) {
                        if(TOKEN.equals(cookie.getName())){
                            token = cookie.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return JwtToken.builder() .token(token).build();
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        try {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json;charset=UTF-8");
            servletResponse.setHeader("Access-Control-Allow-Origin","*");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(new RestResp(ErrorCodes.LOGIN_ERROR.getErrorCode(),ErrorCodes.LOGIN_ERROR.getInfo())));
        } catch (IOException e) {
        }
        return false;
    }
}