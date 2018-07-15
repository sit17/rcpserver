package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountSecurity;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Web Restful Service for Account Security
 * 
 * @author hexiaoming
 *
 */
@Api(value = "账户-安全服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountSecurityRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	@Reference
	private IAccountSecurity security;
	
	@ApiOperation(value="通过账号查询密码",notes="成功返回账号JSON数据")
	@RequestMapping(value="/account/queueForgotPassword",method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public ResultDataSet queueForgotPassword(@RequestParam(value="type") String type,@RequestParam(value = "account") String account){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.queueForgotPassword(type, account);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
		
	}
	
	
	@ApiOperation(value="重置密码",notes="成功返回账号JSON数据")
	@RequestMapping(value="/account/resetPassword",method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public ResultDataSet resetPassword(
			@ApiParam(value="账号类型,可以是accId,openId,phoneNo或email") @RequestParam(value = "type") String type,
			@ApiParam(value="账号内容") @RequestParam(value = "account") String account,
			@ApiParam(value="手机验证码") @RequestParam(value = "verifyCode") String verifyCode,
			@ApiParam(value="新的登录密码") @RequestParam(value = "password") String password
			){
		ResultDataSet rds = new ResultDataSet();
		
		try {
			rds = security.resetPassword(type, account, verifyCode, password);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
		
	}
	
	
	@ApiOperation(value = "重置密码的时候，发送手机验证码", notes = "测试验证码为888888")
	@RequestMapping(value = "/account/sendVerifToPhone", method = RequestMethod.POST)
	public ResultDataSet sendVerifToPhone(
			@ApiParam(value="账号类型,可以是accId,openId,phoneNo或email") @RequestParam(value = "type") String type,
			@ApiParam(value="账号内容") @RequestParam(value = "account") String account) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.sendVerifToPhone(type, account);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "重置密码的时候，检查验证码是否正确", notes = "返回 data : {valid=true/false}")
	@RequestMapping(value = "/account/checkVerifyCode", method = RequestMethod.POST)
	public ResultDataSet checkVerifyCode(
			@ApiParam(value="账号类型,可以是accId,openId,phoneNo或email") @RequestParam(value = "type") String type,
			@ApiParam(value="账号内容") @RequestParam(value = "account") String account,
			@ApiParam(value="验证码") @RequestParam(value = "verifyCode") String verifyCode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.isValidVerifyCode(type, account, verifyCode);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "验证登录密码", notes = "")
	@RequestMapping(value = "/account/checkLoginPassword", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet checkLoginPassword(@RequestParam(value = "password") String password) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.checkLoginPassword(HttpUtils.getAccIdFromHeader(), password);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "验证绑定手机号", notes = "")
	@RequestMapping(value = "/account/verifyBindMobile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet verifyBindMobile(@RequestParam(value = "bindMobile") String bindMobile) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.verifyBindMobile(HttpUtils.getAccIdFromHeader(),bindMobile);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
