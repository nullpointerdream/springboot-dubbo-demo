package com.y2game.dubbo.interceptor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.y2game.common.pojo.ErrorCodes;
import com.y2game.common.pojo.RestResp;
import com.y2game.common.util.CookieUtils;
import com.y2game.common.util.JsonUtils;
import com.y2game.common.util.SecurityMetadataSourceTrustListHolder;
import com.y2game.dubbo.pojo.UserDO;
import com.y2game.dubbo.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;

import java.io.IOException;


/**
 * 用户登录处理拦截器
 * <p>Title: LoginInterceptor</p>
 */
@WebFilter("/*")
@Order(Integer.MAX_VALUE)
public class RequestFilter implements Filter {

	@Reference
	private UserService userService;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");
		//跨域设置,生产环境应该去掉
		httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		httpResponse.addHeader("Access-Control-Allow-Headers", "x-requested-with");
		httpResponse.addHeader("Access-Control-Max-Age", "3600");

		String url = httpRequest.getRequestURI().replaceFirst(httpRequest.getContextPath(), "");
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(httpRequest, "token");
		//2.如果没有token，未登录状态，直接放行
		//	1.1）如果请求不需要登录状态，则直接放行
		if(SecurityMetadataSourceTrustListHolder.isTrustSecurityMetadataSource(url)){
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		if (StringUtils.isBlank(token)) {
			RestResp<String> rs = new RestResp<String>(ErrorCodes.LOGIN_ERROR.getErrorCode(), ErrorCodes.LOGIN_ERROR.getInfo());
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.getWriter().write(JsonUtils.toJson(rs));
			return;
		}
		//3.取到token，需要调用sso系统的服务，根据token取用户信息
		RestResp restResp = userService.getUserByToken(token);
		//4.没有取到用户信息。登录过期
		if (restResp.getCode() != 200) {
			RestResp<String> rs = new RestResp<String>(ErrorCodes.LOGIN_ERROR.getErrorCode(), ErrorCodes.LOGIN_ERROR.getInfo());
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.getWriter().write(JsonUtils.toJson(rs));
			return;
		}
		//5.取到用户信息。登录状态。
		UserDO user = (UserDO) restResp.getResult();
		//6.把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行
		request.setAttribute("user", user);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}



}
