package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountRecord;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 获取历史记录
 * 
 * @author songfl
 */

@Api(value = "历史记录服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountRecordRestful {
	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountRecord accountRecord;

	@ApiOperation(value = "查询充值记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryPayRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryPayRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryPayRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查询礼物消费记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryGiftRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGiftRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryGiftRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "获取非礼物消费记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryConsumptionRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryConsumptionRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryConsumptionRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查询vip购买记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryVipRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryVipRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryVipRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查询守护购买记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryGuardRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGuardRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryGuardRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "查询开通粉丝记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryFansClubRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryFansClubRecord(@ApiParam(value = "") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "") @RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryFansClubRecord(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		return rds;
	}

	@ApiOperation(value = "根据查询充值记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryRegisterListByOwnerId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryRegisterListByOwnerId(@ApiParam(value = "") @RequestParam(value = "ownerId") String ownerId,
			@ApiParam(value = "") @RequestParam(value = "startTime") long startTime,
			@ApiParam(value = "") @RequestParam(value = "endTime") long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryRegisterListByOwnerId(ownerId, startTime, endTime);
		return rds;
	}
	
	@ApiOperation(value = "根据查询充值记录", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryRegisterList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryRegisterListByIp(@ApiParam(value = "") @RequestParam(value = "startTime") long startTime,
			@ApiParam(value = "") @RequestParam(value = "endTime") long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountRecord.queryRegisterListByOwnerId(HttpUtils.getAccIdFromHeader(), startTime, endTime);
		return rds;
	}
}
