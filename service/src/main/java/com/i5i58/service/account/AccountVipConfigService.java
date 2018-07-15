package com.i5i58.service.account;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountVipConfig;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.AccountVipConfig;
import com.i5i58.data.account.HotAccountVipConfig;
import com.i5i58.data.account.MountStore;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.channel.ChannelMount;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.MountStorePriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelMountPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.record.RecordConsumptionPriDao;
import com.i5i58.redis.all.HotAccountVipConfigDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.account.AccountVipConfigSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.MountPresentUtil;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class AccountVipConfigService implements IAccountVipConfig {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AccountVipConfigSecDao accountVipConfigSecDao;

	@Autowired
	HotAccountVipConfigDao hotAccountVipConfigDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	RecordConsumptionPriDao recordConsumptionPriDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	MountPresentUtil mountPresentUtil;

	@Autowired
	MountStorePriDao mountStorePriDao;

	@Autowired
	ChannelMountPriDao channelMountPriDao;
	
	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Override
	public ResultDataSet getAccountVipConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotAccountVipConfig> hotAccountVipConfig = (List<HotAccountVipConfig>) hotAccountVipConfigDao.findAll();
		if (hotAccountVipConfig == null || hotAccountVipConfig.isEmpty()) {
			List<AccountVipConfig> data = (List<AccountVipConfig>) accountVipConfigSecDao.findAll();
			for (AccountVipConfig config : data) {
				HotAccountVipConfig hotConfig = new HotAccountVipConfig();
				hotConfig.setId(config.getId());
				hotConfig.setLevel(config.getLevel());
				hotConfig.setMonth(config.getMonth());
				hotConfig.setPrice(config.getPrice());
				hotAccountVipConfigDao.save(hotConfig);
			}
			rds.setData(data);
		} else {
			rds.setData(hotAccountVipConfig);
		}
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet buyAccountVip(String accId, int level, String clientIP, int month) {
		ResultDataSet rds = new ResultDataSet();
//		String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
//		if (serverKey != null && !serverKey.isEmpty()){
//			Map<String, String> headers = new HashMap<>();
//			Map<String, String> params = new HashMap<>();
//			headers.put("accId", accId);
//			
//			params.put("level", new Integer(level).toString());
//			params.put("clientIP", clientIP);
//			params.put("month", new Integer(month).toString());
//			
//			rds = UniqueOperationCheck.httpPost(serverKey, "/account/buyAccountVip", headers, params);
//			return rds;
//		}
		long date = 0;
		long deadline = 0;
		try {
			date = DateUtils.getDate(new Date());
			Wallet wallet = walletPriDao.findByAccId(accId);
			if (wallet == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_WALLET.getCode());
				return rds;
			}
			HotAccountVipConfig config = hotAccountVipConfigDao.findByLevelAndMonth(level, month);
			if (config == null) {
				Iterable<AccountVipConfig> accountVipConfig = accountVipConfigSecDao.findAll();
				for (AccountVipConfig hgc : accountVipConfig) {
					HotAccountVipConfig hcgc = new HotAccountVipConfig();
					hcgc.setId(hgc.getId());
					hcgc.setLevel(hgc.getLevel());
					hcgc.setMonth(hgc.getMonth());
					hcgc.setPrice(hgc.getPrice());
					hotAccountVipConfigDao.save(hcgc);
				}
				config = hotAccountVipConfigDao.findByLevelAndMonth(level, month);
			}
			if (config == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_VIP.getCode());
				return rds;
			}
			if (config.getPrice() > wallet.getiGold()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
				return rds;
			}
			AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
			if (accountProperty == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("用戶屬性不存在");
				return rds;
			}
			if (accountProperty != null && level < accountProperty.getVip()) {
				if (accountProperty.getVipDeadline() > date) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("您已经购买了更高等级的VIP，无法开通该等级VIP");
					return rds;
				}
			}
			long dateTime = DateUtils.getNowTime();
			MoneyFlow moneyFlow = new MoneyFlow();
			moneyFlow.setAccId(accId);
			moneyFlow.setDateTime(dateTime);
			moneyFlow.setIpAddress(clientIP);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.BuyVip, moneyFlow);
			HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
			
			wallet.setiGold(wallet.getiGold()-config.getPrice());
			walletPriDao.save(wallet);

			HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
			moneyFlowPriDao.save(moneyFlow);
			
			// double price = month * config.getPrice();
			// int addDate = config.getMonth() * month;

			// MountStore mountStore =
			// mountStoreDao.findByAccIdAndMountsId(accId,
			// mountId);

			if (accountProperty.getVipDeadline() > date && accountProperty.getVip() == level) {
				deadline = DateUtils.AddMonth(accountProperty.getVipDeadline(), month);
			} else {
				deadline = DateUtils.AddMonth(date, month);
			}
			accountProperty.setVip(level);
			accountProperty.setVipDeadline(deadline);
			accountPropertyPriDao.save(accountProperty);
			
			List<MountStore> mountStoreList = mountStorePriDao.findByAccId(accId);
			for(MountStore m : mountStoreList){
				ChannelMount mount = channelMountPriDao.findOne(m.getMountsId());
				if(mount.isForVip()){
					if(level < mount.getLevel()){
						break;
					}
					m.setEndTime(deadline);
					mountStorePriDao.save(m);
				}
			}
			/*
			 * HotAccount hotAccounts = hotAccountDao.findOne(accId);
			 * hotAccounts.setVip(accountProperty.getVip());
			 * hotAccounts.setVipDeadLine(accountProperty.getVipDeadline());
			 * hotAccountDao.save(hotAccounts);
			 */

			mountPresentUtil.presentMountForBuyVip(accId, level, deadline);

			RecordConsumption record = new RecordConsumption();
			record.setId(StringUtils.createUUID());
			record.setAccId(accId);
			record.setChannelId("");
			record.setAmount(config.getPrice());
			record.setClientIp(clientIP);
			record.setGoodsNumber(month);
			record.setDate(DateUtils.getNowTime());
			record.setDeadline(deadline);
			record.setDescribe("");
			record.setGoodsId(String.valueOf(level));
			record.setGoodsType(GoodsType.BUY_VIP.getValue());
			recordConsumptionPriDao.save(record);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("server_ex");
			return rds;
		}
	}

	@Override
	@Transactional
	public ResultDataSet upgradeAccountVip(String accId, int level, String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		
//		String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
//		if (serverKey != null && !serverKey.isEmpty()){
//			Map<String, String> headers = new HashMap<>();
//			Map<String, String> params = new HashMap<>();
//			headers.put("accId", accId);
//			
//			params.put("level", new Integer(level).toString());
//			params.put("clientIP", clientIP);
//			
//			rds = UniqueOperationCheck.httpPost(serverKey, "/account/upgradeAccountVip", headers, params);
//			return rds;
//		}
		
		int month = 0;
		int oldLevel = 0;
		long price = 0;
		long oldPrice = 0;
		long newPrice = 0;

		
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}

		AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
		if (accountProperty != null && accountProperty.getVip() == 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("" + accountProperty.getVip());
			return rds;
		}
		if (accountProperty != null && level <= accountProperty.getVip()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您已经购买了更高等级的VIP，无法开通该等级VIP");
			return rds;
		}
		oldLevel = accountProperty.getVip();
		Sort sort = new Sort(Direction.fromString("desc"), "deadline");
		Pageable pageable = new PageRequest(0, 1, sort);

		Page<RecordConsumption> recordConsumption = recordConsumptionPriDao.findByAccIdAndGoodsType(accId,
				GoodsType.BUY_VIP.getValue(), pageable);
		if (recordConsumption != null && recordConsumption.getSize() != 0) {
			month = recordConsumption.getContent().get(0).getGoodsNumber();
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您没有购买过VIP");
			return rds;
		}
		HotAccountVipConfig config = hotAccountVipConfigDao.findByLevelAndMonth(level, month);
		HotAccountVipConfig oldConfig = hotAccountVipConfigDao.findByLevelAndMonth(oldLevel, month);
		if (config == null || oldConfig == null) {
			Iterable<AccountVipConfig> accountVipConfig = accountVipConfigSecDao.findAll();
			for (AccountVipConfig hgc : accountVipConfig) {
				HotAccountVipConfig hcgc = new HotAccountVipConfig();
				hcgc.setId(hgc.getId());
				hcgc.setLevel(hgc.getLevel());
				hcgc.setMonth(hgc.getMonth());
				hcgc.setPrice(hgc.getPrice());
				hotAccountVipConfigDao.save(hcgc);
			}
			config = hotAccountVipConfigDao.findByLevelAndMonth(level, month);
			oldConfig = hotAccountVipConfigDao.findByLevelAndMonth(oldLevel, month);
		}
		if (config == null || oldConfig == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_VIP.getCode());
			return rds;
		}
		newPrice = config.getPrice();
		oldPrice = oldConfig.getPrice();
		price = newPrice - oldPrice;
		if (price > wallet.getiGold()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		long dateTime = DateUtils.getNowTime();
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(dateTime);
		moneyFlow.setIpAddress(clientIP);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.UpgradeVip, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		
		wallet.setiGold(wallet.getiGold() - price);
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);
		
		accountProperty.setVip(level);
		accountPropertyPriDao.save(accountProperty);

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId("");
		record.setAmount(config.getPrice());
		record.setClientIp(clientIP);
		record.setDate(dateTime);
		record.setDeadline(accountProperty.getVipDeadline());
		record.setDescribe("");
		record.setGoodsId(String.valueOf(level));
		record.setGoodsType(GoodsType.BUY_VIP.getValue());
		record.setGoodsNumber(month);
		recordConsumptionPriDao.save(record);

		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

}
