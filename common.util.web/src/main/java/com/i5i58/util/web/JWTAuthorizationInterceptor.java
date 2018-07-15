package com.i5i58.util.web;

import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;

@Component
public class JWTAuthorizationInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = Logger.getLogger(getClass());

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

			// Enumeration<String> e = request.getHeaderNames();
			// while (e.hasMoreElements()) {
			// System.out.println(e.nextElement());
			// }
			// host
			// user-agent
			// accept
			// accept-language
			// accept-encoding
			// authorization
			// device
			// serialnum
			// content-type
			// connection
			// content-length
			String userAgent = request.getHeader("user-agent");
			System.out.println("userAgent:" + userAgent);
			String referer = request.getHeader("referer");
			System.out.println("referer:" + referer);
			String tokenHeader = null;
			// String device = null;
			String serialNum = null;
			if (userAgent != null && !userAgent.contains("i5i58") && referer != null
					&& !referer.startsWith("http://wechat.i5i58.com")) {
				// is web
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						switch (cookie.getName()) {
						case "authorization":
							tokenHeader = cookie.getValue();
							break;
						case "sessionId":
							serialNum = cookie.getValue();
							break;
						default:
							break;
						}
					}
				}
				// device = String.valueOf(DeviceCode.WEBLive);
				request.setAttribute("isWeb", true);
			} else {
				request.setAttribute("isWeb", false);
				tokenHeader = request.getHeader("authorization");
				// device = request.getHeader("device");
				serialNum = request.getHeader("serialNum");
			}
			if (tokenHeader == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("token failed : get jwt failed");
				return false;
			}
			if (StringUtils.isEmpty(serialNum)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("token failed : get device or serialNum empty");
				return false;
			}
			String token = tokenHeader;// .substring(7);
			JsonWebTokenParams jwt = SecurityUtils.validJsonWebToken(token, serialNum);
			if (!jwt.isValid()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("token failed : valid failed");
				return false;
			}
			if (auth.DeviceCode() != 0 && auth.DeviceCode() != Integer.parseInt(jwt.getDevice())) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("auth failed : device failed");
				return false;
			}
			// if (!jwt.getDevice().equals(device)) {
			// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			// System.out.println("token failed : device failed");
			// return false;
			// }
			if (StringUtils.isNotEmpty(jwt.getAccId()) && StringUtils.isNotEmpty(token)) {
				// 验证token
				// String key = Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId;
				String key = Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + jwt.getDevice() + "_" + jwt.getAccId();
				if (jedisUtils.exist(key) && jedisUtils.get(key).equals(token)) {
					if (jwt.getDevice() == String.valueOf(DeviceCode.WEBLive)
							|| jwt.getDevice() == String.valueOf(DeviceCode.WechatPublic)) {
						jedisUtils.expire(key, Constant.ACC_WEB_TOKEN_TIME_TO_LIVE);
					} else {
						jedisUtils.expire(key, Constant.ACC_TOKEN_TIME_TO_LIVE);
					}
					// 如果token验证成功，将token对应的用户id存在request中，便于之后注入
					request.setAttribute("accId", jwt.getAccId());
					request.setAttribute("token", token);
					request.setAttribute("device", jwt.getDevice());
					return true;
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					logger.error("token failed : null in redis or not equals");
					return false;
				}
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				logger.error("token failed : params null");
				return false;
			}
		}
		return true;
	}
}