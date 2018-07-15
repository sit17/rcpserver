package com.i5i58.service.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountWechat;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.constants.SQLResultData;
import com.i5i58.config.SqlserverConfig;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.wechat.AwardConfig;
import com.i5i58.data.wechat.AwardConfigType;
import com.i5i58.data.wechat.AwardOpeRecord;
import com.i5i58.data.wechat.LotteryChance;
import com.i5i58.data.wechat.WechatAccount;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.wechat.AwardConfigPriDao;
import com.i5i58.primary.dao.wechat.AwardOpeRecordPriDao;
import com.i5i58.primary.dao.wechat.LotteryChancePriDao;
import com.i5i58.primary.dao.wechat.WechatAccountPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.wechat.AwardConfigSecDao;
import com.i5i58.secondary.dao.wechat.AwardOpeRecordSecDao;
import com.i5i58.secondary.dao.wechat.LotteryChanceSecDao;
import com.i5i58.secondary.dao.wechat.WechatAccountSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.util.web.HttpUtils;

@Service(protocol = "dubbo")
public class AccountWechatService implements IAccountWechat {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	WechatAccountPriDao wechatAccountPriDao;

	@Autowired
	AccountPriDao accountPriDao;
	
	@Autowired
	AccountSecDao accountSecDao;
	
	@Autowired
	AwardConfigPriDao awardConfigPriDao;
	
	@Autowired
	AwardConfigSecDao awardConfigSecDao;
	
	@Autowired
	AwardOpeRecordPriDao awardOpeRecordPriDao;
	
	@Autowired
	LotteryChancePriDao lotteryChancePriDao;
	
	@Autowired
	LotteryChanceSecDao lotteryChanceSecDao;
	
	@Autowired
	WechatAccountSecDao wechatAccountSecDao;
	
	@Autowired
	AwardOpeRecordSecDao awardOpeRecordSecDao;
	
	@Autowired
	WalletPriDao walletPriDao;
	
