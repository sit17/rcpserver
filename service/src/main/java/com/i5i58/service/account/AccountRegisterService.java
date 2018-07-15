package com.i5i58.service.account;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountRegister;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountAuth;
import com.i5i58.data.account.AccountConfig;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.Wallet;
import com.i5i58.primary.dao.account.AccountConfigPriDao;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.AuthUserPriDao;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.util.CheckIdCard;
import com.i5i58.util.CheckUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.DeviceCode;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.RegisterAccountResult;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * Register Account Srevice port for call
 * 
 * @author frank
 *
 */
@Service(protocol = "dubbo")
public class AccountRegisterService implements IAccountRegister {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	AuthUserPriDao authUserPriDao;

	@Autowired
	AccountConfigPriDao accountConfigPriDao;

	public ResultDataSet registerAccount(String phoneNo, String pword, String clientIP, String verifCode, int device,
			String serialNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		String password = pword.trim();
		try {
			if (!CheckUtils.validPhoneNum("0", phoneNo)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请输入正确手机号");
				return rds;
			}
			if (!CheckUtils.IsPassword(password)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码只能为字母和数字,且必须同时包含字母和数字");
				return rds;
			}
			if (password.length() < 8 || password.length() > 20) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码长度为8-20个字符");
				return rds;
			}
			if (CheckUtils.IsNumber(password) && password.length() < 10) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码不能为9位一下纯数字");
				return rds;
			}
			Account account = accountPriDao.findByPhoneNo(phoneNo);
			if (account != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该手机号码已注册");
				return rds;
			}
			password = StringUtils.getMd5(password);
			/*
			 * HotPhoneVerification pv =
			 * phoneVerificationRepository.findOne(phoneNo); if (pv == null) {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg("无效验证"); return rds; }
			 * System.out.println(pv.getPhoneNo());
			 * System.out.println(pv.getVerification()); if
			 * (!pv.getVerification().equals(verifCode)) {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg("验证码错误"); return rds; }
			 */
			YXResultSet rs = YunxinIM.verifySmscode(phoneNo, verifCode);
			if (!rs.getCode().equals("200")) {
				System.out.println(rs.getError());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("验证码错误");
				return rds;
			}
			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			String accid = StringUtils.createUUID();
			String tokenStr = StringUtils.createUUID();
			RegisterAccountResult registerAccountResult = null;
			try {
				registerAccountResult = YunxinIM.registerAccount(accid, strOpenId, tokenStr);
			} catch (Exception ex) {
				System.out.println(ex.toString());
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
			Account acc = new Account();
			acc.setOpenId(strOpenId);
			acc.setAccId(accid);
			acc.setPhoneNo(phoneNo);
			acc.setBindMobile(phoneNo);
			acc.setNickName(strOpenId);
			acc.setPassword(password);
			acc.setRegistDate(DateUtils.getNowTime());
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

			AccountConfig accountConfig = new AccountConfig();
			accountConfig.setAccId(acc.getAccId());
			accountConfig.setAddFirendsType(0);
			accountConfig.setEnableNoDisturb(false);
			accountConfig.setEnableNoticedOnLive(true);
			accountConfigPriDao.save(accountConfig);

			/*
			 * HotAccount hotAccount = new HotAccount(accid, strOpenId, phoneNo,
			 * strOpenId, "", false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "",
			 * "", "", 0, 0, 0, 0, "", "", "", "",
			 * Constant.ACC_TOKEN_TIME_TO_LIVE);
			 * hotAccountsDao.save(hotAccount);
			 */
			jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accid, tokenStr);
			if (null != acc) {
				rds.setData(acc);
				rds.setCode(ResultCode.SUCCESS.getCode());
			}
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			// throw new RuntimeException("事务失败");
			rds.setMsg(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet sendCode(String phoneNo) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}

		Account acc = accountPriDao.findByPhoneNo(phoneNo);
		if (acc != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该手机号码已注册");
			return rds;
		}

