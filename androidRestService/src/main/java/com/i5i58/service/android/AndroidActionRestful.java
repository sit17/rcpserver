package com.i5i58.service.android;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.android.IAndroidAction;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Web Restful Service for Account Authentication
 * 
 * @author frank
 *
 */

@Api(value = "账户-身份认证服务")
@RestController
public class AndroidActionRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAndroidAction androidAction;

	@ApiOperation(value = "添加机器人账号", notes = "")
	@RequestMapping(value = "/android/addAndroidAccount", method = RequestMethod.POST)
	// @Authorization
	public ResultDataSet addAndroidAccount(@RequestParam(value = "nickName") String nickName,
			@RequestParam(value = "gameName") String gameName, @RequestParam(value = "gender") byte gender,
			@RequestParam(value = "location") String location, @RequestParam(value = "faceUrl") String faceUrl) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.addAndroidAccount(nickName, gameName, gender, location,
					faceUrl);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "机器人ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/android/androidLogin", method = RequestMethod.POST)
	public ResultDataSet loginByOpenId(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "password") String password) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.androidLogin(accId, password);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢机器人賬號列表", notes = "")
	@RequestMapping(value = "/android/getAndroidList", method = RequestMethod.POST)
	public ResultDataSet getAndroidList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = androidAction.getAndroidList(pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "查詢频道列表", notes = "")
	@RequestMapping(value = "/android/getChannelList", method = RequestMethod.POST)
	public ResultDataSet getChannelList(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = androidAction.getChannelList(pageNum, pageSize);
		return rds;
	}

	@ApiOperation(value = "进入频道", notes = "")
	@RequestMapping(value = "/android/enterChannel", method = RequestMethod.POST)
	public ResultDataSet enterChannel(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.enterChannel(accId, cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "退出频道", notes = "")
	@RequestMapping(value = "/android/exitChannel", method = RequestMethod.POST)
	public ResultDataSet exitChannel(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.exitChannel(accId, cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取频道机器人观众列表", notes = "")
	@RequestMapping(value = "/android/getAndroidViewerList", method = RequestMethod.POST)
	public ResultDataSet getAndroidViewerList(@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.getAndroidViewerList(pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "赠送礼物", notes = "")
	@RequestMapping(value = "/android/giveGift", method = RequestMethod.POST)
	public ResultDataSet giveGift(@RequestParam(value = "accId") String accId, @RequestParam(value = "cId") String cId,
			@RequestParam(value = "giftId") Integer giftId, @RequestParam(value = "giftCount") Integer giftCount,
			@RequestParam(value = "continuous") Integer continuous) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.giveGift(accId, cId, giftId, giftCount, continuous);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "关注频道", notes = "")
	@RequestMapping(value = "/android/followInChannel", method = RequestMethod.POST)
	public ResultDataSet followInChannel(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "attAccId") String attAccId, @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.followInChannel(accId, attAccId, cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "赠送星光", notes = "")
	@RequestMapping(value = "/android/giveHeart", method = RequestMethod.POST)
	public ResultDataSet giveHeart(@RequestParam(value = "attAccId") String attAccId,
			@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = androidAction.giveHeart(attAccId, cId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
