package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountVipConfig;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author cw
 *
 */
@Api(value = "账户-VIP服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountVipConfigRestful {
	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountVipConfig accountVipConfig;

	@ApiOperation(value = "获取VIP等级信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getAccountVipConfig", method = org.springframework.web.bind.annotation.RequestMethod.GET)
	public ResultDataSet getAccountVipConfig() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountVipConfig.getAccountVipConfig();
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "购买VIP", notes = "")
	@RequestMapping(value = "/account/buyAccountVip", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet buyAccountVip(@RequestParam(value = "level") int level,
			@RequestParam(value = "month") int month) {
		ResultDataSet rds = new ResultDataSet();
		rds = accountVipConfig.buyAccountVip(HttpUtils.getAccIdFromHeader(), level, HttpUtils.getRealClientIpAddress(),
				month);
		return rds;
	}

	@ApiOperation(value = "升级VIP", notes = "")
	@RequestMapping(value = "/account/upgradeAccountVip", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet upgradeAccountVip(@RequestParam(value = "level") int level) {
		ResultDataSet rds = new ResultDataSet();
		rds = accountVipConfig.upgradeAccountVip(HttpUtils.getAccIdFromHeader(), level, HttpUtils.getRealClientIpAddress());
		return rds;
	}
}
