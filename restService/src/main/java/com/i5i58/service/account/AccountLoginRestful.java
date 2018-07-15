package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountLogin;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author frank
 *
 */
@Api(value = "账户-登陆服务")
@RestController
public class AccountLoginRestful {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IAccountLogin loginAccount;

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "该账号首次登陆accountVersion=0，" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByOpenId", method = RequestMethod.POST)
	public ResultDataSet loginByOpenId(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "password") String password, @RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = loginAccount.loginByOpenId(openId, password, version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "手机登陆，密码使用MD5加密", notes = "该账号首次登陆accountVersion=0，" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByPhoneNo", method = RequestMethod.POST)
	public ResultDataSet loginByPhoneNo(@RequestParam(value = "phoneNo") String phoneNo,
			@RequestParam(value = "password") String password, @RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = loginAccount.loginByPhoneNo(phoneNo, password, version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "token登陆", notes = "" + "若返回数据data不为空将账户信息存在本地，若为空则使用本地账户数据")
	@RequestMapping(value = "/account/loginByToken", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet loginByToken(@RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = loginAccount.loginByToken(HttpUtils.getAccIdFromHeader(), version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "第三方登陆", notes = "")
	@RequestMapping(value = "/account/loginByThird", method = RequestMethod.POST)
	public ResultDataSet loginByThird(@ApiParam(value = "第三方类型,微信1，qq2，微博3") @RequestParam(value = "third") int third,
			@ApiParam(value = "第三方ID") @RequestParam(value = "thirdId") String thirdId,
			@ApiParam(value = "昵称") @RequestParam(value = "name") String name,
			@ApiParam(value = "头像地址") @RequestParam(value = "face") String face,
			@ApiParam(value = "性别") @RequestParam(value = "gender") byte gender,
			@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = loginAccount.loginByThird(third, thirdId, name, face, gender, HttpUtils.getRealClientIpAddress(),
					device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "第三方登陆", notes = "")
	@RequestMapping(value = "/account/loginByThirdV1", method = RequestMethod.POST)
	public ResultDataSet loginByThird(@ApiParam(value = "第三方类型,微信1，qq2，微博3") @RequestParam(value = "third") int third,
			@ApiParam(value = "第三方openId") @RequestParam(value = "openId") String openId,
			@ApiParam(value = "第三方uId") @RequestParam(value = "uId") String uId,
			@ApiParam(value = "第三方unionId") @RequestParam(value = "unionId") String unionId,
			@ApiParam(value = "昵称") @RequestParam(value = "name") String name,
			@ApiParam(value = "头像地址") @RequestParam(value = "face") String face,
			@ApiParam(value = "性别") @RequestParam(value = "gender") byte gender,
			@ApiParam(value = "设备") @RequestParam(value = "device") int device,
			@ApiParam(value = "序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = loginAccount.loginByThird1(third, openId, uId, unionId, name, face, gender,
					HttpUtils.getRealClientIpAddress(), device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value="扫码登陆")
	@RequestMapping(value="/account/loginByQrCode", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet loginByQrCode(@ApiParam(value = "") @RequestParam(value = "qrString") String qrString){
		ResultDataSet rds = new ResultDataSet();
		rds = loginAccount.loginByQrCode(HttpUtils.getAccIdFromHeader(), qrString);
		return rds;
	}
}
