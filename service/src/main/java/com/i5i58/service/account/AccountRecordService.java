package com.i5i58.service.account;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.account.IAccountRecord;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.ChGoodsType;
import com.i5i58.data.channel.ChannelRecord;
import com.i5i58.data.netBar.NetBarAccount;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.data.record.RecordPay;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.netBar.NetBarAccountPriDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelRecordSecDao;
import com.i5i58.secondary.dao.record.RecordConsumptionSecDao;
import com.i5i58.secondary.dao.record.RecordPaySecDao;
import com.i5i58.util.MyPageUtils;

@Service(protocol = "dubbo")
public class AccountRecordService implements IAccountRecord {

	@Autowired
	ChannelRecordSecDao channelRecordSecDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	RecordConsumptionSecDao recordConsumptionSecDao;

	@Autowired
	RecordPaySecDao recordPaySecDao;

	@Autowired
	AccountPriDao accountPriDao;
	
	@Autowired
	NetBarAccountPriDao netBarAccountPriDao;
	
	@Override
	public ResultDataSet queryPayRecord(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<RecordPay> payRecords = recordPaySecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(payRecords));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGiftRecord(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Sort.Direction.DESC, "collectDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelRecord> records = channelRecordSecDao.findByAccIdAndGoodsType(accId,
				ChGoodsType.CHANNEL_GIFT.getValue(), pageable);
		rds.setData(MyPageUtils.getMyPage(records));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryConsumptionRecord(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "date");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordConsumption> recordConsumptions = recordConsumptionSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(recordConsumptions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryVipRecord(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Sort sort = new Sort(Sort.Direction.ASC, "date");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordConsumption> consumptions = recordConsumptionSecDao.findByAccIdAndGoodsType(accId,
				GoodsType.BUY_VIP.getValue(), pageable);

		rds.setData(MyPageUtils.getMyPage(consumptions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGuardRecord(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Sort sort = new Sort(Sort.Direction.ASC, "date");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordConsumption> consumptions = recordConsumptionSecDao.findByAccIdAndGoodsType(accId,
				GoodsType.BUY_GUARD.getValue(), pageable);

		rds.setData(MyPageUtils.getMyPage(consumptions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryFansClubRecord(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Sort sort = new Sort(Sort.Direction.ASC, "date");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordConsumption> consumptions = recordConsumptionSecDao.findByAccIdAndGoodsType(accId,
				GoodsType.BUY_FANSCLUBS.getValue(), pageable);

		rds.setData(MyPageUtils.getMyPage(consumptions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryRegisterListByOwnerId(String accId, long startTime, long endTime) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		
		NetBarAccount nbAcc = netBarAccountPriDao.findByNullityAndOwnerId(false, accId);
		if (nbAcc == null) {
			rds.setMsg("没有这个账号！");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		List<Account> acc = accountPriDao.findByRegistIpAndRegistDateBetween(nbAcc.getNetIp(), startTime, endTime);
		if (acc != null && acc.size() > 0) {
			rds.setData(acc);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有注册用户");
		}
		return rds;
	}
}
