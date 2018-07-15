package com.i5i58.service.netBar;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.INetBarAdmin;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "网吧服务")
@RestController
public class NetBarRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private INetBarAdmin netBarAdmin;

	@ApiOperation(value = "添加网吧", notes = "超管权限")
	@RequestMapping(value = "/super/addNetBar", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet addNetBar(@ApiParam(value = "网吧账号绑定的平台账号accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "网吧名称") @RequestParam(value = "netBarName") String netBarName,
			@ApiParam(value = "网吧位置：{'state':'浙江', 'city':'杭州', 'area':'滨江', 'street':'xxxxx路123号xxx'}") @RequestParam(value = "addr") String addr,
			@ApiParam(value = "网吧固定ip") @RequestParam(value = "netBarIp") String netBarIp,
			@ApiParam(value = "网吧代理id， 非必要") @RequestParam(value = "agId", required = false) String agId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.addNetBar(HttpUtils.getAccIdFromHeader(), accId, netBarName, addr, netBarIp, agId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除网吧", notes = "超管权限")
	@RequestMapping(value = "/super/deleteNetBar", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet deleteNetBar(@ApiParam(value = "网吧nId") @RequestParam(value = "nId") String nId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.deleteNetBar(HttpUtils.getAccIdFromHeader(), nId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "禁用网吧", notes = "超管权限")
	@RequestMapping(value = "/super/nullityNetBar", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet nullityNetBar(@ApiParam(value = "网吧nId") @RequestParam(value = "nId") String nId,
			@ApiParam(value = "是否禁用") @RequestParam(value = "nullity") boolean nullity) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.nullityNetBar(HttpUtils.getAccIdFromHeader(), nId, nullity);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查看网吧", notes = "超管权限")
	@RequestMapping(value = "/super/queryNetBar", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.BASE_AUTH)
	public ResultDataSet queryNetBar(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "是否查看全部") @RequestParam(value = "viewAll") boolean viewAll) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.queryNetBar(pageNum, pageSize, viewAll);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	// ***************************************

	@ApiOperation(value = "添加网吧代理", notes = "超管权限")
	@RequestMapping(value = "/super/addNetBarAgent", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet addNetBarAgent(
			@ApiParam(value = "网吧账号绑定的平台账号accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "网吧代理名称") @RequestParam(value = "agentName") String agentName,
			@ApiParam(value = "网吧代理区域") @RequestParam(value = "area") String area) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.addNetBarAgent(HttpUtils.getAccIdFromHeader(), accId, agentName, area);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除网吧代理", notes = "超管权限")
	@RequestMapping(value = "/super/deleteNetBarAgent", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet deleteNetBarAgent(@ApiParam(value = "网吧代理agId") @RequestParam(value = "agId") String agId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.deleteNetBarAgent(HttpUtils.getAccIdFromHeader(), agId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "禁用网吧代理", notes = "超管权限")
	@RequestMapping(value = "/super/nullityNetBarAgent", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.OPERATION_ADMIN_AUTH)
	public ResultDataSet nullityNetBarAgent(@ApiParam(value = "网吧代理agId") @RequestParam(value = "agId") String agId,
			@ApiParam(value = "是否禁用") @RequestParam(value = "nullity") boolean nullity) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.nullityNetBarAgent(HttpUtils.getAccIdFromHeader(), agId, nullity);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查看网吧代理", notes = "超管权限")
	@RequestMapping(value = "/super/queryNetBarAgent", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.BASE_AUTH)
	public ResultDataSet queryNetBarAgent(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "是否查看全部") @RequestParam(value = "viewAll") boolean viewAll) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.queryNetBarAgent(pageNum, pageSize, viewAll);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查看网吧代理下网吧", notes = "超管权限")
	@RequestMapping(value = "/super/queryNetBarByAgent", method = RequestMethod.POST)
	@SuperAuthorization(value = SuperAdminAuth.BASE_AUTH)
	public ResultDataSet queryNetBarByAgent(@ApiParam(value = "网吧代理agId") @RequestParam(value = "agId") String agId,
			@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "是否查看全部") @RequestParam(value = "viewAll") boolean viewAll) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = netBarAdmin.queryNetBarByAgent(HttpUtils.getAccIdFromHeader(), agId, pageNum, pageSize, viewAll);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
