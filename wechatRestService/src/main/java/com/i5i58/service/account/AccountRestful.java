package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.i5i58.apis.account.IAccountPersonal;
import com.i5i58.apis.account.IAccountSecurity;
import com.i5i58.apis.account.IAccountWechat;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.game.IGame;
import com.i5i58.apis.pay.IPay;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.Authorization;
import com.i5i58.util.web.HttpUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "寰俊涓撶敤-鐢ㄦ埛淇℃伅鏈嶅姟")
@RestController
@RequestMapping("${rpc.controller.version}")
public class AccountRestful {

	private Logger logger = Logger.getLogger(getClass());

	@Reference
	private IAccountPersonal personal;

	@Reference
	private IAccountSecurity security;

	@Reference
	private IGame game;

	@Reference
	private IPay pay;

	@Reference
	private IAccountWechat wechat;

	@ApiOperation(value = "鏌ヨ鎴戠殑閽卞寘", notes = "")
	@RequestMapping(value = "/wechat/getWallet", method = RequestMethod.POST)
	public ResultDataSet getWallet(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.getWallet(accId);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "鑾峰彇涓汉淇℃伅", notes = "鐐瑰嚮灏忓ご鍍忔椂")
	@RequestMapping(value = "/wechat/getMyPersonal", method = RequestMethod.POST)
	public ResultDataSet getMyPersonal(@RequestParam(value = "accId") String accId) {
		ResultDataSet rds = new ResultDataSet();
		rds = personal.getMyPersonal(accId);
		return rds;
	}

