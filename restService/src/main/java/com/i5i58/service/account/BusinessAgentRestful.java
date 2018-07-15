package com.i5i58.service.account;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IBusinessAgent;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;
import com.i5i58.util.web.IpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "账户-商务代理服务")
@RestController
public class BusinessAgentRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IBusinessAgent businessAgent;

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/businessAgent/loginByOpenId", method = RequestMethod.POST)
	public ResultDataSet loginByOpenId(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "password") String password, @RequestParam(value = "version") int version,
			@RequestParam(value = "device") int device, @RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = businessAgent.loginByOpenId(openId, password, version, clientIP, device, serialNum);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加代理", notes = "")
	@RequestMapping(value = "/businessAgent/addAgent", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet addAgent(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "name") String name, @RequestParam(value = "phone") String phone,
			@RequestParam(value = "qq") String qq) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = businessAgent.addAgent(HttpUtils.getAccIdFromHeader(), openId, name, phone, qq, clientIP);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加代理", notes = "")
	@RequestMapping(value = "/businessAgent/deleteAgent", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet deleteAgent(@RequestParam(value = "openId") String openId) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = businessAgent.deleteAgent(HttpUtils.getAccIdFromHeader(), openId, clientIP);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "禁用/解禁代理", notes = "")
	@RequestMapping(value = "/businessAgent/nullityAgent", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet nullityAgent(@RequestParam(value = "openId") String openId,
			@RequestParam(value = "nullity") boolean nullity) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = businessAgent.nullityAgent(HttpUtils.getAccIdFromHeader(), openId, nullity, clientIP);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "禁用/解禁代理", notes = "")
	@RequestMapping(value = "/businessAgent/listAgent", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet listAgent(@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = businessAgent.listAgent(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
}
