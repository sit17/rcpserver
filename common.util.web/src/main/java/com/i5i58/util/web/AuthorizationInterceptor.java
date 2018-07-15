package com.i5i58.util.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.JedisUtils;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	JedisUtils jedisUtils;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Authorization auth = method.getAnnotation(Authorization.class);
		if (auth != null) {
			// 从header中得到token
			String accId = request.getHeader("accId");
			String token = request.getHeader("token");
			if (StringUtils.isNotEmpty(accId) && StringUtils.isNotEmpty(token)) {
				// 验证token
				// String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
				String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
				if (jedisUtils.exist(key) && jedisUtils.get(key).equals(token)) {
					// 如果token验证成功，将token对应的用户id存在request中，便于之后注入
					request.setAttribute("accId", accId);
					request.setAttribute("token", token);
					return true;
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					System.out.println("token failed");
					return false;
				}
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				System.out.println("token failed:params null");
				return false;
			}
		}
		return true;
	}
}