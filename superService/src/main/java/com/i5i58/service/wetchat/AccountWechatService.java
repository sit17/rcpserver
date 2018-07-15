package com.i5i58.service.wetchat;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformWechat;
import com.i5i58.data.account.Account;
import com.i5i58.data.wechat.AwardConfig;
import com.i5i58.data.wechat.AwardOpeRecord;
import com.i5i58.data.wechat.LotteryChance;
import com.i5i58.data.wechat.WechatAccount;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.wechat.AwardConfigPriDao;
import com.i5i58.primary.dao.wechat.AwardOpeRecordPriDao;
import com.i5i58.primary.dao.wechat.LotteryChancePriDao;
import com.i5i58.primary.dao.wechat.WechatAccountPriDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.wechat.AwardConfigSecDao;
import com.i5i58.secondary.dao.wechat.AwardOpeRecordSecDao;
import com.i5i58.secondary.dao.wechat.LotteryChanceSecDao;
import com.i5i58.secondary.dao.wechat.WechatAccountSecDao;
import com.i5i58.util.MyPageUtils;

@Service(protocol = "dubbo")
public class AccountWechatService implements IPlatformWechat {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	AccountPriDao accountPriDao;
	
	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	WechatAccountSecDao wechatAccountSecDao;
	
	@Autowired
	LotteryChancePriDao lotteryChancePriDao;
	
	@Autowired
	LotteryChanceSecDao lotteryChanceSecDao;
	
	@Autowired
	AwardConfigPriDao awardConfigPriDao;
	
	@Autowired
	AwardConfigSecDao awardConfigSecDao;
	
	@Autowired
	WechatAccountPriDao wechatAccountPriDao;
	
	@Autowired
	WechatAccountSecDao WechatAccountSecDao;
	
	@Autowired
	AwardOpeRecordPriDao awardOpeRecordPriDao;
	
	@Autowired
	AwardOpeRecordSecDao awardOpeRecordSecDao;
	@Override
	public ResultDataSet queryWechatAccount(int pageSize,int pageNum) {
		ResultDataSet rds=new ResultDataSet();
		Pageable pageable=new PageRequest(pageNum, pageSize, Direction.ASC,"bindDate");
		Page<WechatAccount> weChat=wechatAccountSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(weChat));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet setLotteryChance(String SuperAccId, String accId, int lotteryCount) {
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
		LotteryChance lotteryChance=lotteryChancePriDao.findOne(accId);
		if(lotteryChance == null){
			LotteryChance lottery = new LotteryChance();
			lottery.setAccId(accId);
			lottery.setLotteryCount(lotteryCount);
			lotteryChancePriDao.save(lottery);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}else{
			int count=lotteryChance.getLotteryCount()+lotteryCount;
			lotteryChance.setLotteryCount(count);
			lotteryChancePriDao.save(lotteryChance);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet deleteLotreryChance(String SuperAccId, String accId) {
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
		if(!lotteryChancePriDao.exists(accId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		lotteryChancePriDao.delete(accId);
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
	public ResultDataSet setAwardConfig(String SuperAccId,int id, long amount,int unit, String description, int rate,String url,boolean nullity) {
		ResultDataSet rds=new ResultDataSet();
		AwardConfig awardConfig;
		if(id == 0){
			awardConfig=new AwardConfig();
		}else{
			awardConfig=awardConfigPriDao.findOne(id);
			if (awardConfig == null){
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("当前奖品不存在");
				return rds;
			}
		}
		awardConfig.setAmount(amount);
		awardConfig.setUnit(unit);
		awardConfig.setDescription(description);
		awardConfig.setRate(rate);
		awardConfig.setNullity(nullity);
		awardConfig.setUrl(url);
		awardConfigPriDao.save(awardConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteAwardConfig(String SuperAccId, int id) {
		ResultDataSet rds=new ResultDataSet();
		if(!awardConfigPriDao.exists(id)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("奖品不存在");
			return rds;
		}
		awardConfigPriDao.delete(id);
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

	@Override
	public ResultDataSet getBindedWechatAccount(String param, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<WechatAccount> account = wechatAccountPriDao.findWechatAccountByParam(param, pageable);
		rds.setData(MyPageUtils.getMyPage(account));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
//	@Override
//	public ResultDataSet getBindedWechatAccount(String param,int pageSize,int pageNum) {
//		ResultDataSet rds=new ResultDataSet();
//		Pageable pageable=new PageRequest(pageNum, pageSize);
//		Page<Account> account=accountPriDao.findByParam(param, pageable);
//		for(Account acc:account){
//			List<WechatAccount> wAccount= wechatAccountPriDao.findByAccId(acc.getAccId());
//			if(wAccount==null||wAccount.isEmpty()){
//				rds.setCode(ResultCode.PARAM_INVALID.getCode());
//				rds.setMsg("用户未绑定微信");
//				return rds;
//			}
//			rds.setData(wAccount);
//			rds.setCode(ResultCode.SUCCESS.getCode());
//			return rds;
//		}
//		rds.setCode(ResultCode.PARAM_INVALID.getCode());
//		rds.setMsg("用户不存在");
//		return rds;
//	}
	
//	@Override
//	public ResultDataSet setAwardOpeRecord(String SuperAccId, int id,String accId,int awardId,long amount,int unit,long rewardDateTime,
//			 long deliveryDateTime) {
//		ResultDataSet rds=new ResultDataSet();
//		AwardOpeRecord awardOpeRecord;
//		if(id==0){
//			awardOpeRecord =new AwardOpeRecord();
//		}else{
//			awardOpeRecord=awardOpeRecordPriDao.findOne(id);
//			if(awardOpeRecord==null){
//				rds.setCode(ResultCode.PARAM_INVALID.getCode());
//				rds.setMsg("没有抽奖记录");
//				return rds;
//			}
//		}
//			awardOpeRecord.setAccId(accId);
//			awardOpeRecord.setAwardId(awardId);
//			awardOpeRecord.setAmount(amount);
//			awardOpeRecord.setUnit(unit);
//			awardOpeRecord.setRewardDateTime(rewardDateTime);
//			awardOpeRecord.setDeliveryDateTime(deliveryDateTime);
//			awardOpeRecordPriDao.save(awardOpeRecord);
//			rds.setCode(ResultCode.SUCCESS.getCode());
//			return rds;
//	}
	
//	@Override
//	public ResultDataSet deleteAwardOpeRecord(String SuperAccId,int id) {
//		ResultDataSet rds=new ResultDataSet();
//		if(!awardOpeRecordPriDao.exists(id)){
//			rds.setCode(ResultCode.PARAM_INVALID.getCode());
//			rds.setMsg("抽奖记录不存在");
//			return rds;
//		}		
//		awardOpeRecordPriDao.delete(id);
//		rds.setCode(ResultCode.SUCCESS.getCode());
//		return rds;
//	}
}
