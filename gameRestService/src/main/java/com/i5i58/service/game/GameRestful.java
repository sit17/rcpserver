package com.i5i58.service.game;

import java.io.IOException;
import java.util.Enumeration;

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
import com.i5i58.apis.game.IGame;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 游戏rest
 * 
 * @author Administrator
 *
 */
@Api(value = "游戏服务")
@RestController
@RequestMapping("${rpc.controller.version}")
public class GameRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IGame game;

	// @ApiOperation(value = "用户ID游戏登陆", notes = "")
	// @RequestMapping(value = "/game/loginByOpenIdForGame", method =
	// RequestMethod.POST)
	// public ResultDataSet loginByOpenIdForGame(@ApiParam(value = "OpenId")
	// @RequestParam(value = "openId") String openId,
	// @ApiParam(value = "密码，需要MD5加密") @RequestParam(value = "password") String
	// password,
	// @ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String
	// appKey,
	// @ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}")
	// @RequestParam(value = "device") int device,
	// @ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String
	// serialNum) {
	// ResultDataSet rds = new ResultDataSet();
	// try {
	// rds = game.loginByOpenIdForGame(openId, password, appKey,
	// HttpUtils.getClientIpAddress(), device,
	// serialNum);
	// } catch (IOException e) {
	// logger.error(HttpUtils.getParamsString(), e);
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("Service Error");
	// }
	// return rds;
	// }
	//
	// @ApiOperation(value = "手机游戏登陆", notes = "")
	// @RequestMapping(value = "/game/loginByPhoneNoForGame", method =
	// RequestMethod.POST)
	// public ResultDataSet loginByPhoneNoForGame(
	// @ApiParam(value = "手机号码") @RequestParam(value = "phoneNo") String
	// phoneNo,
	// @ApiParam(value = "密码，需要MD5加密") @RequestParam(value = "password") String
	// password,
	// @ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String
	// appKey,
	// @ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}")
	// @RequestParam(value = "device") int device,
	// @ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String
	// serialNum) {
	// ResultDataSet rds = new ResultDataSet();
	// try {
	// rds = game.loginByPhoneNoForGame(phoneNo, password, appKey,
	// HttpUtils.getClientIpAddress(), device,
	// serialNum);
	// } catch (IOException e) {
	// logger.error(HttpUtils.getParamsString(), e);
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("Service Error");
	// }
	// return rds;
	// }
	//
	// @ApiOperation(value = "token游戏登陆", notes = "" + "header中需要accId, token")
	// @RequestMapping(value = "/game/loginByTokenForGame", method =
	// RequestMethod.POST)
	// @Authorization
	// public ResultDataSet loginByTokenForGame(
	// @ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String
	// appKey,
	// @ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}")
	// @RequestParam(value = "device") int device,
	// @ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String
	// serialNum) {
	// ResultDataSet rds = new ResultDataSet();
	// try {
	// rds = game.loginByTokenForGame(HttpUtils.getAccIdFromHeader(), appKey,
	// HttpUtils.getClientIpAddress(),
	// device, serialNum);
	// } catch (IOException e) {
	// logger.error(HttpUtils.getParamsString(), e);
	// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
	// rds.setMsg("Service Error");
	// }
	// return rds;
	// }

	@ApiOperation(value = "兑换游戏币", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/exchangeGameGold", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet exchangeGameGold(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "需要兑换的I币") @RequestParam(value = "iGold") long iGold,
			@ApiParam(value = "现有游戏币，兑换前") @RequestParam(value = "gameGold") long gameGold,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "银行密码") @RequestParam(value = "insurePass") String insurePass) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.exchangeGameGold(HttpUtils.getAccIdFromHeader(), appKey, iGold, gameGold,
					HttpUtils.getRealClientIpAddress(), device, serialNum, insurePass);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "兑换游戏币", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/exchangeGameGoldForWeb", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet exchangeGameGoldForWeb(
			@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "需要兑换的I币") @RequestParam(value = "iGold") long iGold,
			@ApiParam(value = "现有游戏币，兑换前") @RequestParam(value = "gameGold") long gameGold,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "银行密码") @RequestParam(value = "insurePass") String insurePass) {
		ResultDataSet rds = new ResultDataSet();
		try {

			// HttpServletRequest request = ((ServletRequestAttributes)
			// RequestContextHolder.getRequestAttributes())
			// .getRequest();
			// Enumeration<String> e = request.getHeaderNames();
			// while (e.hasMoreElements()) {
			// String headerName = e.nextElement();
			// logger.error(String.format("header:{[%s]:[%s]}", headerName,
			// request.getHeader(headerName)));
			// }
			rds = game.exchangeGameGoldForWeb(HttpUtils.getAccIdFromHeader(), appKey, iGold, gameGold,
					HttpUtils.getRealClientIpAddress(), device, serialNum, insurePass);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "兑        换钻石", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/exchangeDiamond", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet exchangeDiamond(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "需要兑换的钻石") @RequestParam(value = "diamond") long diamond,
			@ApiParam(value = "现有游戏币，兑换前") @RequestParam(value = "gameGold") long gameGold,
			@ApiParam(value = "设备ID, {PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "设备序列号") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "银行密码") @RequestParam(value = "insurePass") String insurePass) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.exchangeDiamond(HttpUtils.getAccIdFromHeader(), appKey, diamond, gameGold,
					HttpUtils.getRealClientIpAddress(), device, serialNum, insurePass);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取银行", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getBank", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getBank() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getBank(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取I币", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getIGold", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getIGold(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.getIGold(HttpUtils.getAccIdFromHeader(), appKey);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取I币", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getIGoldByOpenId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getIGoldByOpenId(
			@ApiParam(value = "要查询的openId") @RequestParam(value = "openId") String openId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.getIGoldByOpenId(openId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "添加/更新游戏房间", notes = "")
	@RequestMapping(value = "/game/UpdateGameItem", method = RequestMethod.POST)
	public ResultDataSet UpdateGameItem(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "游戏secret") @RequestParam(value = "secret") String secret,
			@ApiParam(value = "游戏serverId") @RequestParam(value = "serverId") int serverId,
			@ApiParam(value = "游戏kindId") @RequestParam(value = "kindId") int kindId,
			@ApiParam(value = "游戏nodeId") @RequestParam(value = "nodeId") int nodeId,
			@ApiParam(value = "游戏sortId") @RequestParam(value = "sortId") int sortId,
			@ApiParam(value = "游戏serverKind") @RequestParam(value = "serverKind") int serverKind,
			@ApiParam(value = "游戏serverType") @RequestParam(value = "serverType") int serverType,
			@ApiParam(value = "游戏serverLevel") @RequestParam(value = "serverLevel") int serverLevel,
			@ApiParam(value = "游戏serverPort") @RequestParam(value = "serverPort") int serverPort,
			@ApiParam(value = "游戏cellScore") @RequestParam(value = "cellScore") long cellScore,
			@ApiParam(value = "游戏enterScore") @RequestParam(value = "enterScore") long enterScore,
			@ApiParam(value = "游戏serverRule") @RequestParam(value = "serverRule") int serverRule,
			@ApiParam(value = "游戏lineCount") @RequestParam(value = "lineCount") int lineCount,
			@ApiParam(value = "游戏androidCount") @RequestParam(value = "androidCount") int androidCount,
			@ApiParam(value = "游戏fullCount") @RequestParam(value = "fullCount") int fullCount,
			@ApiParam(value = "游戏serverAddr") @RequestParam(value = "serverAddr") String serverAddr,
			@ApiParam(value = "游戏serverName") @RequestParam(value = "serverName") String serverName) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.UpdateGameItem(appKey, secret, serverId, kindId, nodeId, sortId, serverKind, serverType,
					serverLevel, serverPort, cellScore, enterScore, serverRule, lineCount, androidCount, fullCount,
					serverAddr, serverName);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除游戏房间", notes = "")
	@RequestMapping(value = "/game/deleteGameItem", method = RequestMethod.POST)
	public ResultDataSet deleteGameItem(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "游戏secret") @RequestParam(value = "secret") String secret,
			@ApiParam(value = "游戏serverId") @RequestParam(value = "serverId") int serverId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.deleteGameItem(appKey, secret, serverId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新游戏房间在线人数", notes = "")
	@RequestMapping(value = "/game/updateGameLineCount", method = RequestMethod.POST)
	public ResultDataSet updateGameLineCount(
			@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "游戏secret") @RequestParam(value = "secret") String secret,
			@ApiParam(value = "游戏serverId") @RequestParam(value = "serverId") int serverId,
			@ApiParam(value = "游戏lineCount") @RequestParam(value = "lineCount") int lineCount) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.updateGameLineCount(appKey, secret, serverId, lineCount);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "删除所有游戏类型", notes = "")
	@RequestMapping(value = "/game/deleteAllGameKind", method = RequestMethod.POST)
	public ResultDataSet deleteAllGameKind(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "游戏secret") @RequestParam(value = "secret") String secret) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.deleteAllGameKind(appKey, secret);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "更新/添加游戏类型", notes = "")
	@RequestMapping(value = "/game/UpdateGameKindItem", method = RequestMethod.POST)
	public ResultDataSet UpdateGameKindItem(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "游戏secret") @RequestParam(value = "secret") String secret,
			@ApiParam(value = "游戏kindId") @RequestParam(value = "kindId") int kindId,
			@ApiParam(value = "游戏typeId") @RequestParam(value = "typeId") int typeId,
			@ApiParam(value = "游戏gameId") @RequestParam(value = "gameId") int gameId,
			@ApiParam(value = "游戏joinId") @RequestParam(value = "joinId") int joinId,
			@ApiParam(value = "游戏sortId") @RequestParam(value = "sortId") int sortId,
			@ApiParam(value = "游戏kindName") @RequestParam(value = "kindName") String kindName,
			@ApiParam(value = "游戏processName") @RequestParam(value = "processName") String processName,
			@ApiParam(value = "游戏supportMobile") @RequestParam(value = "supportMobile") boolean supportMobile,
			@ApiParam(value = "游戏gameRuleUrl") @RequestParam(value = "gameRuleUrl") String gameRuleUrl,
			@ApiParam(value = "游戏downLoadUrl") @RequestParam(value = "downLoadUrl") String downLoadUrl) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.UpdateGameKindItem(appKey, secret, kindId, gameId, typeId, joinId, sortId, kindName, processName,
					supportMobile, gameRuleUrl, downLoadUrl);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "获取游戏列表", notes = "")
	@RequestMapping(value = "/game/getGameList", method = RequestMethod.POST)
	public ResultDataSet getGameList(@ApiParam(value = "游戏appKey") @RequestParam(value = "appKey") String appKey) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.getGameList(appKey);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "用户首次开通银行", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/UserEnableInsure", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet UserEnableInsure(
			@ApiParam(value = "银行登录密码") @RequestParam(value = "strInsurePass") String strInsurePass,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID,
			@ApiParam(value = "GameID") @RequestParam(value = "GameID") Integer GameID,
			@ApiParam(value = "Gender") @RequestParam(value = "Gender") Short Gender,
			@ApiParam(value = "MobilePhone") @RequestParam(value = "MobilePhone") String MobilePhone) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.UserEnableInsure(HttpUtils.getAccIdFromHeader(), HttpUtils.getTokenFromHeader(), strInsurePass,
				clientIP, MachineID, GameID, Gender, MobilePhone);
		return rds;
	}

	@ApiOperation(value = "游戏银行存钱", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/SaveScore", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet SaveScore(@ApiParam(value = "操作金额") @RequestParam(value = "ExchangeScore") long ExchangeScore,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.SaveScore(HttpUtils.getAccIdFromHeader(), ExchangeScore, clientIP, MachineID);
		return rds;
	}

	@ApiOperation(value = "游戏银行取钱", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/TakeScore", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet TakeScore(@ApiParam(value = "操作金额") @RequestParam(value = "ExchangeScore") long ExchangeScore,
			@ApiParam(value = "银行密码") @RequestParam(value = "strPassword") String strPassword,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.TakeScore(HttpUtils.getAccIdFromHeader(), strPassword, ExchangeScore, clientIP, MachineID);
		return rds;
	}

	@ApiOperation(value = "修改银行密码", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/ModifyInsurePassword", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet ModifyInsurePassword(
			@ApiParam(value = "原始密码MD5") @RequestParam(value = "strPassword") String strPassword,
			@ApiParam(value = "新密码密码MD5") @RequestParam(value = "strNewPassword") String strNewPassword,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.ModifyInsurePassword(HttpUtils.getAccIdFromHeader(), strPassword, strNewPassword, clientIP);
		return rds;
	}

	@ApiOperation(value = "重置银行密码", notes = "" + "header中需要accId, token, clinetIp")
	@RequestMapping(value = "/game/resetInsurePassword", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet resetInsurePassword(
			@ApiParam(value = "验证码") @RequestParam(value = "verifyCode") String verifyCode,
			@ApiParam(value = "密码") @RequestParam(value = "password") String password) {
		ResultDataSet rds = new ResultDataSet();
		String clientIp = HttpUtils.getRealClientIpAddress();
		rds = game.resetInsurePassword(HttpUtils.getAccIdFromHeader(), verifyCode, password, clientIp);
		return rds;
	}

	@ApiOperation(value = "发送验证码", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/sendCodeForResetInsurePassword", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet sendCodeForResetInsurePassword() {
		ResultDataSet rds = new ResultDataSet();
		String clientIp = HttpUtils.getRealClientIpAddress();
		rds = game.sendSMSForResetInsurePassword(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "获取银行操作记录", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getBankRecord", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getBankRecord(@ApiParam(value = "起始时间") @RequestParam(value = "startTime") String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") String endTime,
			@ApiParam(value = "页数") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "当前页") @RequestParam(value = "pageIndex") int pageIndex) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getBankRecord(HttpUtils.getAccIdFromHeader(), startTime, endTime, pageSize, pageIndex);
		return rds;
	}

	@ApiOperation(value = "获取银行转账记录", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getBankRecordByTradeType", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getBankRecordByTradeType(
			@ApiParam(value = "起始时间") @RequestParam(value = "startTime") String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") String endTime,
			@ApiParam(value = "页数") @RequestParam(value = "pageSize") int pageSize,
			@ApiParam(value = "当前页") @RequestParam(value = "pageIndex") int pageIndex) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getBankRecordByTradeType(HttpUtils.getAccIdFromHeader(), startTime, endTime, pageSize, pageIndex);
		return rds;
	}

	@ApiOperation(value = "获取当日已赠送的魅力值", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getCurExchangedLoveLiness", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getCurExchangedLoveLiness() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getCurExchangedLoveLiness(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "魅力值兑换", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/exchagneLiveliness", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet exchagneLiveliness(
			@ApiParam(value = "消耗的魅力值") @RequestParam(value = "lovelinessExchanged") long lovelinessExchanged) {
		ResultDataSet rds = new ResultDataSet();
		String clientIp = HttpUtils.getRealClientIpAddress();
		rds = game.exchagneLiveliness(HttpUtils.getAccIdFromHeader(), lovelinessExchanged, clientIp);
		return rds;
	}

	@ApiOperation(value = "查询游戏兑换比率", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/queryGameExchangeRate", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet queryGameExchangeRate() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.queryGameExchangeRate();
		return rds;
	}

	@ApiOperation(value = "查询是否开通银行", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/isBankEnabled", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet isBankEnabled() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.isBankEnabled(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "检查银行密码", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/checkBankPass", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet checkBankPass(@ApiParam(value = "起始时间") @RequestParam(value = "bankPass") String bankPass) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.checkBankPass(HttpUtils.getAccIdFromHeader(), bankPass);
		return rds;
	}

	@ApiOperation(value = "修改游戏昵称", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/modifyGameNickName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet modifyGameNickName(
			@ApiParam(value = "游戏昵称") @RequestParam(value = "nickname") String nickname) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.modifyGameNickName(HttpUtils.getAccIdFromHeader(), nickname, HttpUtils.getRealClientIpAddress());
		return rds;
	}

	@ApiOperation(value = "获取游戏昵称", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGameNickName", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGameNickName(@ApiParam(value = "") @RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameNickName(accId);
		return rds;
	}

	@ApiOperation(value = "获取游戏排名", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getRankList", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getRankList(@ApiParam(value = "区间开始值,从1开始") @RequestParam(value = "rankFirst") int rankFirst,
			@ApiParam(value = "区间开始值") @RequestParam(value = "rankLast") int rankLast) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getRankList(HttpUtils.getAccIdFromHeader(), rankFirst, rankLast);
		return rds;
	}

	@ApiOperation(value = "获取游戏大厅版本号", notes = "")
	@RequestMapping(value = "/game/getGameVersion", method = RequestMethod.POST)
	public ResultDataSet getGameVersion(
			@ApiParam(value = "{PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameVersion(device);
		return rds;
	}

	@ApiOperation(value = "获取游戏支付配置", notes = "")
	@RequestMapping(value = "/game/getPayOption", method = RequestMethod.POST)
	public ResultDataSet getPayOption(
			@ApiParam(value = "{PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getPayOption(device);
		return rds;
	}

	@ApiOperation(value = "获取游戏选项，用于app审核阶段", notes = "")
	@RequestMapping(value = "/game/getGameOption", method = RequestMethod.POST)
	public ResultDataSet getGameOption(
			@ApiParam(value = "{PC游戏:10, IOS游戏:11, android:12}") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameOption(device);
		return rds;
	}

	@ApiOperation(value = "俸禄兑换游戏币", notes = "")
	@RequestMapping(value = "/game/commissionExchangeGameGold", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet commissionExchangeGameGold(
			@ApiParam(value = "要兑换的俸禄") @RequestParam(value = "commission") long commission) {
		ResultDataSet rds = new ResultDataSet();

		rds = game.commissionExchangeGameGold(HttpUtils.getAccIdFromHeader(), commission,
				HttpUtils.getRealClientIpAddress());

		return rds;
	}

	@ApiOperation(value = "银行转账", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/TransferScore", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet TransferScore(
			@ApiParam(value = "操作金额") @RequestParam(value = "TransferScore") long TransferScore,
			@ApiParam(value = "银行密码") @RequestParam(value = "InsurePass") String InsurePass,
			@ApiParam(value = "目标AccId") @RequestParam(value = "TargetAccId") String TargetAccId,
			@ApiParam(value = "操作备注") @RequestParam(value = "TransRemark") String TransRemark,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.TransferScore(HttpUtils.getAccIdFromHeader(), TransferScore, InsurePass, TargetAccId, TransRemark,
				clientIP, MachineID);
		return rds;
	}

	@ApiOperation(value = "获取游戏昵称 和 accId", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGameInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGameInfo(@ApiParam(value = "") @RequestParam(value = "gameId") String gameId) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameInfo(gameId);
		return rds;
	}

	@ApiOperation(value = "购买游戏VIP", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/buyGameVip", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet buyGameVip(@ApiParam(value = "会员等级") @RequestParam(value = "level") int level,
			@ApiParam(value = "开通时间") @RequestParam(value = "month") int month,
			@ApiParam(value = "机器码") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "目标用户") @RequestParam(value = "tarAccId") String tarAccId) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.buyGameVip(HttpUtils.getAccIdFromHeader(), tarAccId, level, month,
				HttpUtils.getRealClientIpAddress(), serialNum);
		return rds;
	}

	@ApiOperation(value = "查询游戏VIP信息", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGameVipInfo", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGameVipInfo() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameVipInfo(HttpUtils.getAccIdFromHeader());
		return rds;
	}

	@ApiOperation(value = "查询游戏VIP配置信息", notes = "")
	@RequestMapping(value = "/game/getGameVipConfig", method = RequestMethod.POST)
	public ResultDataSet getGameVipConfig() {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameVipConfig();
		return rds;
	}

	@ApiOperation(value = "获取指定IP下游戏积分统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGameScoreByIp", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGameScoreByIp(@ApiParam(value = "起始时间") @RequestParam(value = "starTime") long starTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameScoreByIp(HttpUtils.getAccIdFromHeader(), starTime, endTime);
		return rds;
	}

	@ApiOperation(value = "获取指定IP下游戏积分统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGameScoreByIpAndOwerId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGameScoreByIpAndOwerId(
			@ApiParam(value = "ownerId") @RequestParam(value = "ownerId") String ownerId,
			@ApiParam(value = "起始时间") @RequestParam(value = "starTime") long starTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGameScoreByIp(ownerId, starTime, endTime);
		return rds;
	}

	@ApiOperation(value = "获取指定IP下游戏时长统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGamePlayTimes", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGamePlayTimes(@ApiParam(value = "起始时间") @RequestParam(value = "starTime") long starTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGamePlayTimes(HttpUtils.getAccIdFromHeader(), starTime, endTime);
		return rds;
	}

	@ApiOperation(value = "获取指定IP下游戏时长统计", notes = "" + "header中需要accId, token")
	@RequestMapping(value = "/game/getGamePlayTimesByOwerId", method = RequestMethod.POST)
	@Authorization
	public ResultDataSet getGamePlayTimesByOwerId(
			@ApiParam(value = "ownerId") @RequestParam(value = "ownerId") String ownerId,
			@ApiParam(value = "起始时间") @RequestParam(value = "starTime") long starTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime") long endTime) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getGamePlayTimes(ownerId, starTime, endTime);
		return rds;
	}
}
