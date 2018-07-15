package com.i5i58.service.account;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountRegister;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.HttpUtils;
import com.i5i58.util.web.IpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Web Restful Service for Register Account
 * 
 * @author frank
 *
 */
@Api(value = "账户-注册服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountRegisterRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IAccountRegister registerAccount;

	@ApiOperation(value = "用户注册，密码使用MD5加密", notes = "成功返回用户对象，失败：验证码错误")
	@RequestMapping(value = "/account/register", method = RequestMethod.POST)
	public ResultDataSet registerAccount(@RequestParam(value = "phoneNo") String phoneNo,
			@RequestParam(value = "password") String password, @RequestParam(value = "verifCode") String verifCode,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = registerAccount.registerAccount(phoneNo, password, clientIP, verifCode, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "临时用户注册，密码使用MD5加密", notes = "成功返回用户对象，失败：验证码错误")
	@RequestMapping(value = "/account/registertemp", method = RequestMethod.POST)
	public ResultDataSet registerAccountTemp(@RequestParam(value = "phoneNo") String phoneNo,
			@RequestParam(value = "password") String password, @RequestParam(value = "verifCode") String verifCode,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum,
			@RequestParam(value = "realName") String realName, @RequestParam(value = "idCard") String idCard) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = registerAccount.registerAccountTemp(phoneNo, password, clientIP, verifCode, device, serialNum,
					realName, idCard);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "用户注册，发送手机验证码", notes = "测试验证码为888888")
	@RequestMapping(value = "/account/sendCode", method = RequestMethod.POST)
	public ResultDataSet sendCode(@ApiParam(value = "手机号码") @RequestParam(value = "phoneNo") String phoneNo) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = registerAccount.sendCode(phoneNo);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "验证码检测", notes = "点击下一步时")
	@RequestMapping(value = "/account/checkCode", method = RequestMethod.POST)
	public ResultDataSet checkCode(@RequestParam(value = "phoneNo") String phoneNo,
			@RequestParam(value = "verifCode") String verifCode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = registerAccount.checkCode(phoneNo, verifCode);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "手机号码账号检测", notes = "")
	@RequestMapping(value = "/account/checkPhoneNo", method = RequestMethod.POST)
	public ResultDataSet checkPhoneNo(@RequestParam(value = "phoneNo") String phoneNo) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = registerAccount.checkPhoneNo(phoneNo);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