	@ApiOperation(value = "缁戝畾寰俊鍏紬鍙�", notes = "鎴愬姛杩斿洖鐢ㄦ埛瀵硅薄锛屽け璐ワ細楠岃瘉鐮侀敊璇�")
	@RequestMapping(value = "/wechat/bindWechatAccount", method = RequestMethod.POST)
	public ResultDataSet bindWechatAccount(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "openId") String openId) {

		ResultDataSet rds = new ResultDataSet();
		try {
			rds = wechat.bindWechatAccount(accId, openId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "璁惧畾缁戝畾鐨勯粯璁ゅ钩鍙拌处鍙�", notes = "鎴愬姛杩斿洖鐢ㄦ埛瀵硅薄锛屽け璐ワ細楠岃瘉鐮侀敊璇�")
	@RequestMapping(value = "/wechat/setCurWechatAccount", method = RequestMethod.POST)
	public ResultDataSet setCurWechatAccount(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "openId") String openId) {

		ResultDataSet rds = new ResultDataSet();
		try {
			rds = wechat.setCurWechatAccount(accId, openId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "閲嶇疆瀵嗙爜鐨勬椂鍊欙紝鍙戦�佹墜鏈洪獙璇佺爜", notes = "娴嬭瘯楠岃瘉鐮佷负888888")
	@RequestMapping(value = "/wechat/sendVerifToPhone", method = RequestMethod.POST)
	public ResultDataSet sendVerifToPhone(
			@ApiParam(value = "璐﹀彿绫诲瀷,鍙互鏄痑ccId,openId,phoneNo鎴杄mail") @RequestParam(value = "type") String type,
			@ApiParam(value = "璐﹀彿鍐呭") @RequestParam(value = "account") String account) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.sendVerifToPhone(type, account);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "閲嶇疆瀵嗙爜鐨勬椂鍊欙紝妫�鏌ラ獙璇佺爜鏄惁姝ｇ‘", notes = "杩斿洖 data : {valid=true/false}")
	@RequestMapping(value = "/wechat/checkVerifyCode", method = RequestMethod.POST)
	public ResultDataSet checkVerifyCode(
			@ApiParam(value = "璐﹀彿绫诲瀷,鍙互鏄痑ccId,openId,phoneNo鎴杄mail") @RequestParam(value = "type") String type,
			@ApiParam(value = "璐﹀彿鍐呭") @RequestParam(value = "account") String account,
			@ApiParam(value = "楠岃瘉鐮�") @RequestParam(value = "verifyCode") String verifyCode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = security.isValidVerifyCode(type, account, verifyCode);
		} catch (Exception e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "閲嶇疆瀵嗙爜", notes = "鎴愬姛杩斿洖璐﹀彿JSON鏁版嵁")
	@RequestMapping(value = "/wechat/resetPassword", method = org.springframework.web.bind.annotation.RequestMethod.POST)
	public ResultDataSet resetPassword(
			@ApiParam(value = "璐﹀彿绫诲瀷,鍙互鏄痑ccId,openId,phoneNo鎴杄mail") @RequestParam(value = "type") String type,
			@ApiParam(value = "璐﹀彿鍐呭") @RequestParam(value = "account") String account,
			@ApiParam(value = "鎵嬫満楠岃瘉鐮�") @RequestParam(value = "verifyCode") String verifyCode,
			@ApiParam(value = "鏂扮殑鐧诲綍瀵嗙爜") @RequestParam(value = "password") String password) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = security.resetPassword(type, account, verifyCode, password);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;

	}

	@ApiOperation(value = "璁剧疆缁戝畾鎵嬫満鍙�", notes = "")
	@RequestMapping(value = "/wechat/setBindMobile", method = RequestMethod.POST)
	public ResultDataSet setBindMobile(@RequestParam(value = "accId") String accId,
			@RequestParam(value = "phoneNo") String phoneNo, @RequestParam(value = "password") String password,
			@RequestParam(value = "verifyCode") String verifyCode) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = personal.setBindMobile(accId, phoneNo, password, verifyCode);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@ApiOperation(value = "鍏戞崲娓告垙甯�", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/exchangeGameGold", method = RequestMethod.POST)
	public ResultDataSet exchangeGameGold(@ApiParam(value = "鐢ㄦ埛accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "娓告垙appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "闇�瑕佸厬鎹㈢殑I甯�") @RequestParam(value = "iGold") long iGold,
			@ApiParam(value = "鐜版湁娓告垙甯侊紝鍏戞崲鍓�") @RequestParam(value = "gameGold") long gameGold,
			@ApiParam(value = "璁惧ID, {PC娓告垙:10, IOS娓告垙:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "璁惧搴忓垪鍙�") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "閾惰瀵嗙爜") @RequestParam(value = "insurePass") String insurePass) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.exchangeGameGold(accId, appKey, iGold, gameGold, HttpUtils.getRealClientIpAddress(), device,
					serialNum, insurePass);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "鍏�        鎹㈤捇鐭�", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/exchangeDiamond", method = RequestMethod.POST)
	public ResultDataSet exchangeDiamond(@ApiParam(value = "鐢ㄦ埛accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "娓告垙appKey") @RequestParam(value = "appKey") String appKey,
			@ApiParam(value = "闇�瑕佸厬鎹㈢殑閽荤煶") @RequestParam(value = "diamond") long diamond,
			@ApiParam(value = "鐜版湁娓告垙甯侊紝鍏戞崲鍓�") @RequestParam(value = "gameGold") long gameGold,
			@ApiParam(value = "璁惧ID, {PC娓告垙:10, IOS娓告垙:11, android:12}") @RequestParam(value = "device") int device,
			@ApiParam(value = "璁惧搴忓垪鍙�") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "閾惰瀵嗙爜") @RequestParam(value = "insurePass") String insurePass) {
		ResultDataSet rds = new ResultDataSet();
		try {
			rds = game.exchangeDiamond(accId, appKey, diamond, gameGold, HttpUtils.getRealClientIpAddress(), device,
					serialNum, insurePass);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "淇敼閾惰瀵嗙爜", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/ModifyInsurePassword", method = RequestMethod.POST)
	public ResultDataSet ModifyInsurePassword(@ApiParam(value = "鐢ㄦ埛accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "鍘熷瀵嗙爜MD5") @RequestParam(value = "strPassword") String strPassword,
			@ApiParam(value = "鏂板瘑鐮佸瘑鐮丮D5") @RequestParam(value = "strNewPassword") String strNewPassword,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.ModifyInsurePassword(accId, strPassword, strNewPassword, clientIP);
		return rds;
	}

	@ApiOperation(value = "閲嶇疆閾惰瀵嗙爜", notes = "" + "header涓渶瑕乤ccId, token, clinetIp")
	@RequestMapping(value = "/wechat/resetInsurePassword", method = RequestMethod.POST)
	public ResultDataSet resetInsurePassword(@ApiParam(value = "鐢ㄦ埛accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "楠岃瘉鐮�") @RequestParam(value = "verifyCode") String verifyCode,
			@ApiParam(value = "瀵嗙爜") @RequestParam(value = "password") String password) {
		ResultDataSet rds = new ResultDataSet();
		String clientIp = HttpUtils.getRealClientIpAddress();
		rds = game.resetInsurePassword(accId, verifyCode, password, clientIp);
		return rds;
	}

	@ApiOperation(value = "榄呭姏鍊煎厬鎹�", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/exchagneLiveliness", method = RequestMethod.POST)
	public ResultDataSet exchagneLiveliness(@ApiParam(value = "鐢ㄦ埛accId") @RequestParam(value = "accId") String accId,
			@ApiParam(value = "娑堣�楃殑榄呭姏鍊�") @RequestParam(value = "lovelinessExchanged") long lovelinessExchanged) {
		ResultDataSet rds = new ResultDataSet();
		String clientIp = HttpUtils.getRealClientIpAddress();
		rds = game.exchagneLiveliness(accId, lovelinessExchanged, clientIp);
		return rds;
	}

	@ApiOperation(value = "鐢熸垚鍏呭�艰鍗�", notes = "鎴愬姛杩斿洖璐﹀彿JSON鏁版嵁")
	@RequestMapping(value = "/wechat/createOrder", method = RequestMethod.POST)
	public ResultDataSet createOrder(@ApiParam(value = "骞冲彴ID") @RequestParam(value = "shareId") int shareId,
			@ApiParam(value = "AccId") @RequestParam(value = "AccId") String AccId,
			@ApiParam(value = "鐩爣AccId") @RequestParam(value = "toAccId") String toAccId,
			@ApiParam(value = "璁㈠崟閲戦") @RequestParam(value = "orderAmount") long orderAmount,
			@ApiParam(value = "瀹炰粯閲戦") @RequestParam(value = "payAmount") long payAmount,
			@ApiParam(value = "璁惧涓茬爜") @RequestParam(value = "serialNum") String serialNum,
			@ApiParam(value = "璁惧") @RequestParam(value = "device") int device) {
		ResultDataSet rds = new ResultDataSet();

		try {
			String clientIp = HttpUtils.getRealClientIpAddress();
			rds = pay.createOrder(AccId, toAccId, shareId, orderAmount, payAmount, clientIp, serialNum, device);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}

	@ApiOperation(value = "閫氳繃openid鏌ユ壘accid", notes = "鎴愬姛杩斿洖璐﹀彿JSON鏁版嵁")
	@RequestMapping(value = "/wechat/getAccIdByOpenId", method = RequestMethod.POST)
	public ResultDataSet getAccIdByOpenId(@ApiParam(value = "OpenId") @RequestParam(value = "OpenId") String OpenId) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = wechat.getAccIdByOpenId(OpenId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "閫氳繃liveopenid鏌ユ壘accid", notes = "鎴愬姛杩斿洖璐﹀彿JSON鏁版嵁")
	@RequestMapping(value = "/wechat/getAccIdByLiveOpenId", method = RequestMethod.POST)
	public ResultDataSet getAccIdByLiveOpenId(@ApiParam(value = "liveOpenId") @RequestParam(value = "liveOpenId") String liveOpenId) {
		ResultDataSet rds = new ResultDataSet();

		try {
			rds = wechat.getAccIdByLiveOpenId(liveOpenId);
		} catch (IOException e) {
			logger.error(HttpUtils.getParamsString(), e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("Service Error");
		}
		return rds;
	}
	
	@ApiOperation(value = "鑾峰彇閾惰", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/getBank", method = RequestMethod.POST)
	public ResultDataSet getBank(@ApiParam(value = "AccId") @RequestParam(value = "AccId") String AccId) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.getBank(AccId);
		return rds;
	}
	

	@ApiOperation(value = "閾惰杞处", notes = "" + "header涓渶瑕乤ccId, token")
	@RequestMapping(value = "/wechat/TransferScore", method = RequestMethod.POST)
	public ResultDataSet TransferScore(@ApiParam(value = "AccId") @RequestParam(value = "AccId") String AccId,
			@ApiParam(value = "鎿嶄綔閲戦") @RequestParam(value = "TransferScore") long TransferScore,
			@ApiParam(value = "閾惰瀵嗙爜") @RequestParam(value = "InsurePass") String InsurePass,
			@ApiParam(value = "鐩爣AccId") @RequestParam(value = "TargetAccId") String TargetAccId,
			@ApiParam(value = "鎿嶄綔澶囨敞") @RequestParam(value = "TransRemark") String TransRemark,
			@ApiParam(value = "clientIP") @RequestParam(value = "clientIP") String clientIP,
			@ApiParam(value = "MachineID") @RequestParam(value = "MachineID") String MachineID) {
		ResultDataSet rds = new ResultDataSet();
		rds = game.TransferScore(AccId, TransferScore, InsurePass, TargetAccId, TransRemark, clientIP, MachineID);
		return rds;
	}
}
