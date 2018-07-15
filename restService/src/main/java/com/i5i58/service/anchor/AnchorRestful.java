package com.i5i58.service.anchor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.anchor.IAnchor;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "主播-服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AnchorRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAnchor anchor;

	@ApiOperation(value = "查询我的合约", notes = "")
	@RequestMapping(value = "/anchor/queryMyContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryContractByAccId(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.queryContractByAccId(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "主播向公会回应签约", notes = "")
	@RequestMapping(value = "/anchor/responseContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet responseContract(@RequestParam(value = "ctId") String ctId,
			@RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.responseContract(ctId, agree);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "主播向公会回应签约", notes = "")
	@RequestMapping(value = "/anchor/requestContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet requestContract(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "groupRate") int groupRate, @RequestParam(value = "endDate") long endDate,
			@RequestParam(value = "settleMode") int settleMode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.requestContract(HttpUtils.getAccIdFromHeader(), gId, groupRate, endDate, settleMode);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取我的频道for推流", notes = "")
	@RequestMapping(value = "/anchor/getMyPush", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyPush() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getMyPush(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取我的频道for推流-移動專用", notes = "")
	@RequestMapping(value = "/anchor/getMyPushMobile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyPushMobile(@ApiParam(value = "设备类型") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备串码") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "设备型号") @RequestParam(value = "model") String model,
			@ApiParam(value = "系统版本") @RequestParam(value = "osVersion") String osVersion) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getMyPushMobile(HttpUtils.getAccIdFromHeader(), device, serialNum, model, osVersion);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "提现", notes = "")
	@RequestMapping(value = "/anchor/withdrawCash", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet withdrawCash(@ApiParam(value = "提现金额") @RequestParam(value = "amount") long amount) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.withdrawCash(HttpUtils.getAccIdFromHeader(), amount, HttpUtils.getRealClientIpAddress());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "佣金兑换钻石", notes = "")
	@RequestMapping(value = "/anchor/salaryExchangeToDiamond", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet salaryExchangeToDiamond(
			@ApiParam(value = "兑换钻石数量") @RequestParam(value = "diamond") long diamond) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.commissionExchangeToDiamond(HttpUtils.getAccIdFromHeader(), diamond, HttpUtils.getRealClientIpAddress());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询提现", notes = "")
	@RequestMapping(value = "/anchor/queryWithdrawCash", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryWithdrawCash(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.queryWithdrawCash(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "俸禄交易明细", notes = "")
	@RequestMapping(value = "/anchor/getCommissionExchangeDetail", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getCommissionExchangeDetail() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getCommissionExchangeDetail(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询主播佣金信息", notes = "")
	@RequestMapping(value = "/anchor/getCommissionInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getCommissionInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getCommissionInfo(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取骑士/佣金等级信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/anchor/getGuardCommissionConfigAll", method = org.springframework.web.bind.annotation.RequestMethod.GET)
	public ResultDataSet getGuardCommissionConfigAll() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getGuardCommissionConfigAll();
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取绑定银行卡信息", notes = "1111111********1111")
	@RequestMapping(value = "/anchor/getAnchorBankInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getAnchorBankInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = anchor.getAnchorBankInfo(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询一段时间内主播开播记录", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/anchor/getPushRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getPushRecord(@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = anchor.getPushRecord(HttpUtils.getAccIdFromHeader(), fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "统计主播一段时间内的直播总时长", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/anchor/calcActiveTime", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet calcActiveTime(
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = anchor.calcActiveTime(HttpUtils.getAccIdFromHeader(), fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "按购买时间,获取主播守护", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/anchor/queryGuardByTime", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGuardByTime(
			@ApiParam(value = "购买期限，开始时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "购买期限，开始时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = anchor.queryGuardByTime(HttpUtils.getAccIdFromHeader(), fromTime, toTime);
		return rds;
	}
	
	@ApiOperation(value = "获取主播所属公会信息", notes = "header 中需要accId, token")
	@RequestMapping(value = "/anchor/queryMyTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryMyTopGroup() {
		ResultDataSet rds = new ResultDataSet();
		rds = anchor.queryMyTopGroup(HttpUtils.getAccIdFromHeader());
		return rds;
	}
	
	@ApiOperation(value = "主播强制解约", notes = "cancelDirection:0:主播发起，1：公会发起")
	@RequestMapping(value = "/anchor/forceCancelContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet forceCancelContract(@ApiParam(value = "合约id") @RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		rds = anchor.forceCancelContract(HttpUtils.getAccIdFromHeader(),ctId);
		return rds;
	}
}
