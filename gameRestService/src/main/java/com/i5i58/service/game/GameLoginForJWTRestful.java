package com.i5i58.service.game;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.game.IGame;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "游戏服务-登录v1")
@RestController
@RequestMapping("v1")
public class GameLoginForJWTRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	@Reference
	private IGame game;

	@ApiOperation(value = "用户ID游戏登陆", notes = "")
	@RequestMapping(value = "/game/loginByOpenIdForGame", method = RequestMethod.POST)
	public ResultDataSet loginByOpenIdForGame(@ApiParam(value = "OpenId") @RequestParam(value = "openId") String openId,
			@ApiParam(value = "密码，需要MD5加密") @RequestParam(value = "password") String password,
			@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.loginByOpenIdForGame(openId, password, appKey, HttpUtils.getRealClientIpAddress(), device,
					serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "手机游戏登陆", notes = "")
	@RequestMapping(value = "/game/loginByPhoneNoForGame", method = RequestMethod.POST)
	public ResultDataSet loginByPhoneNoForGame(
			@ApiParam(value = "手机号码") @RequestParam(value = "phoneNo") String phoneNo,
			@ApiParam(value = "密码，需要MD5加密") @RequestParam(value = "password") String password,
			@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.loginByPhoneNoForGame(phoneNo, password, appKey, HttpUtils.getRealClientIpAddress(), device,
					serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "token游戏登陆", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/loginByTokenForGame", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet loginByTokenForGame(
			@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.loginByTokenForGame(HttpUtils.getAccIdFromHeader(), appKey, HttpUtils.getRealClientIpAddress(),
					device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}