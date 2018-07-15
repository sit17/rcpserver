package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountAuth;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
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
@RequestMapping("${rpc.controller.version}")
public class AccountAuthRestful {

	private Logger logger = Logger.getLogger(getClass());
	
	@Reference
	private IAccountAuth authentication;

	@ApiOperation(value = "主播实名认证申请", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/anchorAuth", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet anchorAuthentication(@RequestParam(value = "certificateId") String certificateId,
			@RequestParam(value = "realName") String realName,
			@RequestParam(value = "imgCertificateFace") String imgCertificateFace,
			@RequestParam(value = "imgcertificateBack") String imgcertificateBack,
			@RequestParam(value = "imgPerson") String imgPerson,
			@RequestParam(value = "bankCardNum") String bankCardNum,
			@RequestParam(value = "bankKeepPhone") String bankKeepPhone,
			@RequestParam(value = "location") String location,
			@RequestParam(value = "bankName") String bankName) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = authentication.anchorAuth(HttpUtils.getAccIdFromHeader(), certificateId, realName,
					imgCertificateFace, imgcertificateBack, imgPerson,bankCardNum,bankKeepPhone,location,bankName);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "普通用户实名认证申请", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/userAuth", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet normalAuthentication(@RequestParam(value = "realName") String realName,
			@RequestParam(value = "certificateId") String certificateId) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = authentication.userAuth(HttpUtils.getAccIdFromHeader(), realName, certificateId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;

	}	

	@ApiOperation(value = "普通主播认证申请", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/getMyUserAuthInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyUserAuthInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = authentication.getMyUserAuthInfo(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;

	}
	
	@ApiOperation(value = "发送实名认证的验证码", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/sendAuthVerifyCode", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet sendAuthVerifyCode() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = authentication.sendAuthVerifyCode(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;

	}
	
	@ApiOperation(value = "查询我的实名认证信息", notes = "成功返回账号JSON数据")
	@RequestMapping(value = "/account/queryMyAuthInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryMyAuthInfo() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = authentication.queryMyAuthInfo(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;

	}
}
