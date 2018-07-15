package com.i5i58.util.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.i5i58.util.StringUtils;

public class HttpUtils {

	/**
	 * 获取请求头中的AccId
	 * 
	 * @return
	 */
	public static String getAccIdFromHeader() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// return request.getHeader("accId");
		Object object = request.getAttribute("accId");
		if (object != null) {
			return object.toString(); // for JWT
		} else {
			return null;
		}
	}

	/**
	 * 获取请求头中的Token
	 * 
	 * @return
	 */
	public static String getTokenFromHeader() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// return request.getHeader("token");
		return request.getAttribute("token").toString();// for jwt
	}

	public static String getParamsString() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Map<String, String[]> params = request.getParameterMap();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				sb.append(key);
				sb.append(":");
				sb.append(value);
				sb.append(",");
			}
		}
		int length = sb.length();
		sb.delete(length - 1, length);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 字符串是否为空或空字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean StringIsEmptyOrNull(String str) {
		if (str != null && !str.isEmpty()) {
			return false;
		}
		return true;
	}

	// /**
	// * 获取客户端远程ip地址
	// *
	// * @return
	// */
	// public static String getClientIpAddress() {
	// HttpServletRequest request = ((ServletRequestAttributes)
	// RequestContextHolder.getRequestAttributes())
	// .getRequest();
	// return request.getRemoteAddr();
	// }

	// /**
	// * 获取请求头中的AccId
	// *
	// * @return
	// */
	// public static String getClientIpAddressFromHeader() {
	// HttpServletRequest request = ((ServletRequestAttributes)
	// RequestContextHolder.getRequestAttributes())
	// .getRequest();
	// return request.getHeader("clientIp");
	// }

	public static String getRealClientIpAddress() {
		// 获取request对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String ip = request.getHeader("clientIp");// 获取来自服务器的客户端ip
		if (StringUtils.StringIsEmptyOrNull(ip)) {
			// 获取来自负载均衡的客户端ip地址
			ip = IpUtils.getAddr(request);
		}
		return ip;
	}
}
