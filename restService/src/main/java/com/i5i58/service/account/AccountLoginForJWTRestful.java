package com.i5i58.service.account;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountLoginForJWT;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.Device;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "账户-登陆服务for Json web token")
@RestController
@RequestMapping("v1")
public class AccountLoginForJWTRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountLoginForJWT accountLoginForJWT;

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "该账号首次登陆accountVersion=0，" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByOpenId", method = RequestMethod.POST)
	@Device
	public ResultDataSet loginByOpenId(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "password") String password, @RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			if (device == DeviceCode.WEBLive) {
				serialNum = StringUtils.createUUID();
			}
			rds = accountLoginForJWT.loginByOpenId(openId, password, version, clientIP, device, serialNum);
			if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
				HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getResponse();
				Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
				loginCookie.setDomain("i5i58.com");
				loginCookie.setHttpOnly(true);
				loginCookie.setPath("/");
				response.addCookie(loginCookie);
				Cookie sessionCookie = new Cookie("sessionId", serialNum);
				sessionCookie.setDomain("i5i58.com");
				sessionCookie.setHttpOnly(true);
				sessionCookie.setPath("/");
				response.addCookie(sessionCookie);
			}
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "手机登陆，密码使用MD5加密", notes = "该账号首次登陆accountVersion=0，" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByPhoneNo", method = RequestMethod.POST)
	@Device
	public ResultDataSet loginByPhoneNo(@RequestParam(value = "phoneNo") String phoneNo,
			@RequestParam(value = "password") String password, @RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			if (device == DeviceCode.WEBLive) {
				serialNum = StringUtils.createUUID();
			}
			rds = accountLoginForJWT.loginByPhoneNo(phoneNo, password, version, clientIP, device, serialNum);
			if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
				HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getResponse();
				System.out.println(rds.getInnerData().toString());
				Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
				loginCookie.setDomain("i5i58.com");
				loginCookie.setHttpOnly(true);
				loginCookie.setPath("/");
				response.addCookie(loginCookie);
				Cookie sessionCookie = new Cookie("sessionId", serialNum);
				sessionCookie.setDomain("i5i58.com");
				sessionCookie.setHttpOnly(true);
				sessionCookie.setPath("/");
				response.addCookie(sessionCookie);
			}
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "token登陆", notes = "" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByToken", method = RequestMethod.POST)
	@Device(refuseWeb = true)
	@Authorization
	public ResultDataSet loginByToken(@RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountLoginForJWT.loginByToken(HttpUtils.getAccIdFromHeader(), version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "第三方登陆", notes = "")
	@RequestMapping(value = "/account/loginByThird", method = RequestMethod.POST)
	@Device(refuseWeb = true)
	public ResultDataSet loginByThird(@ApiParam(value = "第三方类型,微信1，qq2，微博3") @RequestParam(value = "third") int third,
			@ApiParam(value = "第三方ID") @RequestParam(value = "thirdId") String thirdId,
			@ApiParam(value = "昵称") @RequestParam(value = "name") String name,
			@ApiParam(value = "头像地址") @RequestParam(value = "face") String face,
			@ApiParam(value = "性别") @RequestParam(value = "gender") byte gender,
			@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountLoginForJWT.loginByThird(third, thirdId, name, face, gender,
					HttpUtils.getRealClientIpAddress(), device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "微信公众号登陆，密码使用MD5加密", notes = "该账号首次登陆accountVersion=0，"
			+ "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByWechatOpenId", method = RequestMethod.POST)
	@Device
	public ResultDataSet loginByWechatOpenId(@RequestParam(value = "wechatOpenId") String WechatOpenId,
			@RequestParam(value = "version") int version, @RequestParam(value = "device") int device,
			@RequestParam(value = "serialNum") String serialNum, @RequestParam(value = "accessKey") String accessKey) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			String key = "DBD432C8B81EA1DB1E2C9CDEA248BD7A";
			if (!accessKey.equals(key)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("Service Error");
			}
			rds = accountLoginForJWT.loginByWechatOpenId(WechatOpenId, version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "测试Cookie", notes = "该账号首次登陆accountVersion=0，" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/cookietest", method = RequestMethod.POST)
	public ResultDataSet cookietest() {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.getRequest();
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		// rds.setData(request.getCookies());
		return rds;
	}
	
	@ApiOperation(value="扫码登陆")
	@RequestMapping(value="/account/loginByQrCode", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet loginByQrCode(@ApiParam(value = "") @RequestParam(value = "qrString") String qrString){
		ResultDataSet rds = new ResultDataSet();
		rds = accountLoginForJWT.loginByQrCode(HttpUtils.getAccIdFromHeader(), qrString);
		return rds;
	}
}
