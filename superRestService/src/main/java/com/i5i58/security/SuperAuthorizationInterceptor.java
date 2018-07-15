package com.i5i58.security;

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
import com.i5i58.util.SuperAdminUtils;

@Component
public class SuperAuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	SuperAdminUtils superAdminUtils;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		SuperAuthorization sa = method.getAnnotation(SuperAuthorization.class);
		if (sa != null) {
			System.out.println(request.getRequestURL().toString());
			// 从header中得到token
			String accId = request.getHeader("accId");
			String token = request.getHeader("token");
			if (StringUtils.isEmpty(accId) || StringUtils.isEmpty(token)) {
				System.out.println("token failed:params null");
				return false;
			}
			String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
			if (!new JedisUtils().exist(key) || !new JedisUtils().get(key).equals(token)) {
				System.out.println("token failed");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			if (!superAdminUtils.verifyAuth(accId, sa.value())) {
				System.out.println("no super auth");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			request.setAttribute("accId",accId);
			request.setAttribute("token",token);
		}
		return true;
	}
}