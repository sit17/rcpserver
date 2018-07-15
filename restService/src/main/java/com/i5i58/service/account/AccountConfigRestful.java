package com.i5i58.service.account;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountConfig;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Web Restful Service for Account config
 * 
 * @author hexiaoming
 *
 */

@Api(value = "账户-设置服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountConfigRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	@Reference
	private IAccountConfig accountConfig;

	@ApiOperation(value = "直播时是否通知", notes = "成功返回设置JSON数据")
	@RequestMapping(value = "/account/updateLivingNotify", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet updateLivingNotify(@RequestParam(value = "notifyEnable") boolean notifyEnable) {
		ResultDataSet rds = new ResultDataSet();
		rds = accountConfig.updateLivingNotify(notifyEnable, HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "免打扰设置", notes = "成功返回设置JSON数据")
	@RequestMapping(value = "/account/updateNoDisturb", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet updateNoDisturb(@RequestParam(value = "notifyEnable") boolean notifyEnable) {
		ResultDataSet rds = new ResultDataSet();
		rds = accountConfig.updateNoDisturb(notifyEnable, HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "查询用户设置", notes = "成功返回设置JSON数据")
	@RequestMapping(value = "/account/queryAccountConfig", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAccountConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = accountConfig.queryAccountConfig(HttpUtils.getAccIdFromHeader());
		return rds;
	}
	
	@ApiOperation(value = "编辑头像", notes = "")
	@RequestMapping(value = "/account/editFace", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editFace(@RequestParam(value = "headUrlSmall") String headUrlSmall,
			@RequestParam(value = "headUrlOrigin") String headUrlOrigin) {
		ResultDataSet rds = new ResultDataSet();
		rds = accountConfig.editFaceImage(HttpUtils.getAccIdFromHeader(), headUrlSmall, headUrlOrigin);
		return rds;
	}
}
