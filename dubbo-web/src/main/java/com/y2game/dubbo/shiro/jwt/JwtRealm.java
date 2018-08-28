package com.y2game.dubbo.shiro.jwt;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Sets;
import com.y2game.common.util.JWTUtil;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class JwtRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        //表示此Realm只支持JwtToken类型
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 根据用户名查找角色，请根据需求实现
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 根据username查询角色
        authorizationInfo.setRoles(Sets.newHashSet("admin","superadmin"));

        // 根据username查询权限
        authorizationInfo.setStringPermissions(Sets.newHashSet("system:*"));

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // 获取token
        String token = jwtToken.getToken();
        // 从token中获取用户名
        String username = JWTUtil.getUsername(token);
        // 根据用户名查询数据库
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        UserDO userBean = userService.findByUsername(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");

    }
}