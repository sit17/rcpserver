package com.i5i58.service.profile;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformProfileAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "平台档案管理服务")
@RestController
public class PlatformProfileAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPlatformProfileAdmin platformProfileAdmin;

	@ApiOperation(value = "经纪公司档案审核", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/verifyGroupProfile", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet verifyGroupProfile(@RequestParam(value = "fId") String fId,
			@RequestParam(value = "topGroupCount") int topGroupCount,
			@RequestParam(value = "subGroupCount") int subGroupCount,
			@RequestParam(value = "channelCount") int channelCount, @RequestParam(value = "agree") boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.verifyGroupProfile(HttpUtils.getAccIdFromHeader(), fId, topGroupCount,
					subGroupCount, channelCount, agree);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查詢经纪公司档案", notes = "status{0不按狀態，1审核中 2正常 3禁用, 按創建時間降序")
	@RequestMapping(value = "/super/queryProfileList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryProfileList(@RequestParam(value = "param") String param,
			@RequestParam(value = "status") int status, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.queryProfileList(param, status, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取经纪公司档案", notes = "")
	@RequestMapping(value = "/super/getProfile", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet getProfile(@RequestParam(value = "fId") String fId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.getProfile(fId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取经纪公司档案", notes = "")
	@RequestMapping(value = "/super/enableProfile", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet enableProfile(@RequestParam(value = "fId") String fId,
			@RequestParam(value = "enable") boolean enable) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.enableProfile(HttpUtils.getAccIdFromHeader(), fId, enable);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "修改经纪公司档案", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/updateGroupAndChannelCount", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet updateGroupAndChannelCount(@RequestParam(value = "fId") String fId,
			@RequestParam(value = "topGroupCount") int topGroupCount,
			@RequestParam(value = "subGroupCount") int subGroupCount,
			@RequestParam(value = "channelCount") int channelCount) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.updateGroupAndChannelCount(HttpUtils.getAccIdFromHeader(), fId, topGroupCount,
					subGroupCount, channelCount);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询采用该资质的公会（即显示该经纪公司下属的全部公会）", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/queryTopGroupsInProfile", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.BASE_AUTH)
	public ResultDataSet queryTopGroupsInProfile(@RequestParam(value = "fId") String fId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformProfileAdmin.queryTopGroupsInProfile(fId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}