package com.i5i58.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;

@Component
public class AccountUtils {

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;
	
	@Autowired
	AccountPropertySecDao accountPropertySecDao;
	
	@Autowired
	WalletPriDao walletDao;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	/**
	 * 获取token
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public String getToken(String accId) {
		if (!StringUtils.StringIsEmptyOrNull(accId) && jedisUtils.exist(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId)) {
			return jedisUtils.get(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId);
		}
		return null;
	}

	/**
	 * 验证token
	 * 
	 * @author frank
	 * @param accId
	 * @param token
	 * @return
	 */
	public boolean virifyToken(String accId, String token) {
		if (!StringUtils.StringIsEmptyOrNull(accId) && jedisUtils.exist(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId)) {
			String tok = jedisUtils.get(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId);
			if (!StringUtils.StringIsEmptyOrNull(tok) && !StringUtils.StringIsEmptyOrNull(token) && tok.equals(token)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置token
	 * 
	 * @author frank
	 * @param accId
	 * @param token
	 */
	public void setToken(String accId, String token) {
		jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId, token);
	}

	/**
	 * 重置token过期时间
	 * 
	 * @author frank
	 * @param accId
	 */
	public void setTokenExpire(String accId) {
		jedisUtils.expire(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId, Constant.ACC_TOKEN_TIME_TO_LIVE);
	}

	/**
	 * 设置jwt token
	 * 
	 * @author frank
	 * @param accId
	 * @param token
	 */
	public void setJWTToken(String accId, int device, String token) {
		jedisUtils.set(Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + device + "_" + accId, token);
		if (device == DeviceCode.WEBLive) {
			jedisUtils.expire(Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + device + "_" + accId,
					Constant.ACC_WEB_TOKEN_TIME_TO_LIVE);
		} else {
			jedisUtils.expire(Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + device + "_" + accId,
					Constant.ACC_TOKEN_TIME_TO_LIVE);
		}
	}

	/**
	 * 重置jwt token过期时间
	 * 
	 * @author frank
	 * @param accId
	 */
	public void setJWTTokenExpire(String accId, int device) {
		if (device == DeviceCode.WEBLive) {
			jedisUtils.expire(Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + device + "_" + accId,
					Constant.ACC_WEB_TOKEN_TIME_TO_LIVE);
		} else {
			jedisUtils.expire(Constant.HOT_ACCOUNT_JWTTOKEN_SET_KEY + device + "_" + accId,
					Constant.ACC_TOKEN_TIME_TO_LIVE);
		}
	}

	/**
	 * 禁用账号
	 * 
	 * @author frank
	 * @param accId
	 * @return
	 */
	public boolean nullityAccount(String accId) {
		Account acc = accountPriDao.findOne(accId);
		if (acc != null) {
			acc.setNullity(true);
			accountPriDao.save(acc);
			jedisUtils.del(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accId);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * public void loadHotAccount(Account acc) { AccountProperty accountProperty
	 * = accountPropertyDao.findOne(acc.getAccId()); HotAccount hotAccount = new
	 * HotAccount(acc.getAccId(), acc.getOpenId(), acc.getPhoneNo(),
	 * acc.getNickName(), acc.getStageName(), acc.isAnchor(), acc.getGender(),
	 * acc.getBirthDate(), acc.getFaceSmallUrl(), acc.getFaceOrgUrl(),
	 * acc.getVersion(), accountProperty.getVip(),
	 * accountProperty.getVipDeadline(), accountProperty.getRichScore(),
	 * accountProperty.getScore(), accountProperty.getMountsId(),
	 * accountProperty.getMountsName(), accountProperty.getClubCid(),
	 * accountProperty.getClubName(), 0, accountProperty.getFansCount(),
	 * accountProperty.getFocusCount(), accountProperty.getEssayCount(),
	 * accountProperty.getMedals(), acc.getLocation(), acc.getSignature(),
	 * acc.getPersonalBrief(), Constant.ACC_TOKEN_TIME_TO_LIVE);
	 * hotAccount.setAndroid(acc.isAndroid()); hotAccountDao.save(hotAccount);
	 * Wallet wallet = walletDao.findOne(acc.getAccId()); if (wallet != null) {
	 * HotWallet hotWallet = new HotWallet();
	 * hotWallet.setId(wallet.getAccId());
	 * hotWallet.setiGold(wallet.getiGold());
	 * hotWallet.setDiamond(wallet.getDiamond());
	 * hotWallet.setTicket(wallet.getTicket());
	 * hotWallet.setGiftTicket(wallet.getGiftTicket());
	 * hotWallet.setCommission(wallet.getCommission());
	 * hotWalletDao.save(hotWallet); } setTokenExpire(acc.getAccId()); }
	 */

	/**
	 * 获取娱乐积分对应的等级
	 * 
	 * @author frank
	 * @param score
	 * @return
	 */
	public static int getRichScoreLevel(long score) {
		score = score / 100;
		if (score <= 10) {
			return 1;
		}
		if (score <= 100) {
			return 2;
		}
		if (score <= 200) {
			return 3;
		}
		if (score <= 500) {
			return 4;
		}
		if (score <= 800) {
			return 5;
		}
		if (score <= 2000) {
			return 6;
		}
		if (score <= 5000) {
			return 7;
		}
		if (score <= 10000) {
			return 8;
		}
		if (score <= 20000) {
			return 9;
		}
		if (score <= 50000) {
			return 10;
		}
		if (score <= 100000) {
			return 11;
		}
		if (score <= 200000) {
			return 12;
		}
		if (score <= 300000) {
			return 13;
		}
		if (score <= 400000) {
			return 14;
		}
		if (score <= 500000) {
			return 15;
		}
		if (score <= 600000) {
			return 16;
		}
		if (score <= 800000) {
			return 17;
		}
		if (score <= 1000000) {
			return 18;
		}
		if (score <= 2000000) {
			return 19;
		}
		if (score <= 3000000) {
			return 20;
		}
		return 20;
	}
	
	public int getVip(String accId){
		AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
		if (accountProperty == null)
			return 0;
		if (accountProperty.getVipDeadline() < DateUtils.getNowTime())
			return 0;
		return accountProperty.getVip();
	}
}
