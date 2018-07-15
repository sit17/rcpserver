package com.i5i58.service.account;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountConfig;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountConfig;
import com.i5i58.data.account.PrettyOpenId;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.account.AccountConfigPriDao;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.PrettyOpenIdPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.secondary.dao.account.AccountConfigSecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.PrettyOpenIdSecDao;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.utils.VersionUtils;

@Service(protocol = "dubbo")
public class AccountConfigService implements IAccountConfig {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AccountConfigPriDao configPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	AccountConfigSecDao configSecDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	PrettyOpenIdSecDao prettyOpenIdSecDao;

	@Autowired
	PrettyOpenIdPriDao prettyOpenIdPriDao;

	@Autowired
	TaskUtil taskUtil;
	
	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Override
	public ResultDataSet updateLivingNotify(boolean notifyEnable, String accId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			Account account = accountPriDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			AccountConfig config = configPriDao.findByAccId(accId);
			if (config == null) {
				AccountConfig newConfig = new AccountConfig();
				newConfig.setAccId(accId);
				newConfig.setAddFirendsType(0);
				newConfig.setEnableNoticedOnLive(notifyEnable);
				newConfig.setEnableNoDisturb(false);
				configPriDao.save(newConfig);
				rds.setData(newConfig);
			} else {
				config.setEnableNoticedOnLive(notifyEnable);
				configPriDao.save(config);
				rds.setData(config);
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		return rds;
	}

	@Override
	public ResultDataSet updateNoDisturb(boolean noDisturbEnable, String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		AccountConfig config = configPriDao.findOne(accId);
		if (config == null) {
			AccountConfig newConfig = new AccountConfig();
			newConfig.setAccId(accId);
			newConfig.setAddFirendsType(0);
			newConfig.setEnableNoticedOnLive(true);
			newConfig.setEnableNoDisturb(noDisturbEnable);
			configPriDao.save(newConfig);
			rds.setData(newConfig);
		} else {
			config.setEnableNoDisturb(noDisturbEnable);
			configPriDao.save(config);
			rds.setData(config);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountConfig(String accId){
		ResultDataSet rds = new ResultDataSet();
		AccountConfig accountConfig = configPriDao.findOne(accId);
		if (accountConfig == null) {
			accountConfig = new AccountConfig();
			accountConfig.setAccId(accId);
			accountConfig.setAddFirendsType(0);
			accountConfig.setEnableNoticedOnLive(true);
			accountConfig.setEnableNoDisturb(false);
			configPriDao.save(accountConfig);
		}
		rds.setData(accountConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet editFaceImage(String accId, String faceSmallUrl, String faceOrgUrl) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		account.setFaceSmallUrl(faceSmallUrl);
		account.setFaceOrgUrl(faceOrgUrl);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		account.setFaceUseInGame(true);
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(accId); if (hotAcc != null)
		 * { hotAcc.setFaceSmallUrl(faceSmallUrl);
		 * hotAcc.setFaceOrgUrl(faceOrgUrl);
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		taskUtil.performTaskOnSetFaceImage(accId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet changeOpenId(String accId, String changedAccId,String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		PrettyOpenId pOpenId = prettyOpenIdPriDao.findOne(changedAccId);
		if (pOpenId == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在该ID");
			return rds;
		}
		if (pOpenId.getUsed() != 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该ID已被使用");
			return rds;
		}
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (wallet.getiGold() < pOpenId.getPrice()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(DateUtils.getNowTime());
		moneyFlow.setIpAddress(clientIP);
		
		HelperFunctions.setMoneyFlowType(MoneyFlowType.ChangeOpenId, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		
		wallet.setiGold(wallet.getiGold() - pOpenId.getPrice());
		walletPriDao.save(wallet);
		
//		moneyFlow.setTargetIGold(wallet.getiGold());
		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);
		
		acc.setOpenId(pOpenId.getOpenId());
		acc.setVersion(VersionUtils.updateVersion(acc.getVersion()));
		accountPriDao.save(acc);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(accId); if (hotAcc != null)
		 * { hotAcc.setOpenId(pOpenId.getOpenId());
		 * hotAcc.setVersion(acc.getVersion()); hotAccountDao.save(hotAcc); }
		 */
		// ?此处需要记录消费
		return rds;
	}
}