		/*
		 * Random random = new Random(DateUtils.getNowTime()); int i =
		 * random.nextInt(999999); if (i < 100000) { i += 100000; } i =
		 * 888888;// for test System.out.println(Integer.toString(i));
		 */
		YXResultSet rs = YunxinIM.sendSmsCode(phoneNo, phoneNo, "0");
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		// HotPhoneVerification phoneVerification = new HotPhoneVerification();
		// phoneVerification.setPhoneNo(phoneNo);
		// phoneVerification.setVerification(Integer.toString(i));
		// phoneVerification.setExpiration(900);
		// phoneVerificationRepository.save(phoneVerification);
		System.out.println("sendid:" + rs.getString("msg") + "; code:" + rs.getString("obj"));
		ResponseData responseData = new ResponseData();
		String maskPhoneNo = StringUtils.addMask(phoneNo, '*', 2, 2);
		responseData.put("phoneNo", maskPhoneNo);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet checkCode(String phoneNo, String verifCode) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		/*
		 * HotPhoneVerification pv =
		 * phoneVerificationRepository.findOne(phoneNo); if (pv == null) {
		 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
		 * rds.setMsg("请先获取验证码"); return rds; }
		 * System.out.println(pv.getPhoneNo());
		 * System.out.println(pv.getVerification()); if
		 * (!pv.getVerification().equals(verifCode)) {
		 * rds.setCode(ResultCode.PARAM_INVALID.getCode()); rds.setMsg("验证码错误");
		 * return rds; }
		 */
		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}
		YXResultSet rs = YunxinIM.verifySmscode(phoneNo, verifCode);
		if (rs.getCode().equals("200")) {
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
		}
		return rds;
	}

	@Override
	public ResultDataSet checkPhoneNo(String phoneNo) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}
		Account acc = accountPriDao.findByPhoneNo(phoneNo);
		if (acc != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该手机号码已注册");
			return rds;
		}
		return rds;
	}

	@Override
	public ResultDataSet registerAccountTemp(String phoneNo, String pword, String clientIP, String verifCode,
			int device, String serialNum, String realName, String idCard) throws IOException {

		ResultDataSet rds = new ResultDataSet();
		String password = pword.trim();
		try {
			if (!CheckUtils.validPhoneNum("0", phoneNo)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请输入正确手机号");
				return rds;
			}
			if (!CheckUtils.IsPassword(password)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码只能为字母和数字,且必须同时包含字母和数字");
				return rds;
			}
			if (password.length() < 8 || password.length() > 20) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码长度为8-20个字符");
				return rds;
			}
			if (CheckUtils.IsNumber(password) && password.length() < 10) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("密码不能为9位一下纯数字");
				return rds;
			}
			if (!CheckUtils.checkIdNumber(idCard)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("身份证号码不合法");
				return rds;
			}
			if (!CheckUtils.IsChinese(realName)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("请填写真实姓名");
				return rds;
			}
			Account account = accountPriDao.findByPhoneNo(phoneNo);
			if (account != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该手机号码已注册");
				return rds;
			}
			password = StringUtils.getMd5(password);
			/*
			 * HotPhoneVerification pv =
			 * phoneVerificationRepository.findOne(phoneNo); if (pv == null) {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg("无效验证"); return rds; }
			 * System.out.println(pv.getPhoneNo());
			 * System.out.println(pv.getVerification()); if
			 * (!pv.getVerification().equals(verifCode)) {
			 * rds.setCode(ResultCode.PARAM_INVALID.getCode());
			 * rds.setMsg("验证码错误"); return rds; }
			 */
			YXResultSet rs = YunxinIM.verifySmscode(phoneNo, verifCode);
			if (!rs.getCode().equals("200")) {
				System.out.println(rs.getError());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("验证码错误");
				return rds;
			}
			if (!CheckIdCard.checkIdCardPost(realName, idCard)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("身份证信息不匹配");
				return rds;
			}
			String strOpenId = openIdUtils.getRandomOpenId();
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}
			String accid = StringUtils.createUUID();
			String tokenStr = StringUtils.createUUID();
			RegisterAccountResult registerAccountResult = null;
			try {
				registerAccountResult = YunxinIM.registerAccount(accid, strOpenId, tokenStr);
			} catch (Exception ex) {
				System.out.println(ex.toString());
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
			Account acc = new Account();
			acc.setOpenId(strOpenId);
			acc.setAccId(accid);
			acc.setPhoneNo(phoneNo);
			acc.setNickName(strOpenId);
			acc.setPassword(password);
			acc.setRegistDate(DateUtils.getNowTime());
			acc.setRegistIp(clientIP);
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

			AccountAuth authUser = null;
			authUser = new AccountAuth();
			authUser.setAccId(accid);
			authUser.setAuthed(true);
			authUser.setCertificateId(idCard);
			authUser.setRealName(realName);
			authUserPriDao.save(authUser);

			AccountConfig accountConfig = new AccountConfig();
			accountConfig.setAccId(acc.getAccId());
			accountConfig.setAddFirendsType(0);
			accountConfig.setEnableNoDisturb(false);
			accountConfig.setEnableNoticedOnLive(true);
			accountConfigPriDao.save(accountConfig);

			/*
			 * HotAccount hotAccount = new HotAccount(accid, strOpenId, phoneNo,
			 * strOpenId, "", false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "",
			 * "", "", 0, 0, 0, 0, "", "", "", "",
			 * Constant.ACC_TOKEN_TIME_TO_LIVE);
			 * hotAccountsDao.save(hotAccount);
			 */

			jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accid, tokenStr);
			if (null != acc) {
				rds.setData(acc);
				rds.setCode(ResultCode.SUCCESS.getCode());
			}
			return rds;
		} catch (Exception e) {
			logger.error("", e);
			// throw new RuntimeException("事务失败");
			rds.setMsg(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}
}
