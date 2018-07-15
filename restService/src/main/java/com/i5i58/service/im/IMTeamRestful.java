package com.i5i58.service.im;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.im.IIMTeam;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Web Restful Service for IM Team management
 * @author songfl
 */

@Api(value="IM-群组服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class IMTeamRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * Call Dubbo Service
	 */
	@Reference
	private IIMTeam imTeam;
	
	@ApiOperation(value = "创建群组", notes = "成功返回群ID")
	@RequestMapping(value = "/im/createTeam", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet createTeam(@RequestParam(value = "tname") 		String tname,
									@RequestParam(value = "members") 	String members,
									@RequestParam(value = "msg") 		String msg,
									@RequestParam(value = "magree") 	String magree,
									@RequestParam(value = "joinMode") 	String joinMode,
									@RequestParam(value = "custom") 	String custom){
		ResultDataSet rds  = new ResultDataSet();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String clientIp = request.getRemoteAddr();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.create(accId, tname, members, msg, magree, joinMode, custom, clientIp);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "解散群组", notes = "成功返回JSON数据")
	@RequestMapping(value = "/im/removeTeam", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet removeTeam(@RequestParam(value = "tid") 		String tid){
		ResultDataSet rds  = new ResultDataSet();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String clientIp = request.getRemoteAddr();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.remove(accId, tid, clientIp);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量查询群组信息", notes = "成功返回群组信息")
	@RequestMapping(value = "/im/queryTeam", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet queryTeam(@RequestParam(value = "tids") 	String tids,
									@RequestParam(value = "ope") 	String ope){
		ResultDataSet rds  = new ResultDataSet();
		try {
			rds = imTeam.query(tids, ope);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "移交群", notes = "成功返回JSON")
	@RequestMapping(value = "/im/changeTeamOwner", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet changeOwner(@RequestParam(value = "tid") 		String tid,
									@RequestParam(value = "newOwner") 	String newOwner){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.changeOwner(accId, tid, newOwner);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量添加群成员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/addTeamMembers", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet addTeamMembers(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "owner") 	String owner,
										@RequestParam(value = "members") 	String members,
										@RequestParam(value = "magree") 	String magree,
										@RequestParam(value = "msg") 	String msg,
										@RequestParam(value = "attach") 	String attach){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.addTeamMembers(accId, tid, owner, members, magree, msg, attach);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "踢人", notes = "成功返回JSON")
	@RequestMapping(value = "/im/kickTeamMemeber", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet kickTeamMemeber(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "owner") 	String owner,
										@RequestParam(value = "member") 	String member,
										@RequestParam(value = "attach") 	String attach){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.kickTeamMemeber(accId, tid, owner, member, attach);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "更新群信息", notes = "成功返回JSON")
	@RequestMapping(value = "/im/updateTeam", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet updateTeam(@RequestParam(value = "tid") 			String tid,
									@RequestParam(value = "tname") 			String tname,
									@RequestParam(value = "owner") 			String owner,
									@RequestParam(value = "announcement") 	String announcement,
									@RequestParam(value = "intro") 			String intro,
									@RequestParam(value = "joinMode") 		String joinMode,
									@RequestParam(value = "custom") 		String custom,
									@RequestParam(value = "icon") 			String icon,
									@RequestParam(value = "beinvteMode") 	String beinvteMode,
									@RequestParam(value = "inviteMode") 	String inviteMode,
									@RequestParam(value = "uptinfoMode") 	String uptinfoMode,
									@RequestParam(value = "upcustomMode") 	String upcustomMode){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.updateTeam(accId, tid, tname, owner, announcement, intro, joinMode, 
					custom, icon, beinvteMode, inviteMode, uptinfoMode, upcustomMode);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "批量添加管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/addTeamManager", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet addTeamManager(@RequestParam(value = "tid") 		String tid,
										@RequestParam(value = "owner") 		String owner,
										@RequestParam(value = "members") 	String members){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.addTeamManager(accId, tid, owner, members);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量移除管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/removeTeamManager", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet removeTeamManager(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "owner") 		String owner,
										@RequestParam(value = "members") 	String members){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.removeTeamManager(accId, tid, owner, members);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询已经加入群", notes = "成功返回JSON")
	@RequestMapping(value = "/im/queryUserJoinedTeamsInfo", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet queryUserJoinedTeamsInfo(){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.queryUserJoinedTeamsInfo(accId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量移除管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/updateTeamNick", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet updateTeamNick(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "owner") 	String owner,
										@RequestParam(value = "nick") 	String nick){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.updateTeamNick(tid, owner, accId, nick);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量移除管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/updateMuteTeamConfig", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet updateMuteTeamConfig(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "targetAccId") 	String targetAccId,
										@RequestParam(value = "ope") 	String ope){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.updateMuteTeamConfig(accId, tid, targetAccId, ope);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量移除管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/setTeamMuteList", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet setTeamMuteList(@RequestParam(value = "tid") 	String tid,
										@RequestParam(value = "owner") 	String owner,
										@RequestParam(value = "targetAccId") 	String targetAccId,
										@RequestParam(value = "mute") 	String mute){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.setTeamMuteList(accId, tid, owner, targetAccId, mute);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "批量移除管理员", notes = "成功返回JSON")
	@RequestMapping(value = "/im/leaveTeam", method=RequestMethod.POST)
	@Authorization
	public ResultDataSet leaveTeam(@RequestParam(value = "tid") String tid){
		ResultDataSet rds  = new ResultDataSet();
		String accId = HttpUtils.getAccIdFromHeader();
		try {
			rds = imTeam.leaveTeam(tid, accId);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
