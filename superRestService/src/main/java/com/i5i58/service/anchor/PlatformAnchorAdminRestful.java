package com.i5i58.service.anchor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAnchorAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "平台主播管理服务")
@RestController
public class PlatformAnchorAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPlatformAnchorAdmin platformAnchorAdmin;

	@ApiOperation(value = "主播实名认证审核", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/verifyAnchorAuth", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet checkAnchorAuthentication(@RequestParam(value = "accid") String accid,
			@RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAnchorAdmin.verifyAnchorAuth(HttpUtils.getAccIdFromHeader(), accid, agree);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询主播列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/queryAnchorList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAnchorList(@RequestParam(value = "param") String param,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAnchorAdmin.queryAnchorList(param, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询主播认证列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/queryAnchorVerifyList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAnchorVerifyList(@RequestParam(value = "sortDir") String sortDir,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAnchorAdmin.queryAnchorVerifyList(sortDir, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取主播信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/getAnchorInfo", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAnchorInfo(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAnchorAdmin.getAnchorInfo(accId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取主播所属公会和所拥有的频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/getAnchorChannelAndGroup", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAnchorChannelAndGroup(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformAnchorAdmin.getAnchorChannelAndGroup(accId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询一段时间内主播开播记录", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/super/getAnchorPushRecord", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAnchorPushRecord(@ApiParam(value = "用户accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getAnchorPushRecord(accId, fromTime, toTime);
		return rds;
	}
	
	@ApiOperation(value = "统计主播一段时间内的直播总时长", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/super/calcAnchorActiveTime", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet calcAnchorActiveTime(@ApiParam(value = "用户accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.calcAnchorActiveTime(accId, fromTime, toTime);
		return rds;
	}
	
	
	@ApiOperation(value = "获得主播佣金信息", notes = "按照佣金Commission降序排列")
	@RequestMapping(value = "/super/getAnchorCommissionInfo", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet getAnchorCommissionInfo(
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getAnchorCommissionInfo(pageSize,pageNum);
		return rds;
	}

	
	@ApiOperation(value = "获取主播提现信息", notes = "按照状态Status返回信息列表")
	@RequestMapping(value = "/super/getAnchorWithdrawCashsInfo", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet getAnchorWithdrawCashsInfo(
			@ApiParam(value="0 主播提出申请，1 后台处理中, 2 处理完成, 3 出错了") @RequestParam(value = "status") int status,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getAnchorWithdrawCashsInfo(status,pageSize,pageNum);
		return rds;
	}

	@ApiOperation(value = "处理提现", notes = "")
	@RequestMapping(value = "/super/processWithdrawCash", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.FIANCE_AUTH)
	public ResultDataSet processWithdrawCash(
			@RequestParam(value = "withdrawId") String withdrawId, 
			@ApiParam(value="0 主播提出申请，1 后台处理中, 2 处理完成, 3 出错了") @RequestParam(value = "status") int status) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.processWithdrawCash(HttpUtils.getAccIdFromHeader(), withdrawId, status);
		return rds;
	}
	
	@ApiOperation(value = "查询主播状态", notes = "")
	@RequestMapping(value = "/super/queryAnchorStatus", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.BASE_AUTH)
	public ResultDataSet queryAnchorStatus(@ApiParam(value="主播id") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.queryAnchorStatus(HttpUtils.getAccIdFromHeader(),accId);
		return rds;
	}
	@ApiOperation(value = "指定时间段，查询主播的守护开通情况", notes = "")
	@RequestMapping(value = "/super/queryOpenGuardRecord", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryOpenGuardRecord(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "开始时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "结束时间，毫秒") @RequestParam(value = "toTime") long toTime,
			@ApiParam(value = "分页序号,从0开始") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "分页大小,必须大于0") @RequestParam(value = "pageSize") int pageSize){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.queryOpenGuardRecord(accId, fromTime, toTime, pageNum, pageSize);
		return rds;
	}
	
	@ApiOperation(value = "获取当月新增守护数量", notes = "")
	@RequestMapping(value = "/super/getIncreasedGuardCountCurMonth", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getIncreasedGuardCountCurMonth(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "开始时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "结束时间，毫秒") @RequestParam(value = "toTime") long toTime){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getIncreasedGuardCount(accId, fromTime, toTime);
		return rds;
	}
	
	@ApiOperation(value = "获取守护总数量", notes = "")
	@RequestMapping(value = "/super/getTotalGuardCount", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getTotalGuardCount(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getTotalGuardCount(accId);
		return rds;
	}
	
	@ApiOperation(value = "指定时间段，查询粉丝团开通情况", notes = "")
	@RequestMapping(value = "/super/queryOpenClubRecord", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryOpenClubRecord(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "开始时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "结束时间，毫秒") @RequestParam(value = "toTime") long toTime,
			@ApiParam(value = "分页序号,从0开始") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "分页大小,必须大于0") @RequestParam(value = "pageSize") int pageSize){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.queryOpenClubRecord(accId, fromTime, toTime, pageNum, pageSize);
		return rds;
	}
	
	@ApiOperation(value = "查询当月新增粉丝团人数", notes = "")
	@RequestMapping(value = "/super/getIncreasedClubMemberCount", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getIncreasedClubMemberCount(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "开始时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "结束时间，毫秒") @RequestParam(value = "toTime") long toTime){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getIncreasedClubMemberCount(accId, fromTime, toTime);
		return rds;
	}
	
	@ApiOperation(value = "查询粉丝团总人数", notes = "")
	@RequestMapping(value = "/super/getTotalClubMemberCount", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getTotalClubMemberCount(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getTotalClubMemberCount(accId);
		return rds;
	}
	
	@ApiOperation(value = "获取主播频道信息", notes = "")
	@RequestMapping(value = "/super/getAnchorChannel", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getAnchorChannel(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.getAnchorChannel(accId);
		return rds;
	}
	
	@ApiOperation(value = "超管分配主播的经纪公司-公会-频道", notes = "")
	@RequestMapping(value = "/super/assignChannel", method = RequestMethod.POST)
	@SuperAuthorization(value=SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet assignChannel(@ApiParam(value = "主播id") @RequestParam(value = "accId") 	String accId,
			@ApiParam(value = "经济公司合约id") 	@RequestParam(value = "fId") 						String fId, 
			@ApiParam(value = "公会id") 			@RequestParam(value = "gId") 						String gId, 
			@ApiParam(value = "") 				@RequestParam(value = "cId") 						String cId, 
			@ApiParam(value = "频道类型1:潮音乐,2:燃舞蹈,3:脱口秀,1024:热门,1000:内部测试") @RequestParam(value = "type") 	int type,
			@ApiParam(value = "分成比例") 		@RequestParam(value = "groupRate") 					int groupRate, 
			@ApiParam(value = "") 				@RequestParam(value = "settleMode") 				int settleMode){
		ResultDataSet rds = new ResultDataSet();
		rds = platformAnchorAdmin.assignChannel(HttpUtils.getAccIdFromHeader(),	accId, fId, gId, cId, type, groupRate, settleMode);
		return rds;
	}
}
