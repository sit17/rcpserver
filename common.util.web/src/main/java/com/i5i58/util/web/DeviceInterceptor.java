package com.i5i58.util.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.i5i58.util.DeviceCode;

@Component
public class DeviceInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Device device = method.getAnnotation(Device.class);
		if (device != null) {
			String userAgent = request.getHeader("user-agent");
			System.out.println("userAgent:" + userAgent);
			String referer = request.getHeader("referer");
			System.out.println("referer:" + referer);
			if (userAgent != null && !userAgent.contains("i5i58") && referer != null
					&& !referer.startsWith("http://wechat.i5i58.com")) {
				// is web
				if (device.refuseWeb()) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
				if (!DeviceCode.isWeb(Integer.parseInt(request.getParameter("device")))) {
					response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
					return false;
				}
				// if
				// (!request.getParameter("device").equals(String.valueOf(DeviceCode.WEBLive))
				// &&
				// !request.getParameter("device").equals(String.valueOf(DeviceCode.WechatPublic)))
				// {
				// response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				// return false;
				// }
			}
		}
		return true;
	}
}
