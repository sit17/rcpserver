package com.i5i58.service.im;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.im.IIMp2p;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Web Restful Service for IM management
 * @author songfl
 */

@Api(value = "IM-好友服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class IMp2pRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IIMp2p accountIM;
	
	@ApiOperation(value = "添加好友", notes="成功返回账号JSON数据")
	@RequestMapping(value="/im/addFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet addFriendRequest(@RequestParam(value = "faccid") String faccid){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.addFriendRequest(accId, faccid);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "同意别人添加好友的请求", notes="成功返回账号JSON数据")
	@RequestMapping(value = "/im/agreeFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet agreeFriendRequest(@RequestParam(value="faccid") String faccid){
		ResultDataSet rds = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			// 注意这里的参数顺序
			rds = accountIM.agreeFriendRequest(faccid, accId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "拒绝别人添加好友的请求", notes="成功返回账号JSON数据")
	@RequestMapping(value = "/im/disagreeFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet disagreeFriendRequest(@RequestParam(value="faccid") String faccid){
		ResultDataSet rds = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.disagreeFriendRequest(accId, faccid);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "把好友移动到黑名单", notes="成功返回账号JSON数据")
	@RequestMapping(value = "/im/pullBlack", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet pullBlackRequest(@RequestParam(value="targetAccId") String targetAccId){
		ResultDataSet rds = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.pullBlackRequest(accId, targetAccId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "把好友移出黑名单", notes="成功返回账号JSON数据")
	@RequestMapping(value = "/im/CancelPullBlack", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet CancelPullBlackRequest(@RequestParam(value="targetAccId") String targetAccId){
		ResultDataSet rds = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.CancelPullBlackRequest(accId, targetAccId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量获取用户信息", notes="成功后，以JSON数组形式返回用户的详细信息")
	@RequestMapping(value = "/im/getAccount", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet getAccount(@RequestParam(value="queryAccIds") String queryAccIds){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = accountIM.getAccount(queryAccIds);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "删除好友", notes="成功返回账号JSON数据")
	@RequestMapping(value="/im/deleteFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet deleteFriendRequest(@RequestParam(value = "faccid") String faccid){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.deleteFriendRequest(accId, faccid);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "更新好友", notes="成功返回账号JSON数据")
	@RequestMapping(value="/im/updateFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet updateFriendRequest(@RequestParam(value = "faccid") String faccid,
											 @RequestParam(value = "alias") String alias){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.updateFriendRequest(accId, faccid, alias);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "获取好友", notes="成功返回账号JSON数据")
	@RequestMapping(value="/im/getFriend", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet getFriendRequest(@RequestParam(value = "createTime") String createTime){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.getFriendRequest(accId, createTime);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "获取黑名单和静音列表", notes="成功返回账号JSON数据")
	@RequestMapping(value="/im/listBlackAndMuteList", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet listBlackAndMuteListRequest(){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = accountIM.listBlackAndMuteListRequest(accId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
