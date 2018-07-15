package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountBehavior;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Web Restful Service for Account Behavior
 * 
 * @author frank
 *
 */
@Api(value = "账户-用户行为")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountBehaviorRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountBehavior accountBehavior;

	@ApiOperation(value = "添加/更新收藏频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/addCollectChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet addCollectChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.addCollectChannel(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取收藏频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getCollectChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getCollectChannel(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.getCollectChannel(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "取消收藏频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/cancelCollectChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet cancelCollectChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.cancelCollectChannel(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "判断频道是否被收藏", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/isChannelCollected", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet isChannelCollected(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.isChannelCollected(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加/更新最近观看频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/addNearWatchChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet addNearWatchChannel(@ApiParam(value = "频道cId") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.addNearWatchChannel(HttpUtils.getAccIdFromHeader(), cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取最近观看频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getNearWatchChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getNearWatchChannel(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.getNearWatchChannel(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加/更新我的游戏", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/addMyGame", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet addMyGame(@ApiParam(value = "游戏kindId") @RequestParam(value = "kindId") int kindId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.addMyGame(HttpUtils.getAccIdFromHeader(), kindId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取我的游戏", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getMyGame", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyGame(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountBehavior.getMyGame(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "举报主播/用户", notes = "")
	@RequestMapping(value = "/account/reportUser", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet reportUser(@ApiParam(value="被举报的用户id") @RequestParam(value = "reportedUser") String reportedUser,
			@ApiParam(value="举报内容描述") @RequestParam(value = "reason") String reason) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountBehavior.reportUser(HttpUtils.getAccIdFromHeader(), reportedUser, reason);
		return rds;
	}
	
	@ApiOperation(value = "游戏中是否使用直播头像", notes = "")
	@RequestMapping(value = "/account/useFaceInGame", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet useFaceInGame(@ApiParam(value="是否使用直播头像") @RequestParam(value = "useFace") int useFace) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = accountBehavior.useFaceInGame(HttpUtils.getAccIdFromHeader(), useFace);
		return rds;
	}
}