	@Autowired
	SqlserverConfig sqlserverConfig;
	
	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Override
	public ResultDataSet bindWechatAccount(String accId, String openId) throws IOException {
		// TODO Auto-generated method stub
		ResultDataSet rds = new ResultDataSet();
		try {
			if (StringUtils.StringIsEmptyOrNull(accId) || StringUtils.StringIsEmptyOrNull(openId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("参数错误！");
				return rds;
			}
			WechatAccount account = wechatAccountPriDao.findByOpenIdAndAccId(openId, accId);
			if (account != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该账号已经绑定过此微信！");
				return rds;
			}
			WechatAccount newAccount = new WechatAccount();
			newAccount.setAccId(accId);
			newAccount.setOpenId(openId);
			newAccount.setSelected(false);
			newAccount.setBindDate(DateUtils.getNowTime());
			wechatAccountPriDao.save(newAccount);

			List<WechatAccount> AllWechat = wechatAccountPriDao.findByOpenId(openId);
			for (WechatAccount wa : AllWechat) {
				if (accId.equals(wa.getAccId())) {
					wa.setSelected(true);
				} else {
					wa.setSelected(false);
				}
				wechatAccountPriDao.save(wa);
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
	public ResultDataSet setCurWechatAccount(String accId, String openId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			if (StringUtils.StringIsEmptyOrNull(accId) || StringUtils.StringIsEmptyOrNull(openId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("参数错误！");
				return rds;
			}
			WechatAccount account = wechatAccountPriDao.findByOpenIdAndAccId(openId, accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("查无此账号！");
				return rds;
			}

			List<WechatAccount> AllWechat = wechatAccountPriDao.findByOpenId(openId);
			for (WechatAccount wa : AllWechat) {
				if (accId.equals(wa.getAccId())) {
					wa.setSelected(true);
				} else {
					wa.setSelected(false);
				}
				wechatAccountPriDao.save(wa);
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
	public ResultDataSet getAccIdByOpenId(String openId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			if (StringUtils.StringIsEmptyOrNull(openId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("参数错误！");
				return rds;
			}
			WechatAccount account = wechatAccountPriDao.findByOpenIdAndSelected(openId, true);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("查无此账号！");
				return rds;
			}

			ResponseData response = new ResponseData();
			response.put("accId", account.getAccId());
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());

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
	public ResultDataSet getAccIdByLiveOpenId(String liveOpenId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			if (StringUtils.StringIsEmptyOrNull(liveOpenId)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("参数错误！");
				return rds;
			}
			Account account = accountPriDao.findByOpenId(liveOpenId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("查无此账号！");
				return rds;
			}

			ResponseData response = new ResponseData();
			response.put("accId", account.getAccId());
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());

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
	public ResultDataSet lottery(String accId, String clientIp, String serialNum) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		LotteryChance lotteryChance = lotteryChancePriDao.findOne(accId);
		if (lotteryChance == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户没有抽奖机会");
			return rds;
		}
		int lc = lotteryChance.getLotteryCount();
		if (lc == 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户没有抽奖机会");
			return rds;
		}

		//获取奖品配置
		List<AwardConfig> award = (List<AwardConfig>) awardConfigPriDao.findAll();
		if (award == null || award.size() == 0) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("奖品不存在");
			return rds;
		}

		//随机奖品
		int randMax = 0;
		for (AwardConfig a : award) {
			randMax += a.getRate();
		}
		if (randMax <= 0) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("奖品配置异常");
			return rds;
		}
		Random rand = new Random();
		int r = rand.nextInt(randMax);

		int total = 0;
		AwardConfig result = null;
		for (AwardConfig a : award) {
			total += a.getRate();
			if (r < total) {
				result = a;
				break;
			}
		}
		if (result == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("");
			return rds;
		}

		//奖品发放&记录数据
		long nowTime = DateUtils.getNowTime();
		Wallet wallet = walletPriDao.findOne(accId);
		MoneyFlow mFlow = new MoneyFlow();
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		mFlow.setAccId(accId);
		mFlow.setDateTime(nowTime);
		mFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.WechatAward, mFlow);
		HelperFunctions.setMoneyFlowSource(wallet, mFlow);
		
		if (result.getUnit() == AwardConfigType.IGold.getValue()) {
			wallet.setiGold(wallet.getiGold() + result.getAmount());
			walletPriDao.save(wallet);
			HelperFunctions.setMoneyFlowTarget(wallet, mFlow);
			moneyFlowPriDao.save(mFlow);
		} else if (result.getUnit() == AwardConfigType.Diamond.getValue()) {
			wallet.setDiamond(wallet.getDiamond() + result.getAmount());
			walletPriDao.save(wallet);
			HelperFunctions.setMoneyFlowTarget(wallet, mFlow);
			moneyFlowPriDao.save(mFlow);
		} else if (result.getUnit() == AwardConfigType.GameGold.getValue()) {
			GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
			SQLResultData srd = dbHelper.getGameGold(accId, result.getAmount(), "微信抽奖",
					clientIp,serialNum);
			if (srd.getReutrnCode() != 0) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(srd.getDescribe());
				return rds;
			}
		} else if (result.getUnit() != AwardConfigType.NoAward.getValue()
				&& result.getUnit() != AwardConfigType.Again.getValue()){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("抽奖失败!");
			return rds;
		}

		AwardOpeRecord aor = new AwardOpeRecord();
		aor.setAccId(accId);
		aor.setRewardDateTime(nowTime);
		aor.setAwardId(result.getId());
		aor.setAmount(result.getAmount());
		aor.setUnit(result.getUnit());
		aor.setDeliveryDateTime(nowTime);
		awardOpeRecordPriDao.save(aor);

		lotteryChance.setLotteryCount(lc - 1);
		lotteryChancePriDao.save(lotteryChance);

		HashMap<String, Object> raward = new HashMap<>();
		raward.put("amount", result.getAmount());
		raward.put("unit", result.getUnit());
		raward.put("awardId", result.getId());
		rds.setData(raward);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryLotteryChance(String accId) {
		ResultDataSet rds=new ResultDataSet();
		Account account=accountPriDao.findOne(accId);
		if(account==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			 return rds;
		}
		WechatAccount wechatAccount=wechatAccountPriDao.findOne(accId);
		if(wechatAccount==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该用户没有绑定微信");
			return rds;
		}
		if(accId==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		LotteryChance lottery=lotteryChanceSecDao.findByAccId(accId);
		int count=lottery.getLotteryCount();
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAwardConfig(int pageSize, int pageNum) {
		ResultDataSet rds=new ResultDataSet();
		Sort sort=new Sort(Direction.ASC, "id");
	    Pageable pageable=new PageRequest(pageNum, pageSize, sort);
	    Page<AwardConfig> page=awardConfigSecDao.findAll(pageable);
	    rds.setData(MyPageUtils.getMyPage(page));
	    rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAwardOpeRecord(String accId) {
		ResultDataSet rds=new ResultDataSet();
		Account account=accountSecDao.findOne(accId);
		if(account==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		WechatAccount wechatAccount=wechatAccountSecDao.findOne(accId);
		if(wechatAccount==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该用户没有绑定微信");
			return rds;
		}
		if(accId==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		List<AwardOpeRecord> awardOpeRecord= awardOpeRecordSecDao.findByAccId(accId);
		rds.setData(awardOpeRecord);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
