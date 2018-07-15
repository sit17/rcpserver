package com.i5i58.service.channel;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.ILiveServerAssign;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "分配直播间服务器")
@RestController
public class AssignLiveServerRestful {
	@Reference
	private ILiveServerAssign liveServerAssign;
	
	@ApiOperation(value = "为直播间分配服务器", notes = "")
	@RequestMapping(value = "/super/assignLiveServer", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet assignLiveServer(@RequestParam(value = "cId") String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = liveServerAssign.assignLiveServer(HttpUtils.getAccIdFromHeader(), cId);
		return rds;
	}
	
	@ApiOperation(value = "获取所有空闲的服务器", notes = "")
	@RequestMapping(value = "/super/getAllFreeLiveServer", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet getAllFreeLiveServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = liveServerAssign.getAllFreeLiveServer();
		return rds;
	}
	
	@ApiOperation(value = "获取所有激活的服务器", notes = "")
	@RequestMapping(value = "/super/getAllActiveLiveServer", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_CONTROL_AUTH)
	public ResultDataSet getAllActiveLiveServer() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds = liveServerAssign.getAllActiveLiveServer();
		return rds;
	}
}
