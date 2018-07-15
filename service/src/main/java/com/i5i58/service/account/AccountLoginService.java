package com.i5i58.service.account;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.account.IAccountLogin;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.ThirdAccount;
import com.i5i58.data.account.Wallet;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.ThirdAccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
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
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.RegisterAccountResult;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * Login Account Srevice port for call
 * 
 * @author frank
 *
 */
@Service(protocol = "dubbo")
public class AccountLoginService implements IAccountLogin {

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
	RabbitMqBroadcastSender rabbitMqBroadcastSender;

	@Override
	public ResultDataSet loginByOpenId(String openId, String password, int accountVersion, String clientIP, int device,
			String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenIdAndPassword(openId, password);
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
		// if (device == 1 &&
		// !StringUtils.StringIsEmptyOrNull(acc.getLastPcLoginIp())) {
		// if (!acc.getLastPcLoginIp().equals(clientIP)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// if (!acc.getLastPcLoginSerialNum().equals(serialNum)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// }

		String token = accountUtils.getToken(acc.getAccId());
		if (token == null) {
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
			token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(acc.getAccId(), token);
		}

		if (device == DeviceCode.PCLive) {
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		accountPriDao.save(acc);
		// accountUtils.loadHotAccount(acc);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		ResponseData response = new ResponseData();
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
		response.put("acc", acc);
		response.put("token", token);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginByPhoneNo(String phoneNo, String password, int accountVersion, String clientIP,
			int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByPhoneNoAndPassword(phoneNo, password);
		if (null == acc) {
			if (DeviceCode.isGame(device)) {
				Account existAcc = accountPriDao.findByPhoneNo(phoneNo);
				if (existAcc == null) {
					List<Account> bindAcc = accountPriDao.findByBindMobile(phoneNo);
					if (bindAcc != null && bindAcc.size() > 0) {
						rds.setCode(ResultCode.PARAM_INVALID.getCode());
						rds.setMsg("账户不存在，使用微信、QQ、微博登陆【胖虎直播】的玩家请使用直播ID登陆游戏！");
						return rds;
					}
				}
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("账户或密码错误，忘记密码请关注【胖虎娱乐】公众号找回密码！");
				return rds;
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("账户或密码错误");
				return rds;
			}
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		// if (device == 1 &&
		// !StringUtils.StringIsEmptyOrNull(acc.getLastPcLoginIp())) {
		// if (!acc.getLastPcLoginIp().equals(clientIP)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// if (!acc.getLastPcLoginSerialNum().equals(serialNum)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// }
		String token = accountUtils.getToken(acc.getAccId());
		if (token == null) {
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
			token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(acc.getAccId(), token);
		}

		if (device == DeviceCode.PCLive) {
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		accountPriDao.save(acc);
		// accountUtils.loadHotAccount(acc);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		ResponseData response = new ResponseData();
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
		response.put("acc", acc);
		response.put("token", token);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginByToken(String accId, int version, String clientIP, int device, String serialNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
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

		// if (device == 1 &&
		// !StringUtils.StringIsEmptyOrNull(acc.getLastPcLoginIp())) {
		// if (!acc.getLastPcLoginIp().equals(clientIP)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// if (!acc.getLastPcLoginSerialNum().equals(serialNum)) {
		// rds.setData(StringUtils.addMask(acc.getBindMobile(), '*', 2, 2));
		// rds.setCode(ResultCode.DIFF_SPACE_LOGIN.getCode());
		// return rds;
		// }
		// }
		// accountUtils.loadHotAccount(acc);
		if (device == DeviceCode.PCLive) {
			if (!clientIP.equals(acc.getLastPcLoginIp()) || !serialNum.equals(acc.getLastPcLoginSerialNum())) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("登陆状态已失效");
				return rds;
			}
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		accountPriDao.save(acc);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
		rds.setData(acc);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet sendSMSForlogin(String openId, String clientIP, int device, String serialNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenId(openId);
		if (acc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		YXResultSet rs = YunxinIM.sendSmsCode(acc.getBindMobile(), acc.getBindMobile(), "3055176");
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		if (device == DeviceCode.PCLive) {
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginBySMS(String openId, String sms, String clientIP, int device, String serialNum)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findByOpenId(openId);
		if (null == acc) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户不存在");
			return rds;
		}
		if (acc.isNullity()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账号被禁用");
			return rds;
		}
		YXResultSet rs = YunxinIM.verifySmscode(acc.getBindMobile(), sms);
		if (!rs.getCode().equals("200")) {
			System.out.println(rs.getError());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("验证码错误");
			return rds;
		}
		String token = accountUtils.getToken(acc.getAccId());
		if (token == null) {
			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(acc.getAccId());
			if (refreshTokenResult == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:logid null");
				return rds;
			}
			if (!refreshTokenResult.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
						+ refreshTokenResult.getString("desc"));
				return rds;
			}
			if (!refreshTokenResult.getInfo().getAccid().equals(acc.getAccId())) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:accid dif");
				return rds;
			}
			token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(acc.getAccId(), token);
		}
		// accountUtils.loadHotAccount(acc);
		if (device == DeviceCode.PCLive) {
			acc.setLastPcLoginIp(clientIP);
			acc.setLastPcLoginSerialNum(serialNum);
		}
		acc.setLastLoginIp(clientIP);
		accountPriDao.save(acc);
		taskUtil.performTaskOnLogon(acc.getAccId(), device);
		ResponseData response = new ResponseData();
		acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
		acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));

		response.put("acc", acc);
		response.put("token", token);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet loginByThird(int third, String thirdId, String name, String face, byte gender, String clientIP,
			int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
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
			String tokenStr = StringUtils.createUUID();
			RegisterAccountResult registerAccountResult = null;
			try {
				registerAccountResult = YunxinIM.registerAccount(accId, strOpenId, tokenStr);
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
			acc.setRegistType(String.valueOf(device));
			acc.setVersion(1);
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
			jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId, tokenStr);
			if (device == DeviceCode.PCLive) {
				acc.setLastPcLoginIp(clientIP);
				acc.setLastPcLoginSerialNum(serialNum);
			}
			acc.setLastLoginIp(clientIP);
			taskUtil.performTaskOnLogon(acc.getAccId(), device);
			if (null != acc) {
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", tokenStr);
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
				// acc.setFaceSmallUrl(face);
				// acc.setFaceOrgUrl(face);
				if (device == DeviceCode.PCLive) {
					acc.setLastPcLoginIp(clientIP);
					acc.setLastPcLoginSerialNum(serialNum);
				}
				acc.setLastLoginIp(clientIP);
				accountPriDao.save(acc);
				String token = accountUtils.getToken(acc.getAccId());
				if (token == null) {
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
					token = refreshTokenResult.getInfo().getToken();
					accountUtils.setToken(acc.getAccId(), token);
				}

				// accountUtils.loadHotAccount(acc);
				taskUtil.performTaskOnLogon(acc.getAccId(), device);
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", token);
				rds.setData(response);
				rds.setCode(ResultCode.SUCCESS.getCode());
			} else {
				String strOpenId = openIdUtils.getRandomOpenId();
				if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("id exception");
					return rds;
				}
				String tokenStr = StringUtils.createUUID();
				RegisterAccountResult registerAccountResult = null;
				try {
					registerAccountResult = YunxinIM.registerAccount(thirdAccount.getAccId(), strOpenId, tokenStr);
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
				newAcc.setRegistType(String.valueOf(device));
				newAcc.setVersion(1);
				if (device == DeviceCode.PCLive) {
					newAcc.setLastPcLoginIp(clientIP);
					newAcc.setLastPcLoginSerialNum(serialNum);
				}
				newAcc.setLastLoginIp(clientIP);
				accountPriDao.save(acc);
				Wallet wallet = new Wallet();
				wallet.setAccId(newAcc.getAccId());
				wallet.setiGold(0);
				wallet.setTicket("");
				wallet.setCommission(0);
				wallet.setDiamond(0);
				walletPriDao.save(wallet);
				/*
				 * HotWallet hotWallet = new HotWallet();
				 * hotWallet.setId(wallet.getAccId());
				 * hotWallet.setiGold(wallet.getiGold());
				 * hotWallet.setDiamond(wallet.getDiamond());
				 * hotWallet.setTicket(wallet.getTicket());
				 * hotWallet.setCommission(wallet.getCommission());
				 * hotWalletDao.save(hotWallet);
				 */
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
				/*
				 * HotAccount hotAccount = new
				 * HotAccount(thirdAccount.getAccId(), strOpenId, "", name, "",
				 * false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "", "", "", 0,
				 * 0, 0, 0, "", "", "", "", Constant.ACC_TOKEN_TIME_TO_LIVE);
				 * hotAccountsDao.save(hotAccount);
				 */
				jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + thirdAccount.getAccId(), tokenStr);
				if (null != newAcc) {
					ResponseData response = new ResponseData();
					newAcc.setPhoneNo(StringUtils.addMask(newAcc.getPhoneNo(), '*', 2, 2));
					newAcc.setEmailAddress(StringUtils.addEmailMask(newAcc.getEmailAddress(), '*', 2, 2));
					response.put("acc", newAcc);
					response.put("token", tokenStr);
					rds.setData(response);
					rds.setCode(ResultCode.SUCCESS.getCode());
				}
			}
			return rds;
		}
	}

	@Override
	public ResultDataSet loginByThird1(int third, String openId, String uId, String unionId, String name, String face,
			byte gender, String clientIP, int device, String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (third == 0 || third == 1) {
			ThirdAccount thirdAccountByOpenId = thirdAccountPriDao.findByThirdTypeAndThirdId(third, openId);
			if (thirdAccountByOpenId != null) {
				thirdAccountByOpenId.setThirdId(unionId);
				thirdAccountByOpenId.setOpenId(openId);
				thirdAccountByOpenId.setuId(uId);
				thirdAccountPriDao.save(thirdAccountByOpenId);
			}
		}
		ThirdAccount thirdAccount = thirdAccountPriDao.findByThirdTypeAndThirdId(third, unionId);
		if (thirdAccount == null) {
			String accId = StringUtils.createUUID();
			long time = DateUtils.getNowTime();
			ThirdAccount newThirdAccount = new ThirdAccount();
			newThirdAccount.setThirdType(third);
			newThirdAccount.setThirdId(unionId);
			newThirdAccount.setOpenId(openId);
			newThirdAccount.setuId(uId);
			newThirdAccount.setAccId(accId);
			newThirdAccount.setCreateTime(time);
			thirdAccountPriDao.save(newThirdAccount);

			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			String tokenStr = StringUtils.createUUID();
			RegisterAccountResult registerAccountResult = null;
			try {
				registerAccountResult = YunxinIM.registerAccount(accId, strOpenId, tokenStr);
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
			acc.setRegistType(String.valueOf(device));
			acc.setVersion(1);
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
			jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId, tokenStr);
			if (device == DeviceCode.PCLive) {
				acc.setLastPcLoginIp(clientIP);
				acc.setLastPcLoginSerialNum(serialNum);
			}
			acc.setLastLoginIp(clientIP);
			taskUtil.performTaskOnLogon(acc.getAccId(), device);
			if (null != acc) {
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", tokenStr);
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
				// acc.setFaceSmallUrl(face);
				// acc.setFaceOrgUrl(face);
				if (device == DeviceCode.PCLive) {
					acc.setLastPcLoginIp(clientIP);
					acc.setLastPcLoginSerialNum(serialNum);
				}
				acc.setLastLoginIp(clientIP);
				accountPriDao.save(acc);
				String token = accountUtils.getToken(acc.getAccId());
				if (token == null) {
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
					token = refreshTokenResult.getInfo().getToken();
					accountUtils.setToken(acc.getAccId(), token);
				}

				// accountUtils.loadHotAccount(acc);
				taskUtil.performTaskOnLogon(acc.getAccId(), device);
				ResponseData response = new ResponseData();
				acc.setPhoneNo(StringUtils.addMask(acc.getPhoneNo(), '*', 2, 2));
				acc.setEmailAddress(StringUtils.addEmailMask(acc.getEmailAddress(), '*', 2, 2));
				response.put("acc", acc);
				response.put("token", token);
				rds.setData(response);
				rds.setCode(ResultCode.SUCCESS.getCode());
			} else {
				String strOpenId = openIdUtils.getRandomOpenId();
				if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					rds.setMsg("id exception");
					return rds;
				}
				String tokenStr = StringUtils.createUUID();
				RegisterAccountResult registerAccountResult = null;
				try {
					registerAccountResult = YunxinIM.registerAccount(thirdAccount.getAccId(), strOpenId, tokenStr);
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
				newAcc.setRegistType(String.valueOf(device));
				newAcc.setVersion(1);
				if (device == DeviceCode.PCLive) {
					newAcc.setLastPcLoginIp(clientIP);
					newAcc.setLastPcLoginSerialNum(serialNum);
				}
				newAcc.setLastLoginIp(clientIP);
				accountPriDao.save(acc);
				Wallet wallet = new Wallet();
				wallet.setAccId(newAcc.getAccId());
				wallet.setiGold(0);
				wallet.setTicket("");
				wallet.setCommission(0);
				wallet.setDiamond(0);
				walletPriDao.save(wallet);
				/*
				 * HotWallet hotWallet = new HotWallet();
				 * hotWallet.setId(wallet.getAccId());
				 * hotWallet.setiGold(wallet.getiGold());
				 * hotWallet.setDiamond(wallet.getDiamond());
				 * hotWallet.setTicket(wallet.getTicket());
				 * hotWallet.setCommission(wallet.getCommission());
				 * hotWalletDao.save(hotWallet);
				 */
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
				/*
				 * HotAccount hotAccount = new
				 * HotAccount(thirdAccount.getAccId(), strOpenId, "", name, "",
				 * false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "", "", "", 0,
				 * 0, 0, 0, "", "", "", "", Constant.ACC_TOKEN_TIME_TO_LIVE);
				 * hotAccountsDao.save(hotAccount);
				 */
				jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + thirdAccount.getAccId(), tokenStr);
				if (null != newAcc) {
					ResponseData response = new ResponseData();
					newAcc.setPhoneNo(StringUtils.addMask(newAcc.getPhoneNo(), '*', 2, 2));
					newAcc.setEmailAddress(StringUtils.addEmailMask(newAcc.getEmailAddress(), '*', 2, 2));
					response.put("acc", newAcc);
					response.put("token", tokenStr);
					rds.setData(response);
					rds.setCode(ResultCode.SUCCESS.getCode());
				}
			}
			return rds;
		}
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
