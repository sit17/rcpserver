package com.i5i58.service.account;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAccount;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountAuth;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.anchor.AnchorAuth;
import com.i5i58.data.anchor.AuthType;
import com.i5i58.data.channel.ChGoodsType;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelRecord;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.pay.OnLineOrder;
import com.i5i58.data.pay.TotalPay;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.data.record.RecordPay;
import com.i5i58.data.superAdmin.SuperAdminAuth;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.record.RecordConsumptionPriDao;
import com.i5i58.primary.dao.record.RecordPayPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.AuthAnchorSecDao;
import com.i5i58.secondary.dao.account.AuthUserSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.channel.ChannelRecordSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.pay.OnLineOrderSecDao;
import com.i5i58.secondary.dao.record.MoneyFlowSecDao;
import com.i5i58.secondary.dao.record.RecordConsumptionSecDao;
import com.i5i58.secondary.dao.record.RecordPaySecDao;
import com.i5i58.secondary.dao.social.FollowInfoSecDao;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class AccountAdminSuperService implements IPlatformAccount {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	RecordPayPriDao recordPayPriDao;

	@Autowired
	RecordPaySecDao recordPaySecDao;

	@Autowired
	RecordConsumptionPriDao recordConsumptionPriDao;

	@Autowired
	RecordConsumptionSecDao recordConsumptionSecDao;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	FollowInfoSecDao followInfoSecDao;

	@Autowired
	OnLineOrderSecDao onLineOrderSecDao;

	@Autowired
	MoneyFlowSecDao moneyFlowSecDao;

	@Autowired
	AuthUserSecDao authUserSecDao;

	@Autowired
	AuthAnchorSecDao authAnchorSecDao;

	@Autowired
	ChannelRecordSecDao channelRecordSecDao;

	@Autowired
	EntityManager entityManager;

	@Autowired
	AccountUtils accountUtils;
	
	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Override
	public ResultDataSet queryAccountList(String param, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "registDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<Account> data = accountSecDao.findByParam(param, pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		return rds;
	}

	@Override
	public ResultDataSet getAccountInfo(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		AccountProperty accPty = accountPropertySecDao.findOne(accId);
		Wallet wallet = walletSecDao.findOne(accId);
		Channel channel = channelSecDao.findByOwnerId(accId);
		int fansCount = followInfoSecDao.countFansByAccId(accId);
		int followCount = followInfoSecDao.countFollowByAccId(accId);
		List<ChannelGroup> groups = channelGroupSecDao.findByOwnerId(accId);
		ResponseData res = new ResponseData();
		res.put("acc", acc);
		res.put("accPty", accPty);
		res.put("wallet", wallet);
		res.put("groups", groups);
		res.put("channel", channel);
		res.put("fansCount", fansCount);
		res.put("followCount", followCount);
		rds.setData(res);
		return rds;
	}

	@Override
	public ResultDataSet queryOnlineOrder(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "orderId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<OnLineOrder> onlineOrder = onLineOrderSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(onlineOrder));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryMoneyFlow(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "dateTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<MoneyFlow> moneyFlow = moneyFlowSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(moneyFlow));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountPay(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "createTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordPay> recordPay = recordPaySecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(recordPay));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountConsume(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "date");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<RecordConsumption> recordConsumptions = recordConsumptionSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(recordConsumptions));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet enableAccount(String superAccId, String accId, boolean nullity, boolean needKick) {
		ResultDataSet rds = new ResultDataSet();
		try {
			if (nullity == true) {
				YXResultSet ret = YunxinIM.blockAccount(accId, needKick);
				if (ret.getCode().equals("200")) {
					if (accountUtils.nullityAccount(accId)) {
						superAdminUtils.superAdminRecord(superAccId, "禁用/解封账号{accId:%s, nullity:%s, needKick:%s}",
								accId, nullity, needKick);
					}
					rds.setCode(ResultCode.SUCCESS.getCode());
				} else {
					rds.setMsg(ret.getError());
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
				}
			} else {
				YXResultSet ret = YunxinIM.unblockAccount(accId);
				if (ret.getCode().equals("200")) {
					Account acc = accountPriDao.findOne(accId);
					acc.setNullity(nullity);
					accountPriDao.save(acc);
					superAdminUtils.superAdminRecord(superAccId, "禁用/解封账号{accId:%s, nullity:%s, needKick:%s}", accId,
							nullity, needKick);
					rds.setCode(ResultCode.SUCCESS.getCode());
				} else {
					rds.setMsg(ret.getError());
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
				}
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("yunxin_error");
		}
		return rds;
	}

	@Override
	public ResultDataSet answerAppeal(String superAccId, String appealId, boolean agree, String reason) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ResultDataSet updateGiftTicket(String superAccId, String targetAccId, byte ope, long giftTickets, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		if (!superAdminUtils.verifyAuth(superAccId, SuperAdminAuth.SYSTEM_CONTROL_AUTH)) {
			rds.setMsg("没有权限");
			rds.setCode(ResultCode.AUTH.getCode());
			return rds;
		}
		Account account = accountPriDao.findOne(targetAccId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (giftTickets < 0) {
			rds.setMsg("参数不能为负数");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(targetAccId);
		moneyFlow.setDateTime(DateUtils.getNowTime());
		moneyFlow.setIpAddress(clientIp);
		
		Wallet wallet = walletPriDao.findByAccId(targetAccId);
		
		HelperFunctions.setMoneyFlowType(MoneyFlowType.SuperUpdateGiftTicket, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		
		long orgGiftTickets = wallet.getGiftTicket();
		long newGiftTickets = 0;
		switch (ope) {
		case 0:
			newGiftTickets = orgGiftTickets + giftTickets;
			break;
		case 1:
			newGiftTickets = orgGiftTickets - giftTickets;
			break;
		case 2:
			newGiftTickets = giftTickets;
			break;
		default: {
			rds.setMsg("操作类型未知");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		}
		if (newGiftTickets < 0) {
			newGiftTickets = 0;
		}
		wallet.setGiftTicket(newGiftTickets);
		walletPriDao.save(wallet);
		superAdminUtils.superAdminRecord(superAccId, "更新礼物券{targetAccId:%s,ope:%s, giftTickets:%s}", targetAccId, ope,
				giftTickets);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet updateIGold(String superAccId, String targetAccId, byte ope, long iGolds) {
		ResultDataSet rds = new ResultDataSet();
		if (!superAdminUtils.verifyAuth(superAccId, SuperAdminAuth.SYSTEM_CONTROL_AUTH)) {
			rds.setMsg("没有权限");
			rds.setCode(ResultCode.AUTH.getCode());
			return rds;
		}
		Account account = accountPriDao.findOne(targetAccId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (iGolds < 0) {
			rds.setMsg("参数不能为负数");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(targetAccId);
		long orgIGold = wallet.getiGold();
		long newIGold = 0;
		switch (ope) {
		case 0:
			newIGold = orgIGold + iGolds;
			break;
		case 1:
			newIGold = orgIGold - iGolds;
			break;
		case 2:
			newIGold = iGolds;
			break;
		default: {
			rds.setMsg("操作类型未知");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		}
		if (newIGold < 0) {
			newIGold = 0;
		}
		wallet.setiGold(newIGold);
		walletPriDao.save(wallet);
		superAdminUtils.superAdminRecord(superAccId, "更新虎币{targetAccId:%s,ope:%s, iGolds:%s}", targetAccId, ope,
				iGolds);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountAuthInfo(String adminAccId, String targetAccId) {
		ResultDataSet rds = new ResultDataSet();
		String certificateId = null;
		String realName = null;
		AnchorAuth anchorAuth = authAnchorSecDao.findByAccIdAndAuthed(targetAccId, AuthType.Success.getValue());
		if (anchorAuth != null) {
			certificateId = anchorAuth.getCertificateId();
			realName = anchorAuth.getRealName();
		}
		if (StringUtils.StringIsEmptyOrNull(certificateId) || StringUtils.StringIsEmptyOrNull(realName)) {
			AccountAuth accountAuth = null;
			accountAuth = authUserSecDao.findByAccId(targetAccId);
			if (accountAuth != null) {
				certificateId = accountAuth.getCertificateId();
				realName = accountAuth.getRealName();
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("用户没有实名认证");
				return rds;
			}
		}

		ResponseData rp = new ResponseData();
		rp.put("certificateId", certificateId);
		rp.put("realName", realName);

		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAccountGiftRecord(String accId, int pageSize, int pageNum) {
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
	public ResultDataSet queryAccountWallet(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Wallet wallet = walletSecDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
		}
		rds.setData(wallet);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet appLogRequirement(String admin, String accId, String bucketName) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		YXResultSet ret = null;
		try {
			String objectKey = String.format("%s%03d%d%06d.log", DateUtils.getNowTimeString(),
					DateUtils.getNowTime() % 1000, accId.hashCode(), (int) (Math.random() * 1000000));
			ResponseData rp = new ResponseData();
			ResponseData data = new ResponseData();
			data.put("bucketName", bucketName);
			data.put("objectKey:", objectKey);
			rp.put("cmd", "getLog");
			rp.put("data", data);
			ret = YunxinIM.sendAttachMessage(admin, "0", accId, new JsonUtils().toJson(rp), "", "", "", "2", "");
			if (ret.getCode().equals("200")) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setData(rp);
			} else {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("云信发送失败 " + ret.getCode() + ":" + ret.getError());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("云信操作异常");
		}
		return rds;
	}

	@Override
	public ResultDataSet queryAccountPayList(int status, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		try {
//			Sort sort = new Sort(Direction.fromString("desc"), "orderAmount");
			Pageable pageable = new PageRequest(pageNum, pageSize);
			Page<TotalPay> data = onLineOrderSecDao.findOrderByGroupAndParam(status, pageable);
			rds.setData(MyPageUtils.getMyPage(data));
		} catch (Exception e) {
			logger.error(e);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
