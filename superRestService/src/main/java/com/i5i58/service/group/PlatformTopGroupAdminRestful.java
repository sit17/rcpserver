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
import com.i5i58.apis.platform.IPlatformTopGroupAdmin;
import com.i5i58.security.SuperAuthorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "平台公会管理服务")
@RestController
public class PlatformTopGroupAdminRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IPlatformTopGroupAdmin platformTopGroupAdmin;

	@ApiOperation(value = "查询公会", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryTopGroupList", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryTopGroupList(@RequestParam(value = "param") String param,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.queryTopGroupList(param, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "开禁公会", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/enableTopGroup", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet enableTopGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "enable") boolean enable) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.enableTopGroup(HttpUtils.getAccIdFromHeader(), gId, enable);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会档案", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryProfileByTopGroup", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryProfileByTopGroup(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.queryProfileByTopGroup(gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会管理员", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryAdminorsByTopGroup", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAdminorsByTopGroup(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.queryAdminorsByTopGroup(gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "查询公会下属频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/getChannelByTopGroup", method = RequestMethod.GET)
	//@SuperAuthorization
	public ResultDataSet getChannelByTopGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.getChannelByTopGroup(gId, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询工会下属主播", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/getAnchorByTopGroup", method = RequestMethod.GET)
	@SuperAuthorization
	public ResultDataSet getAnchorByTopGroup(@RequestParam(value = "gId") String gId,
			@RequestParam(value="pageSize") int pageSize, @RequestParam(value="pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.getAnchorByTopGroup(gId, pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "同意解除合约", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/responseCancelContract", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet responseCancelContract(@RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.responseCancelContract(ctId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "根据分组Id或工会Id查询工会信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/getTopGroupByGId", method = RequestMethod.GET)
	@SuperAuthorization
	public ResultDataSet getTopGroupByGId(@RequestParam(value = "gId") String gId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.getTopGroupByGId(gId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "分页查询公会取消合约的列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryCancelContract", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryCancelContract(@RequestParam(value = "pageSize") int pageSize, 
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.queryCancelContract(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "分页查询公会强制解约的请求列表", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryForceCancelContract", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryForceCancelContract(@RequestParam(value = "pageSize") int pageSize, 
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.queryForceCancelContract(pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "同意强制解约", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/responseForceCancelContract", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet responseForceCancelContract(@RequestParam(value = "ctId") String ctId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = platformTopGroupAdmin.responseForceCancelContract(ctId);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "查询主播日均俸禄信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/queryAverageGiftCommision", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet queryAverageGiftCommision(@ApiParam(value = "主播accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "查询开始时间") @RequestParam(value = "fromTime") long fromTime,
			@ApiParam(value = "查询截止时间") @RequestParam(value = "toTime") long toTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformTopGroupAdmin.queryAverageGiftCommision(accId,fromTime,toTime);
		return rds;
	}
	
	@ApiOperation(value = "创建频道", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/super/group/createChannel", method = RequestMethod.POST)
	@SuperAuthorization
	public ResultDataSet createChannel(@ApiParam(value = "公会id") @RequestParam(value = "gId") String gId,
			@ApiParam(value = "查询开始时间") @RequestParam(value = "name") String name) {
		ResultDataSet rds = new ResultDataSet();
		rds = platformTopGroupAdmin.createChannel(HttpUtils.getAccIdFromHeader(), gId, name, HttpUtils.getRealClientIpAddress());
		return rds;
	}
}