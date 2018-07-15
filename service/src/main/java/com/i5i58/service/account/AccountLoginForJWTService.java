package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.account.IAccountLoginForJWT;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.ThirdAccount;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.wechat.WechatAccount;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.ThirdAccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.wechat.WechatAccountPriDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.ThirdAccountSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.SecurityUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.RegisterAccountResult;

/**
 * Login Account Srevice for JWT port for call
 * 
 * @author frank
 *
 */
@Service(protocol = "dubbo")
public class AccountLoginForJWTService implements IAccountLoginForJWT {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	ThirdAccountPriDao thirdAccountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	ThirdAccountSecDao thirdAccountSecDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	TaskUtil taskUtil;

	@Autowired
	WechatAccountPriDao wechatAccountPriDao;
	
	@Autowired
	RabbitMqBroadcastSender rabbitMqBroadcastSender;

	private ResultDataSet login(Account acc, String clientIP, int device, String serialNum, boolean byToken)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		// ================JWT================
		if (device == DeviceCode.PCLive) {
			if (byToken) {
				if (!clientIP.equals(acc.getLastPcLoginIp()) || !serialNum.equals(acc.getLastPcLoginSerialNum())) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("登陆状态已失效");
					return rds;
				}
			}
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		String yxToken = acc.getYxToken();
		if (StringUtils.StringIsEmptyOrNull(yxToken)) {
			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
			if (refreshTokenResult == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:null");
				return rds;
			}
			if (!refreshTokenResult.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
						+ refreshTokenResult.getString("desc"));
				return rds;
			}
			logger.info(String.format("db.accId:%s, yxinfo.accId:%s, yxinfo.token:%s", acc.getAccId(),
					refreshTokenResult.getInfo().getAccid(), refreshTokenResult.getInfo().getToken()));
			if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:accid dif");
				return rds;
			}
			yxToken = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(acc.getAccId(), yxToken);
		}
		acc.setYxToken(yxToken);
		accountPriDao.save(acc);
		String token = SecurityUtils.createJsonWebToken(acc.getAccId(), device, serialNum);
		accountUtils.setJWTToken(acc.getAccId(), device, token);
		ResponseData response = new ResponseData();
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
		response.put("acc", acc);
		response.put("yxToken", yxToken);
		if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
			response.put("super", true);
		}
		if (device == DeviceCode.WEBLive) {
			rds.setInnerData(token);
		} else {
			response.put("token", token);
		}
		rds.setData(response);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		// ================JWT================

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginByOpenId(String openId, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException {
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
		return login(acc, clientIP, device, serialNum, false);
	}

	@Override
	public ResultDataSet loginByPhoneNo(String phoneNo, String password, int accountVersion, String clientIP,
			int device, String serialNum) throws IOException {
		Account acc = accountPriDao.findByPhoneNoAndPassword(phoneNo, password);
		return login(acc, clientIP, device, serialNum, false);
	}

	@Override
	public ResultDataSet loginByToken(String accId, int version, String clientIP, int device, String serialNum)
			throws IOException {
		Account acc = accountPriDao.findOne(accId);
		return login(acc, clientIP, device, serialNum, true);
	}

	@Override
	public ResultDataSet loginByThird(int third, String thirdId, String name, String face, byte gender, String clientIP,
			int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (device == DeviceCode.PCLive || device == DeviceCode.PCGame || device == DeviceCode.WEBLive) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("device exception");
			return rds;
		}
		ThirdAccount thirdAccount = thirdAccountPriDao.findByThirdTypeAndThirdId(third, thirdId);
		if (thirdAccount == null) {
			String accId = StringUtils.createUUID();
			long time = DateUtils.getNowTime();
			ThirdAccount newThirdAccount = new ThirdAccount();
			newThirdAccount.setThirdId(thirdId);
			newThirdAccount.setThirdType(third);
			newThirdAccount.setAccId(accId);
			newThirdAccount.setCreateTime(time);
			thirdAccountPriDao.save(newThirdAccount);

			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			String yxToken = StringUtils.createUUID();
			RegisterAccountResult registerAccountResult = null;
			try {
				registerAccountResult = YunxinIM.registerAccount(accId, strOpenId, yxToken);
			} catch (Exception ex) {
				logger.error("", ex);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:register");
				return rds;
			}
			if (registerAccountResult == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yxException:reg null");
				return rds;
			}
			if (!registerAccountResult.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(registerAccountResult.getString("desc"));
				return rds;
			}
			if (name.length() > 20) {
				name = name.substring(0, 20);
			}
			Account acc = new Account();
			acc.setOpenId(strOpenId);
			acc.setAccId(accId);
			acc.setNickName(name);
			acc.setPassword("");
			acc.setFaceSmallUrl(face);
			acc.setFaceUseInGame(true);
			acc.setFaceOrgUrl(face);
			acc.setGender(gender);
			acc.setRegistDate(time);
			acc.setRegistIp(clientIP);
			acc.setVersion(1);
			acc.setYxToken(yxToken);
			acc.setLastLoginIp(clientIP);
			accountPriDao.save(acc);

			Wallet wallet = new Wallet();
			wallet.setAccId(acc.getAccId());
			wallet.setiGold(0);
			wallet.setTicket("");
			wallet.setCommission(0);
			wallet.setDiamond(0);
			walletPriDao.save(wallet);

			AccountProperty accountProperty = new AccountProperty();
			accountProperty.setAccId(acc.getAccId());
			accountProperty.setEssayCount(0);
			accountProperty.setFansCount(0);
			accountProperty.setFocusCount(0);
			accountProperty.setMedals("");
			accountProperty.setMountsId(0);
			accountProperty.setMountsName("");
			accountProperty.setRichScore(0);
			accountProperty.setScore(0);
			accountProperty.setVip(0);
			accountProperty.setVipPurchase((byte) 0);
			accountPropertyPriDao.save(accountProperty);
			/*
			 * HotAccount hotAccount = new HotAccount(accId, strOpenId, "",
			 * name, "", false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "", "",
			 * "", 0, 0, 0, 0, "", "", "", "", Constant.ACC_TOKEN_TIME_TO_LIVE);
			 * hotAccountsDao.save(hotAccount);
			 */
			String token = SecurityUtils.createJsonWebToken(acc.getAccId(), device, serialNum);
			accountUtils.setJWTToken(acc.getAccId(), device, token);
			taskUtil.performTaskOnLogon(acc.getAccId(), device);
			if (null != acc) {
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", token);
				response.put("yxToken", yxToken);
				if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
					response.put("super", true);
				}
				rds.setData(response);
				rds.setCode(ResultCode.SUCCESS.getCode());
			}
			return rds;
		} else {
			Account acc = accountPriDao.findOne(thirdAccount.getAccId());
			if (null != acc) {
				if (acc.isNullity()) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("账号被禁用");
					return rds;
				}
				acc.setGender(gender);
				acc.setFaceSmallUrl(face);
				acc.setFaceOrgUrl(face);
				acc.setLastLoginIp(clientIP);
				if (StringUtils.StringIsEmptyOrNull(acc.getYxToken())) {
					RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
					if (refreshTokenResult == null) {
						rds.setCode(ResultCode.SERVICE_ERROR.getCode());
						rds.setMsg("yx_exception:null");
						return rds;
					}
					if (!refreshTokenResult.getCode().equals("200")) {
						rds.setCode(ResultCode.SERVICE_ERROR.getCode());
						rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
								+ refreshTokenResult.getString("desc"));
						return rds;
					}
					logger.info(String.format("db.accId:%s, yxinfo.accId:%s, yxinfo.token:%s", acc.getAccId(),
							refreshTokenResult.getInfo().getAccid(), refreshTokenResult.getInfo().getToken()));
					if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
						rds.setCode(ResultCode.SERVICE_ERROR.getCode());
						rds.setMsg("yx_exception:accid dif");
						return rds;
					}
					acc.setYxToken(refreshTokenResult.getInfo().getToken());
				}
				accountPriDao.save(acc);

				String token = SecurityUtils.createJsonWebToken(acc.getAccId(), device, serialNum);
				accountUtils.setJWTToken(acc.getAccId(), device, token);

				taskUtil.performTaskOnLogon(acc.getAccId(), device);
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", token);
				response.put("yxToken", acc.getYxToken());
				if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
					response.put("super", true);
				}
				rds.setData(response);
				rds.setCode(ResultCode.SUCCESS.getCode());
			} else {
				String strOpenId = openIdUtils.getRandomOpenId();
				if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("id exception");
					return rds;
				}
				String yxToken = StringUtils.createUUID();
				RegisterAccountResult registerAccountResult = null;
				try {
					registerAccountResult = YunxinIM.registerAccount(thirdAccount.getAccId(), strOpenId, yxToken);
				} catch (Exception ex) {
					logger.error("", ex);
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yxException:register");
					return rds;
				}
				if (registerAccountResult == null) {
					logger.error("yxException:reg null");
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("yxException:reg null");
					return rds;
				}
				if (!registerAccountResult.getCode().equals("200")) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg(registerAccountResult.getString("desc"));
					return rds;
				}
				if (name.length() > 32) {
					name = name.substring(0, 31);
				}
				Account newAcc = new Account();
				newAcc.setOpenId(strOpenId);
				newAcc.setAccId(thirdAccount.getAccId());
				newAcc.setPhoneNo("");
				newAcc.setNickName(name);
				newAcc.setPassword("");
				newAcc.setFaceSmallUrl(face);
				newAcc.setFaceOrgUrl(face);
				newAcc.setGender(gender);
				newAcc.setRegistDate(thirdAccount.getCreateTime());
				newAcc.setRegistIp(clientIP);
				newAcc.setYxToken(yxToken);
				newAcc.setLastLoginIp(clientIP);
				newAcc.setVersion(1);
				accountPriDao.save(acc);

				Wallet wallet = new Wallet();
				wallet.setAccId(newAcc.getAccId());
				wallet.setiGold(0);
				wallet.setTicket("");
				wallet.setCommission(0);
				wallet.setDiamond(0);
				walletPriDao.save(wallet);

				AccountProperty accountProperty = new AccountProperty();
				accountProperty.setAccId(newAcc.getAccId());
				accountProperty.setEssayCount(0);
				accountProperty.setFansCount(0);
				accountProperty.setFocusCount(0);
				accountProperty.setMedals("");
				accountProperty.setMountsId(0);
				accountProperty.setMountsName("");
				accountProperty.setRichScore(0);
				accountProperty.setScore(0);
				accountProperty.setVip(0);
				accountProperty.setVipPurchase((byte) 0);
				accountPropertyPriDao.save(accountProperty);

				String token = SecurityUtils.createJsonWebToken(newAcc.getAccId(), device, serialNum);
				accountUtils.setJWTToken(newAcc.getAccId(), device, token);
				if (null != newAcc) {
					ResponseData response = new ResponseData();
					newAcc.setPhoneNo(StringUtils.addMask(newAcc.getPhoneNo(), '*', 2, 2));
					newAcc.setEmailAddress(StringUtils.addEmailMask(newAcc.getEmailAddress(), '*', 2, 2));
					response.put("acc", newAcc);
					response.put("token", token);
					response.put("yxToken", yxToken);
					rds.setData(response);
					rds.setCode(ResultCode.SUCCESS.getCode());
				}
			}
			return rds;
		}
	}

	@Override
	public ResultDataSet loginByWechatOpenId(String wechatOpenId, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		WechatAccount Waccount = wechatAccountPriDao.findByOpenIdAndSelected(wechatOpenId, true);
		if (Waccount != null) {
			Account acc = accountPriDao.findOne(Waccount.getAccId());
			return login(acc, clientIP, device, serialNum, false);
		}
		rds.setCode(ResultCode.PARAM_INVALID.getCode());
		rds.setMsg("未绑定过的微信账号");
		return rds;
	}

	@Override
	public ResultDataSet loginByQrCode(String accId, String qrString) {
		
		JSONObject rep = JSON.parseObject(qrString);
		int device = rep.getIntValue("device");
		int version = rep.getIntValue("version");
		String serialNum = rep.getString("serialNum");
		String clientIp = rep.getString("ip");
		String qrToken = rep.getString("qrToken");
		
		try {
			ResultDataSet loginRds = loginByToken(accId, version, clientIp, device, serialNum);
			if (loginRds.getCode().equals(ResultCode.SUCCESS.getCode())){
				ResponseData responseData = new ResponseData();
				responseData.put("cmd", "qrLoginResult");
				
				ResponseData data = new ResponseData();
				data.put("qrToken", qrToken);
				data.put("result", loginRds.getData());
				
				responseData.put("data", data);
				
				String qrLoginNotify = new JsonUtils().toJson(responseData);
				rabbitMqBroadcastSender.sendQrLoginMessage(qrLoginNotify);
			}
			return loginRds;
		} catch (IOException e) {
			logger.error(e);
			ResultDataSet rds = new ResultDataSet();
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("登录失败");
			return rds;
		}
		
	}
}
