package com.i5i58.service.netBar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
import com.i5i58.apis.netBar.INetBar;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.Device;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "网吧")
@RestController
@RequestMapping("${rpc.controller.version}")
public class NetBarRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private INetBar netBar;

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/netBar/loginByOpenId", method = RequestMethod.POST)
	@Device(DeviceCode = DeviceCode.WechatPublic)
	public ResultDataSet loginByOpenId(@ApiParam(value = "微信的openId") @RequestParam(value = "openId") String openId,
			@RequestParam(value = "version") int version,
			@ApiParam(value = "device 只能是netBar") @RequestParam(value = "device") int device,
			@RequestParam(value = "serialNum") String serialNum, @RequestParam(value = "accessKey") String accessKey) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		// if (DeviceCode.isWeb(device)) {
		// serialNum = StringUtils.createUUID();
		// }
		String key = "DBD432C8B81EA1DB1E2C9CDEA248BD7A";
		if (!accessKey.equals(key)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		rds = netBar.loginByOpenId(openId, version, clientIP, device, serialNum);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
			loginCookie.setDomain("i5i58.com");
			loginCookie.setHttpOnly(true);
			loginCookie.setPath("/");
			response.addCookie(loginCookie);
			Cookie sessionCookie = new Cookie("sessionId", serialNum);
			sessionCookie.setDomain("i5i58.com");
			sessionCookie.setHttpOnly(true);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
		}
		return rds;
	}

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/netBar/loginByOpenIdForAgent", method = RequestMethod.POST)
	@Device(DeviceCode = DeviceCode.WechatPublic)
	public ResultDataSet loginByOpenIdForAgent(
			@ApiParam(value = "微信的openId") @RequestParam(value = "openId") String openId,
			@RequestParam(value = "version") int version,
			@ApiParam(value = "device 只能是netBar") @RequestParam(value = "device") int device,
			@RequestParam(value = "serialNum") String serialNum, @RequestParam(value = "accessKey") String accessKey) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
		if (DeviceCode.isWeb(device)) {
			serialNum = StringUtils.createUUID();
		}
		String key = "DBD432C8B81EA1DB1E2C9CDEA248BD7A";
		if (!accessKey.equals(key)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		rds = netBar.loginByOpenIdForAgent(openId, version, clientIP, device, serialNum);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
			loginCookie.setDomain("i5i58.com");
			loginCookie.setHttpOnly(true);
			loginCookie.setPath("/");
			response.addCookie(loginCookie);
			Cookie sessionCookie = new Cookie("sessionId", serialNum);
			sessionCookie.setDomain("i5i58.com");
			sessionCookie.setHttpOnly(true);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
		}
		return rds;
	}

	@ApiOperation(value = "查询礼物收入", notes = "")
	@RequestMapping(value = "/netBar/queryGiftIncome", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGiftIncome(@RequestParam(value = "startDate") long startDate,
			@RequestParam(value = "endDate") long endDate) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryGiftIncome(HttpUtils.getAccIdFromHeader(), startDate, endDate);
		return rds;
	}

	@ApiOperation(value = "通过ownerId查询礼物收入", notes = "")
	@RequestMapping(value = "/netBar/queryGiftIncomeByOwnerId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGiftIncomeByOwnerId(
			@ApiParam(value = "ownerId") @RequestParam(value = "ownerId") String ownerId,
			@RequestParam(value = "startDate") long startDate, @RequestParam(value = "endDate") long endDate) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryGiftIncome(ownerId, startDate, endDate);
		return rds;
	}

	@ApiOperation(value = "查询游戏收入", notes = "")
	@RequestMapping(value = "/netBar/queryGameIncome", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGameIncome(@RequestParam(value = "startDate") long startDate,
			@RequestParam(value = "endDate") long endDate) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryGameIncome(HttpUtils.getAccIdFromHeader(), startDate, endDate);
		return rds;
	}

	@ApiOperation(value = "查询网吧代理下的网吧", notes = "")
	@RequestMapping(value = "/netBar/queryNetBarByAgent", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryNetBarByAgent(@RequestParam(value = "pageNum") int pageNum,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "viewAll") boolean viewAll) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryNetBarByAgent(HttpUtils.getAccIdFromHeader(), pageNum, pageSize, viewAll);
		return rds;
	}

	@ApiOperation(value = "查询账号的类型", notes = "返回Data 1、既是代理商又是网吧；2、代理商；3、网吧；0、无身份权限")
	@RequestMapping(value = "/netBar/queryAccountKind", method = RequestMethod.GET)
	public ResultDataSet queryAccountKind(
			@ApiParam(value = "微信的openId") @RequestParam(value = "openId") String openId) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryAccountKind(openId);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的充值统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getPayRebate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getPayRebate(
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryPayRebate(HttpUtils.getAccIdFromHeader(), searchMonth);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的充值统计通过AccId", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getPayRebateByAccId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getPayRebateByAccId(@ApiParam(value = "用户的accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryPayRebate(accId, searchMonth);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的充值统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getExChangeScoreRebate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getExChangeScoreRebate(
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryExChangeScoreRebate(HttpUtils.getAccIdFromHeader(), searchMonth);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的充值统计通过AccId", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getExChangeScoreRebateByAccId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getExChangeScoreRebateByAccId(
			@ApiParam(value = "用户的accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryExChangeScoreRebate(accId, searchMonth);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的直播中礼物消耗统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getGiveGiftRebate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGiveGiftRebate(
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryGiveGiftRebate(HttpUtils.getAccIdFromHeader(), searchMonth);
		return rds;
	}

	@ApiOperation(value = "获取指定网吧下的直播中礼物消耗统计通过AccId", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/netBar/getGiveGiftRebateByAccId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGiveGiftRebateByAccId(
			@ApiParam(value = "用户的accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "查询的月份") @RequestParam(value = "searchMonth") String searchMonth) {
		ResultDataSet rds = new ResultDataSet();
		rds = netBar.queryGiveGiftRebate(accId, searchMonth);
		return rds;
	}

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/netBar/loginByOpenIdForWeb", method = RequestMethod.POST)
	@Device(DeviceCode = DeviceCode.WEBLive)
	public ResultDataSet loginByOpenIdForWeb(
			@ApiParam(value = "平台的openId") @RequestParam(value = "openId") String openId,
			@ApiParam(value = "password") @RequestParam(value = "password") String password,
			@RequestParam(value = "version") int version,
			@ApiParam(value = "device") @RequestParam(value = "device") int device,
			@RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
//		if (DeviceCode.isWeb(device)) {
//			serialNum = StringUtils.createUUID();
//		}
		rds = netBar.loginByOpenIdForWeb(openId, password, version, clientIP, device, serialNum);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
			loginCookie.setDomain("i5i58.com");
			loginCookie.setHttpOnly(true);
			loginCookie.setPath("/");
			response.addCookie(loginCookie);
			Cookie sessionCookie = new Cookie("sessionId", serialNum);
			sessionCookie.setDomain("i5i58.com");
			sessionCookie.setHttpOnly(true);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
		}
		return rds;
	}

	@ApiOperation(value = "用户ID登陆，密码使用MD5加密", notes = "")
	@RequestMapping(value = "/netBar/loginByOpenIdForAgentForWeb", method = RequestMethod.POST)
	@Device(DeviceCode = DeviceCode.WEBLive)
	public ResultDataSet loginByOpenIdForAgentForWeb(
			@ApiParam(value = "平台的openId") @RequestParam(value = "openId") String openId,
			@ApiParam(value = "password") @RequestParam(value = "password") String password,
			@RequestParam(value = "version") int version,
			@ApiParam(value = "device 只能是netBar") @RequestParam(value = "device") int device,
			@RequestParam(value = "serialNum") String serialNum) {
		String clientIP = HttpUtils.getRealClientIpAddress();
		ResultDataSet rds = new ResultDataSet();
//		if (DeviceCode.isWeb(device)) {
//			serialNum = StringUtils.createUUID();
//		}

		rds = netBar.loginByOpenIdForAgentForWeb(openId, password, version, clientIP, device, serialNum);
		if (rds.getCode().equals(ResultCode.SUCCESS.getCode()) && device == DeviceCode.WEBLive) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			Cookie loginCookie = new Cookie("authorization", rds.getInnerData().toString());
			loginCookie.setDomain("i5i58.com");
			loginCookie.setHttpOnly(true);
			loginCookie.setPath("/");
			response.addCookie(loginCookie);
			Cookie sessionCookie = new Cookie("sessionId", serialNum);
			sessionCookie.setDomain("i5i58.com");
			sessionCookie.setHttpOnly(true);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
		}
		return rds;
	}
}
