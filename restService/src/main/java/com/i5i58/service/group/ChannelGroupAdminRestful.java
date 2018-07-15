package com.i5i58.service.group;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.group.IChannelGroupAdmin;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "公会-（经纪公司）管理服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class ChannelGroupAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IChannelGroupAdmin channelGroupAdmin;

	@ApiOperation(value = "建立公会、经纪公司档案", notes = "")
	@RequestMapping(value = "/group/createProfile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createProfile(@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "type") int type,
			@RequestParam(value = "registerId") String registerId, @RequestParam(value = "operRange") String operRange,
			@RequestParam(value = "licenseUrl", defaultValue = "") String licenseUrl,
			@RequestParam(value = "taxCertificateUrl", defaultValue = "") String taxCertificateUrl,
			@RequestParam(value = "organizationCodeUrl", defaultValue = "") String organizationCodeUrl,
			@RequestParam(value = "bankLicenseUrl", defaultValue = "") String bankLicenseUrl,
			@RequestParam(value = "address") String address, @RequestParam(value = "legalPerson") String legalPerson,
			@RequestParam(value = "legalPersonUrl") String legalPersonUrl,
			@RequestParam(value = "legalPersonBackUrl") String legalPersonBackUrl,
			@RequestParam(value = "dataUrl") String dataUrl,
			@RequestParam(value = "regCapital", defaultValue = "0") double regCapital,
			@RequestParam(value = "fixedPhone", defaultValue = "") String fixedPhone,
			@RequestParam(value = "email", defaultValue = "") String email) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.createProfile(HttpUtils.getAccIdFromHeader(), name, description, type, registerId,
				operRange, licenseUrl, taxCertificateUrl, organizationCodeUrl, bankLicenseUrl, address, legalPerson,
				legalPersonUrl, legalPersonBackUrl, dataUrl, regCapital, fixedPhone, email,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "超管创建顶级组（公会）", notes = "需要profile审核通过")
	@RequestMapping(value = "/group/createTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createChannelTopGroup(@RequestParam(value = "name") String name,
			@RequestParam(value = "faceUrl") String faceUrl, @RequestParam(value = "fId") String fId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.createChannelTopGroup(HttpUtils.getAccIdFromHeader(), name, faceUrl, fId,
					HttpUtils.getRealClientIpAddress());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "创建次级频道组", notes = "需要已有顶级组")
	@RequestMapping(value = "/group/createSubGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createChannelSubGroup(@RequestParam(value = "name") String name,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.createChannelSubGroup(HttpUtils.getAccIdFromHeader(), name, gId,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "指派频道组拥有者", notes = "")
	@RequestMapping(value = "/group/assignGroupOwner", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet assignChannelGroupOwner(@RequestParam(value = "owner") String owner,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.assignChannelGroupOwner(owner, HttpUtils.getAccIdFromHeader(), gId);
		return rds;
	}

	@ApiOperation(value = "指派频道组管理员", notes = "")
	@RequestMapping(value = "/group/assignGroupAdminor", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet assignChannelGroupAdmin(@RequestParam(value = "admin") String admin,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.assignChannelGroupAdmin(admin, HttpUtils.getAccIdFromHeader(), gId);
		return rds;
	}

	@ApiOperation(value = "在组内创建频道", notes = "")
	@RequestMapping(value = "/group/createChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet createChannelInGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.createChannelInGroup(HttpUtils.getAccIdFromHeader(), gId, name,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "获取我的公会档案列表", notes = "")
	@RequestMapping(value = "/group/getMyProfiles", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyGroupFiles() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.getMyGroupFiles(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取我的公会列表", notes = "")
	@RequestMapping(value = "/group/getMyGroups", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyGroups() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.getMyGroups(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取我的经纪公司下属的公会/频道分组列表", notes = "")
	@RequestMapping(value = "/group/getMyAgencyGroups", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyAgencyGroups() {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.getMyAgencyGroups(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "修改组名称", notes = "")
	@RequestMapping(value = "/group/editName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editGroupName(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.editGroupName(HttpUtils.getAccIdFromHeader(), gId, name);
		return rds;
	}

	@ApiOperation(value = "修改组介绍", notes = "")
	@RequestMapping(value = "/group/editDesc", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editGroupDesc(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "desc") String desc) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.editGroupDesc(HttpUtils.getAccIdFromHeader(), gId, desc);
		return rds;
	}

	@ApiOperation(value = "修改公会资料", notes = "")
	@RequestMapping(value = "/group/editGroupInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet editGroupInfo(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "groupIconUrl") String groupIconUrl,
			@RequestParam(value = "groupName") String groupName, @RequestParam(value = "desc") String desc,
			@RequestParam(value = "groupNotice") String groupNotice) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.editGroupInfo(HttpUtils.getAccIdFromHeader(), gId, groupIconUrl, groupName, desc,
				groupNotice);
		return rds;
	}

	@ApiOperation(value = "发起签约主播", notes = "")
	@RequestMapping(value = "/group/requestContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet requestContract(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "accId") String accId, @RequestParam(value = "groupRate") int groupRate,
			@RequestParam(value = "endDate") long endDate, @RequestParam(value = "settleMode") int settleMode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.requestContract(HttpUtils.getAccIdFromHeader(), gId, accId, groupRate, endDate,
					settleMode);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "回应签约主播", notes = "")
	@RequestMapping(value = "/group/responseContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet responseContract(@RequestParam(value = "ctId") String ctId,
			@RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.responseContract(HttpUtils.getAccIdFromHeader(), ctId, agree);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取主播合约", notes = "")
	@RequestMapping(value = "/group/getContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getContract(@RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.getContract(HttpUtils.getAccIdFromHeader(), ctId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会合约", notes = "")
	@RequestMapping(value = "/group/queryContractByGId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryContractByGId(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "status") int status, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryContractByGId(HttpUtils.getAccIdFromHeader(), gId, status, pageSize, pageNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会即将到期合约", notes = "")
	@RequestMapping(value = "/group/queryContractExpire", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryContractExpire(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "Expire") long Expire, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryContractExpire(HttpUtils.getAccIdFromHeader(), gId, Expire, pageSize, pageNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会主播", notes = "")
	@RequestMapping(value = "/group/queryAnchorByTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorByTopGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryAnchorByTopGroup(HttpUtils.getAccIdFromHeader(), gId, pageSize, pageNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会没有频道的主播", notes = "")
	@RequestMapping(value = "/group/queryAnchorNoChannelByTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorNoChannelByTopGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryAnchorNoChannelByTopGroup(HttpUtils.getAccIdFromHeader(), gId, pageSize,
					pageNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢经纪公司档案", notes = "status{0不按狀態，1审核中 2正常 3禁用, 按創建時間降序")
	@RequestMapping(value = "/group/queryProfileList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryProfileList(@RequestParam(value = "param") String param,
			@RequestParam(value = "status") int status, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryProfileList(param, status, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢公会内分组", notes = "")
	@RequestMapping(value = "/group/querySubGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet querySubGroup(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.querySubGroup(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢公会内频道", notes = "")
	@RequestMapping(value = "/group/queryChannelInTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryChannelInTopGroup(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryChannelInTopGroup(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢公会内未指派给分组的频道", notes = "")
	@RequestMapping(value = "/group/queryChannelInTopGroupNoAssign", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryChannelInTopGroupNoAssign(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryChannelInTopGroupNoAssign(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢公会信息", notes = "")
	@RequestMapping(value = "/group/getTopGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTopGroup(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.getTopGroup(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询管理员列表", notes = "")
	@RequestMapping(value = "/group/queryAdminorList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAdminorList(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryAdminorList(HttpUtils.getAccIdFromHeader(), gId, pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询主播列表", notes = "")
	@RequestMapping(value = "/group/queryAnchorList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorList(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.queryAnchorList(HttpUtils.getAccIdFromHeader(), gId, pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "移除次级频道组", notes = "需要是组的管理员或拥有者")
	@RequestMapping(value = "/group/removeChannelSubGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet removeChannelSubGroup(@RequestParam(value = "name") String name,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.removeChannelSubGroup(HttpUtils.getAccIdFromHeader(), name, gId,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "移动频道组", notes = "需要是组的管理员或拥有者")
	@RequestMapping(value = "/group/rechangeChannelGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet rechangeChannelGroup(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.rechangeChannelGroup(HttpUtils.getAccIdFromHeader(), cId, gId,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "发起解除合约", notes = "")
	@RequestMapping(value = "/group/requestCancelContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet requestCancelContract(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.requestCancelContract(HttpUtils.getAccIdFromHeader(), accId, ctId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "同意解除合约", notes = "")
	@RequestMapping(value = "/group/responseCancelContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet responseCancelContract(@RequestParam(value = "ctId") String ctId,
			@RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.responseCancelContractNormal(HttpUtils.getAccIdFromHeader(), ctId, agree);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "隐藏合约", notes = "")
	@RequestMapping(value = "/group/hideContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet hideContract(@RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.hideContract(HttpUtils.getAccIdFromHeader(), ctId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "解绑频道组", notes = "")
	@RequestMapping(value = "/channel/unbundlingChannelGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet unbundlingChannelGroup(@RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.unbundlingChannelGroup(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}

	@ApiOperation(value = "取消频道组管理员权限", notes = "")
	@RequestMapping(value = "/group/cancelChannelGroupAdmin", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet cancelChannelGroupAdmin(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.cancelChannelGroupAdmin(HttpUtils.getAccIdFromHeader(), accId, gId);
		return rds;
	}

	@ApiOperation(value = "从分组中移除频道", notes = "需要是组的管理员或拥有者")
	@RequestMapping(value = "/group/removeChannelInSubGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet removeChannelInSubGroup(@RequestParam(value = "cId") String cId,
			@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.removeChannelInSubGroup(HttpUtils.getAccIdFromHeader(), cId, gId,
				HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "查詢公会或分组的展示信息", notes = "")
	@RequestMapping(value = "/group/getGroupInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGroupInfo(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.getGroupInfo(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取自己的组内权限", notes = "")
	@RequestMapping(value = "/group/getMyGroupRight", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyGroupRight(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = channelGroupAdmin.getMyGroupRight(HttpUtils.getAccIdFromHeader(), gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询一段时间内主播开播记录", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/group/getAnchorPushRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getAnchorPushRecord(@ApiParam(value = "用户accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.getAnchorPushRecord(HttpUtils.getAccIdFromHeader(), accId, fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "统计主播一段时间内的直播总时长", notes = "[fromTime, toTime]，最大间隔60天")
	@RequestMapping(value = "/group/calcAnchorActiveTime", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet calcAnchorActiveTime(@ApiParam(value = "用户accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "当前查询起止时间，毫秒") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.calcAnchorActiveTime(HttpUtils.getAccIdFromHeader(), accId, fromTime, toTime);
		return rds;
	}

	@ApiOperation(value = "查看公会下等待处理的合约（签约和解约）", notes = "direction:0:主播发起，1：公会发起 ")
	@RequestMapping(value = "/group/queryRequestedContractsInGroup", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryRequestedContractsInGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "direction") int direction, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryRequestedContracts(HttpUtils.getAccIdFromHeader(), gId, direction, pageSize,
				pageNum);
		return rds;
	}
	
	@ApiOperation(value = "获取公会操作记录", notes = "")
	@RequestMapping(value = "/group/queryTopGroupRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryTopGroupRecord(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryTopGroupRecord(HttpUtils.getAccIdFromHeader(), gId, pageSize,pageNum);
		return rds;
	}
	
	@ApiOperation(value = "获取公会内频道分组操作记录", notes = "")
	@RequestMapping(value = "/group/querySubGroupRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet querySubGroupRecord(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.querySubGroupRecord(HttpUtils.getAccIdFromHeader(), gId, pageSize,pageNum);
		return rds;
	}
	
	@ApiOperation(value = "获取公会内频道操作记录", notes = "")
	@RequestMapping(value = "/group/queryChannelOpeRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryChannelOpeRecord(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryChannelOpeRecord(HttpUtils.getAccIdFromHeader(), gId, pageSize,pageNum);
		return rds;
	}
	
	@ApiOperation(value = "获取公会内合约记录", notes = "")
	@RequestMapping(value = "/group/queryContractOpeRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryContractOpeRecord(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryContractOpeRecord(HttpUtils.getAccIdFromHeader(), gId, pageSize,pageNum);
		return rds;
	}
	
	@ApiOperation(value = "删除公会空闲频道", notes = "")
	@RequestMapping(value = "/group/deleteFreeChannel", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet deleteFreeChannel(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@ApiParam(value = "频道id") @RequestParam(value = "cId") String cId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.deleteFreeChannel(HttpUtils.getAccIdFromHeader(),gId,cId);
		return rds;
	}
	
	@ApiOperation(value = "查询公会频道数", notes = "")
	@RequestMapping(value = "/group/queryChannelCount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryChannelCount(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryChannelCount(HttpUtils.getAccIdFromHeader(),gId);
		return rds;
	}
	
	@ApiOperation(value = "公会强制解约", notes = "cancelDirection:0:主播发起，1：公会发起")
	@RequestMapping(value = "/group/forceCancelContract", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet forceCancelContract(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@ApiParam(value = "合约id") @RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.forceCancelContract(HttpUtils.getAccIdFromHeader(), gId,ctId);
		return rds;
	}
	
	@ApiOperation(value = "查看主播状态", notes = "")
	@RequestMapping(value = "/group/queryAnchorStatus", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorStatus(@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryAnchorStatus(HttpUtils.getAccIdFromHeader(), accId);
		return rds;
	}
	
	@ApiOperation(value = "查询主播钱包", notes = "")
	@RequestMapping(value = "/group/queryAnchorWallet", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorWallet(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryAnchorWallet(HttpUtils.getAccIdFromHeader(), gId, accId);
		return rds;
	}
	
	@ApiOperation(value = "查询主播日均俸禄信息", notes = "")
	@RequestMapping(value = "/group/queryAnchorAverageCommision", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryAnchorAverageCommision(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@ApiParam(value = "主播id") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "查询开始时间") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "查询结束时间") @RequestParam(value = "toTime") long toTime){
		ResultDataSet rds = new ResultDataSet();
		rds = channelGroupAdmin.queryAverageGiftCommision(HttpUtils.getAccIdFromHeader(), gId, accId,fromTime,toTime);
		return rds;
	}
}
	
	

