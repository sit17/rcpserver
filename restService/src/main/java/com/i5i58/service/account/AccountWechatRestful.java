package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountWechat;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformWechat;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "账户-微信专用服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountWechatRestful {
	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountWechat wechat;	
	
	@ApiOperation(value = "绑定微信公众号", notes = "成功返回用户对象，失败：验证码错误")
	@RequestMapping(value = "/account/bindWechatAccount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet bindWechatAccount(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "openId") String openId) {

		ResultDataSet rds = new ResultDataSet();
		try {
			rds = wechat.bindWechatAccount(accId, openId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "设定绑定的默认平台账号", notes = "成功返回用户对象，失败：验证码错误")
	@RequestMapping(value = "/account/setCurWechatAccount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setCurWechatAccount(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "openId") String openId) {

		ResultDataSet rds = new ResultDataSet();
		try {
			rds = wechat.setCurWechatAccount(accId, openId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "通过openid查找accid", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getAccIdByOpenId", method = RequestMethod.POST)
	public ResultDataSet getAccIdByOpenId(@ApiParam(value = "OpenId") @RequestParam(value = "OpenId") String OpenId) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = wechat.getAccIdByOpenId(OpenId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "抽奖", notes = "")
	@RequestMapping(value = "/account/lottery", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet lottery(@RequestParam(value = "serialNum") String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		rds =wechat.lottery(HttpUtils.getAccIdFromHeader(),HttpUtils.getRealClientIpAddress(),serialNum);
		return rds;
	}
	
	@ApiOperation(value = "查询用户抽奖机会", notes = "返回Json数据")
	@RequestMapping(value = "/account/queryLotteryChance", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryLotteryChance(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
			rds = wechat.queryLotteryChance(accId);
			return rds;
	}
	
	@ApiOperation(value = "查询奖品", notes = "返回Json数据")
	@RequestMapping(value = "/account/queryAwardConfig", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAwardConfig(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
			rds = wechat.queryAwardConfig(pageSize,pageNum);
			return rds;
	}
	
	@ApiOperation(value = "查询用户抽奖记录表", notes = "返回Json数据")
	@RequestMapping(value = "/account/queryAwardOpeRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAwardOpeRecord(@RequestParam(value = "accId") String accId) {

		ResultDataSet rds = new ResultDataSet();
			rds = wechat.queryAwardOpeRecord(accId);
		return rds;
	}
	
}
