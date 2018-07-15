package com.i5i58.service.account;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountPersonal;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;
import com.i5i58.util.web.IpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Lee
 *
 */
@Api(value = "账户-个人信息设置服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountPersonalRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountPersonal personal;

	@ApiOperation(value = "修改小头像", notes = "")
	@RequestMapping(value = "/account/setIconSmall", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setIconSmall(@RequestParam(value = "iconUrl") String iconUrl) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setIconSmall(HttpUtils.getAccIdFromHeader(), iconUrl);
		return rds;
	}

	@ApiOperation(value = "修改高清头像", notes = "")
	@RequestMapping(value = "/account/setIconOrg", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setIconOrg(@RequestParam(value = "iconUrl") String iconUrl) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setIconOrg(HttpUtils.getAccIdFromHeader(), iconUrl);
		return rds;
	}

	@ApiOperation(value = "修改昵称", notes = "")
	@RequestMapping(value = "/account/setNickName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setNickName(@RequestParam(value = "nickName") String nickName) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setNickName(HttpUtils.getAccIdFromHeader(), nickName);
		return rds;
	}

	@ApiOperation(value = "修改艺名", notes = "")
	@RequestMapping(value = "/account/setStageName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setStageName(@RequestParam(value = "stageName") String stageName) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setStageName(HttpUtils.getAccIdFromHeader(), stageName);
		return rds;
	}

	@ApiOperation(value = "修改签名", notes = "")
	@RequestMapping(value = "/account/setSignature", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setSignature(@RequestParam(value = "signature") String signature) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.setSignature(HttpUtils.getAccIdFromHeader(), signature);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "修改性别", notes = "")
	@RequestMapping(value = "/account/setGender", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setGender(@RequestParam(value = "gender") String gender) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setGender(HttpUtils.getAccIdFromHeader(), gender);
		return rds;
	}

	@ApiOperation(value = "修改生日", notes = "")
	@RequestMapping(value = "/account/setBirth", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setBirth(@RequestParam(value = "brith") long brith) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setBirth(HttpUtils.getAccIdFromHeader(), brith);
		return rds;
	}

	@ApiOperation(value = "修改地区", notes = "")
	@RequestMapping(value = "/account/setAddress", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setAddress(@RequestParam(value = "address") String address) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setAddress(HttpUtils.getAccIdFromHeader(), address);
		return rds;
	}

	@ApiOperation(value = "修改个性签名", notes = "")
	@RequestMapping(value = "/account/setPersonalBrief", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setPersonalBrief(@RequestParam(value = "personalBrief") String personalBrief) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.setPersonalBrief(HttpUtils.getAccIdFromHeader(), personalBrief);
		return rds;
	}

	@ApiOperation(value = "获取个人信息", notes = "点击小头像时")
	@RequestMapping(value = "/account/getMyPersonal", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyPersonal() {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getMyPersonal(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取个人信息", notes = "手机登陆后顶部")
	@RequestMapping(value = "/account/getMyInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyInfo() {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getMyInfo(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取个人信息", notes = "手机登陆后顶部_nickName")
	@RequestMapping(value = "/account/getMyInfoEx", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyInfo1() {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getMyInfo1(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取TA的个人信息", notes = "手机登陆后顶部")
	@RequestMapping(value = "/account/getTAInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTAInfo(@RequestParam(value = "ta") String ta) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getTAInfo(ta);
		return rds;
	}

	@ApiOperation(value = "获取TA的个人信息", notes = "手机登陆后顶部nickName")
	@RequestMapping(value = "/account/getTAInfoEx", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getTAInfo1(@RequestParam(value = "ta") String ta) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getTAInfo1(ta);
		return rds;
	}

	@ApiOperation(value = "购买VIP与普通坐骑", notes = "")
	@RequestMapping(value = "/account/buyMount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet buyMount(@RequestParam(value = "mountId") int mountId,
			@RequestParam(value = "month") int month) {

		ResultDataSet rds = new ResultDataSet();
		rds = personal.buyMount(HttpUtils.getAccIdFromHeader(), mountId, HttpUtils.getRealClientIpAddress(), month);		
		return rds;
	}

	@ApiOperation(value = "查询我的钱包", notes = "")
	@RequestMapping(value = "/account/getWallet", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getWallet() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getWallet(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "查询我的坐骑", notes = "")
	@RequestMapping(value = "/account/getMyMount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyMount(@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "pageNum") int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getMyMount(HttpUtils.getAccIdFromHeader(), pageSize, pageNum);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "设置我的个人信息", notes = "")
	@RequestMapping(value = "/account/setMyInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setMyInfo(@RequestParam(value = "nickName") String nickName,
			@RequestParam(value = "brith") long brith, @RequestParam(value = "address") String address,
			@RequestParam(value = "gender") byte gender,
			@RequestParam(value = "location") String location,
			@RequestParam(value = "signature") String signature,
			@RequestParam(value = "personalBrief") String personalBrief,
			@RequestParam(value = "stageName") String stageName) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.setMyInfo(HttpUtils.getAccIdFromHeader(), nickName, brith, address, gender, location, signature, personalBrief, stageName);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "获取我的账号", notes = "")
	@RequestMapping(value = "/account/getMyAccount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyAccount() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getMyAccount(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "获取我的账号", notes = "")
	@RequestMapping(value = "/account/getMyAccountEx", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyAccount1() {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getMyAccount1(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "获取我的守护", notes = "")
	@RequestMapping(value = "/account/getMyGuard", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getMyGuard(@ApiParam(value = "页码") @RequestParam(value = "pageNum") int pageNum,
			@ApiParam(value = "每页数量") @RequestParam(value = "pageSize") int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getMyGuard(HttpUtils.getAccIdFromHeader(), pageNum, pageSize);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "设置我的坐骑", notes = "")
	@RequestMapping(value = "/account/selectMount", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet selectMount(@RequestParam(value = "selectJson") String selectJson) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.selectMount(HttpUtils.getAccIdFromHeader(), selectJson);
		return rds;
	}

	@ApiOperation(value = "批量获取马甲相关账号信息", notes = "")
	@RequestMapping(value = "/account/getMajiaAccounts", method = RequestMethod.GET)
	public ResultDataSet getMajiaAccounts(@RequestParam(value = "accIds") List<String> accIds) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getMajiaAccounts(accIds);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="设置绑定手机号", notes="")
	@RequestMapping(value="/account/setBindMobile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet setBindMobile(@RequestParam(value ="phoneNo") String phoneNo,
			@RequestParam(value="password") String password,
			@RequestParam(value ="verifyCode") String verifyCode){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.setBindMobile(HttpUtils.getAccIdFromHeader(), phoneNo, password, verifyCode);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="修改绑定手机号", notes="")
	@RequestMapping(value="/account/modifyBindMobile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet modifyBindMobile(
			@RequestParam(value ="password") String password,
			@RequestParam(value ="phoneNo") String phoneNo,
			@RequestParam(value ="verifyCode") String verifyCode){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.modifyBindMobile(HttpUtils.getAccIdFromHeader(), password, phoneNo, verifyCode);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="发送验证码，验证绑定手机号", notes="")
	@RequestMapping(value="/account/sendBindingMobileVerifyCode", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet sendBindingMobileVerifyCode(@RequestParam(value ="phoneNo") String phoneNo){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.sendBindingMobileVerifyCode(HttpUtils.getAccIdFromHeader(), phoneNo);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	@ApiOperation(value="获取绑定手机号", notes="")
	@RequestMapping(value="/account/getBindMobile", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getBindMobile(){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getBindMobile(HttpUtils.getAccIdFromHeader());
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="根据accId获取头像", notes="")
	@RequestMapping(value="/account/getFaceUrlByAccId", method = RequestMethod.GET)
	public ResultDataSet getFaceUrlByAccId(@RequestParam(value ="targetAccId") String targetAccId){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getFaceUrlByAccId(targetAccId);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="根据openId获取头像", notes="")
	@RequestMapping(value="/account/getFaceUrlByOpenId", method = RequestMethod.GET)
	public ResultDataSet getFaceUrlByOpenId(@RequestParam(value ="openId") String openId){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getFaceUrlByOpenId(openId);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
	
	@ApiOperation(value="根据accId获取用户属性", notes="")
	@RequestMapping(value="/account/getAccountProperty", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getAccountProperty(@RequestParam(value ="targetAccId") String targetAccId){
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getAccountProperty(targetAccId);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}
}
