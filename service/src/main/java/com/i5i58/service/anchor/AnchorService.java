package com.i5i58.service.anchor;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import com.i5i58.apis.anchor.IAnchor;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.anchor.AnchorAuth;
import com.i5i58.data.anchor.AnchorPushRecord;
import com.i5i58.data.anchor.AuthType;
import com.i5i58.data.anchor.CommissionByGuardConfig;
import com.i5i58.data.anchor.WithdrawCash;
import com.i5i58.data.anchor.WithdrawCashStatus;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.ChannelPushDevice;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.config.PlatformConfig;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.ContractRequestDirection;
import com.i5i58.data.group.ForceCancelContract;
import com.i5i58.data.group.ForceCancelContractStatus;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AuthAnchorPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.anchor.CommissionByGuardConfigPriDao;
import com.i5i58.primary.dao.anchor.WithdrawCashPriDao;
import com.i5i58.primary.dao.channel.ChannelPushDevicePriDao;
import com.i5i58.primary.dao.channel.ChannelStatus;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.ForceCancelContractPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.AuthAnchorSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.anchor.AnchorPushRecordSecDao;
import com.i5i58.secondary.dao.anchor.CommissionByGuardConfigSecDao;
import com.i5i58.secondary.dao.anchor.WithdrawCashSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.group.ForceCancelContractSecDao;
import com.i5i58.secondary.dao.record.MoneyFlowSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.ContractOpeRecordUtil;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

@Service(protocol = "dubbo")
public class AnchorService implements IAnchor {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	WithdrawCashPriDao withdrawCashPriDao;

	@Autowired
	WithdrawCashSecDao withdrawCashSecDao;

	@Autowired
	CommissionByGuardConfigPriDao commissionByGuardConfigPriDao;

	@Autowired
	CommissionByGuardConfigSecDao commissionByGuardConfigSecDao;

	@Autowired
	AuthAnchorPriDao authAnchorPriDao;

	@Autowired
	AuthAnchorSecDao authAnchorSecDao;

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Autowired
	MoneyFlowSecDao moneyFlowSecDao;

	@Autowired
	AnchorPushRecordSecDao anchorPushRecordSecDao;

	@Autowired
	PlatformConfigPriDao platformConfigPriDao;

	@Autowired
	AnchorUtils anchorUtils;

	@Autowired
	ContractOpeRecordUtil contractOpeRecordUtil;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	ChannelPushDevicePriDao channelPushDevicePriDao;
	
	@Autowired
	ForceCancelContractPriDao forceCancelContractPriDao;
	
	@Autowired
	ForceCancelContractSecDao forceCancelContractSecDao;

	@Override
	public ResultDataSet queryContractByAccId(String accId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "startDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorContract>  page= anchorContractSecDao.findByAccId(accId, pageable);
		List<AnchorContract> contract=page.getContent();
		List<ResponseData> rebuilds=new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for(AnchorContract con:contract){
				ResponseData tmp=new ResponseData();
				tmp.put("ctId",con.getCtId());
				tmp.put("gId",con.getgId());
				tmp.put("accId", con.getAccId());
				tmp.put("groupRate", con.getGroupRate());
				tmp.put("startDate", con.getStartDate());
				tmp.put("endDate", con.getEndDate());
				tmp.put("settleMode",con.getSettleMode());
				tmp.put("createTime",con.getCreateTime());
				tmp.put("status",con.getStatus());
				tmp.put("direction",con.getDirection());
				tmp.put("cancelDirection",con.getCancelDirection());
				tmp.put("hide",con.getHide());
				
			ChannelGroup channelGroup=channelGroupSecDao.findByGId(con.getgId());
			if(channelGroup!=null){
				tmp.put("groupName",channelGroup.getName());
			}
			rebuilds.add(tmp); 
		}
		result.put("content", rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet responseContract(String ctId, boolean agree) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if (contract == null || contract.getStatus() != AnchorContractStatus.REQUESTED.getValue()
				|| contract.getDirection() != 1) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约或已处理");
			return rds;
		}
		AnchorContract anchorContract;
		anchorContract = anchorUtils.getAnchorContract(contract.getAccId());

		if (anchorContract != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您已签约公会");
			return rds;
		}
		if (agree) {
			contract.setStatus(AnchorContractStatus.AGREED.getValue());
			contract.setStartDate(DateUtils.getNowTime());
			contractOpeRecordUtil.agreeSign(contract.getAccId(), contract.getgId());
		} else {
			contract.setStatus(AnchorContractStatus.REJECTED.getValue());
			contract.setStartDate(DateUtils.getNowTime());
			contractOpeRecordUtil.refuseSign(contract.getAccId(), contract.getgId());
		}
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet requestContract(String accId, String gId, int groupRate, long endDate, int settleMode)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
		if (channelGroup == null || !StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}
		AnchorContract ct;
		ct = anchorUtils.getAnchorContract(accId);

		if (ct != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您已签约公会");
			return rds;
		}
		ct = anchorContractPriDao.findByAccIdAndStatusAndEndDateGreaterThan(accId,
					AnchorContractStatus.REQUESTED.getValue(), DateUtils.getNowTime());

		if (ct != null && ct.getgId().equals(gId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("已经提交过申请，正在等待公会同意");
			return rds;
		}
		AnchorContract contract = new AnchorContract();
		contract.setCtId(StringUtils.createUUID());
		contract.setAccId(accId);
		contract.setgId(gId);
		contract.setCreateTime(DateUtils.getNowTime());
		contract.setEndDate(endDate);
		contract.setGroupRate(groupRate);
		contract.setSettleMode(settleMode);
		contract.setStartDate(DateUtils.getNowTime());
		contract.setStatus(AnchorContractStatus.REQUESTED.getValue());
		contract.setDirection(ContractRequestDirection.Anchor.getValue());
		anchorContractPriDao.save(contract);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyPush(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (!acc.isAnchor()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还不是主播");
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findByOwnerId(accId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还没有频道");
			return rds;
		}
		if (hotChannel.getStatus() == ChannelStatus.Nullity.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的频道已被禁用");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(hotChannel.getClubName()) // 没有粉丝团名称
				|| StringUtils.StringIsEmptyOrNull(hotChannel.getChannelName()) // 没有频道名称
				|| hotChannel.getChannelName().equals(hotChannel.getChannelId()) // 频道名称与频道ID一样
				|| StringUtils.StringIsEmptyOrNull(hotChannel.getChannelNotice()) // 没有频道公告
				|| hotChannel.getType() == 0 // 没有频道类型
				|| StringUtils.StringIsEmptyOrNull(acc.getStageName()) // 没有主播艺名
				|| StringUtils.StringIsEmptyOrNull(acc.getFaceSmallUrl()) // 没有设置个人头像
				|| StringUtils.StringIsEmptyOrNull(acc.getLocation()) // 没有设置地理位置
				|| StringUtils.StringIsEmptyOrNull(acc.getNickName()) // 没有个人名称
				|| acc.getNickName().equals(acc.getOpenId()) // 个人名称与账号ID一样
				|| acc.getBirthDate() == 0) { // 没有填写生日
			rds.setMsg("请前往 我的->设置->主播设置，完善您的主播信息！");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("cId", hotChannel.getId());
		response.put("rId", hotChannel.getYunXinRId());
		response.put("pushUrl", hotChannel.getPushUrl());
		response.put("coverUrl", hotChannel.getCoverUrl());
		response.put("title", hotChannel.getTitle());
		PlatformConfig pushConfig = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_QUALITY);
		response.put("pushQuality", pushConfig.getcValue());
		PlatformConfig pushConfig2 = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_MIX_MODE);
		response.put("pushMode", pushConfig2.getcValue());
		PlatformConfig pushConfig3 = platformConfigPriDao.findOne(Constant.PUSH_VIDEO_LOGO_MARK);
		response.put("pushMark", pushConfig3.getcValue());
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyPushMobile(String accId, int device, String serialNum, String model, String osVersion)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (!acc.isAnchor()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还不是主播,请前往 我的->设置->主播认证,完善您的主播信息！");
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findByOwnerId(accId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您还没有频道");
			return rds;
		}
		if (hotChannel.getStatus() == ChannelStatus.Nullity.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的频道已被禁用");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(hotChannel.getClubName()) // 没有粉丝团名称
				|| StringUtils.StringIsEmptyOrNull(hotChannel.getChannelName()) // 没有频道名称
				|| hotChannel.getChannelName().equals(hotChannel.getChannelId()) // 频道名称与频道ID一样
				|| StringUtils.StringIsEmptyOrNull(hotChannel.getChannelNotice()) // 没有频道公告
				|| hotChannel.getType() == 0 // 没有频道类型
				|| StringUtils.StringIsEmptyOrNull(acc.getStageName()) // 没有主播艺名
				|| StringUtils.StringIsEmptyOrNull(acc.getFaceSmallUrl()) // 没有设置个人头像
				|| StringUtils.StringIsEmptyOrNull(acc.getLocation()) // 没有设置地理位置
				|| StringUtils.StringIsEmptyOrNull(acc.getNickName()) // 没有个人名称
				|| acc.getNickName().equals(acc.getOpenId()) // 个人名称与账号ID一样
				|| acc.getBirthDate() == 0) { // 没有填写生日
			rds.setMsg("请前往 我的->设置->主播设置，完善您的主播信息！");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (model.contains("iPhone")) {
			String[] arr = osVersion.split("\\.");
			int ver = Integer.parseInt(arr[0]);
			if (ver < 10) {
				rds.setMsg("主播系统版本过低，请升级系统才能开播哟~");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("cId", hotChannel.getId());
		response.put("rId", hotChannel.getYunXinRId());
		response.put("pushUrl", hotChannel.getPushUrl());
		response.put("coverUrl", hotChannel.getCoverUrl());
		response.put("title", hotChannel.getTitle());

		ChannelPushDevice channelPushDevice = channelPushDevicePriDao.findByAccIdAndDeviceAndSerialNumAndModel(accId,
				device, serialNum, model);
		if (channelPushDevice == null) {
			ChannelPushDevice defaultChannelPushDevice = channelPushDevicePriDao.findByAccIdAndDevice("defaultPush",
					device);
			if (defaultChannelPushDevice == null) {
				logger.error("当前设备无默认推流配置, device=" + device);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("当前设备无默认配置");
				return rds;
			}
			ChannelPushDevice newChannelPushDevice = new ChannelPushDevice();
			newChannelPushDevice.setAccId(accId);
			newChannelPushDevice.setDevice(device);
			newChannelPushDevice.setModel(model);
			newChannelPushDevice.setSerialNum(serialNum);
			newChannelPushDevice.setOsVersion(osVersion);
			newChannelPushDevice.setPushMode(defaultChannelPushDevice.getPushMode());
			newChannelPushDevice.setPushMark(defaultChannelPushDevice.getPushMark());
			newChannelPushDevice.setPushQuality(defaultChannelPushDevice.getPushQuality());
			newChannelPushDevice.setPushBit(defaultChannelPushDevice.getPushBit());
			newChannelPushDevice.setPushFPS(defaultChannelPushDevice.getPushFPS());
			newChannelPushDevice.setEnableHardEncode(defaultChannelPushDevice.isEnableHardEncode());
			channelPushDevicePriDao.save(newChannelPushDevice);

			response.put("pushMode", newChannelPushDevice.getPushMode());
			response.put("pushMark", newChannelPushDevice.getPushMark());
			response.put("pushQuality", newChannelPushDevice.getPushQuality());
			response.put("pushBit", newChannelPushDevice.getPushBit());
			response.put("pushFPS", newChannelPushDevice.getPushFPS());
			response.put("pushHard", newChannelPushDevice.isEnableHardEncode());
		} else {
			response.put("pushMode", channelPushDevice.getPushMode());
			response.put("pushMark", channelPushDevice.getPushMark());
			response.put("pushQuality", channelPushDevice.getPushQuality());
			response.put("pushBit", channelPushDevice.getPushBit());
			response.put("pushFPS", channelPushDevice.getPushFPS());
			response.put("pushHard", channelPushDevice.isEnableHardEncode());
		}
		// PlatformConfig pushConfig =
		// platformConfigPriDao.findOne(Constant.PUSH_VIDEO_QUALITY);
		// response.put("pushQuality", pushConfig.getcValue());
		// PlatformConfig pushConfig2 =
		// platformConfigPriDao.findOne(Constant.PUSH_VIDEO_MIX_MODE);
		// response.put("pushMode", pushConfig2.getcValue());
		// PlatformConfig pushConfig3 =
		// platformConfigPriDao.findOne(Constant.PUSH_VIDEO_LOGO_MARK);
		// response.put("pushMark", pushConfig3.getcValue());
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet withdrawCash(String accId, long amount, String clientIp) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (amount < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("提现金额不能为负");
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		if (wallet.getCommission() < amount * 100) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的俸禄不够");
			return rds;
		}

		WithdrawCash withdrawCash = new WithdrawCash();
		long time = DateUtils.getNowTime();

		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(time);
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.CommissionToCash, moneyFlow);

		withdrawCash.setId(DateUtils.getTimeString(time) + time);
		withdrawCash.setAccId(accId);
		withdrawCash.setAmount(amount);
		withdrawCash.setCollectTime(time);
		withdrawCash.setClientIp(clientIp);
		withdrawCash.setStatus(WithdrawCashStatus.REQUEST.getValue());
		withdrawCashPriDao.save(withdrawCash);
		wallet.setCommission(wallet.getCommission() - amount * 100);
		walletPriDao.save(wallet);
		/*
		 * HotWallet hotWallet = hotWalletDao.findOne(accId); if (hotWallet !=
		 * null) { hotWallet.setCommission(wallet.getCommission());
		 * hotWalletDao.save(hotWallet); }
		 */

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet commissionExchangeToDiamond(String accId, long diamond,String clientIP) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (diamond < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("兑换值不能为负");
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		if (diamond <= 0) {
			rds.setMsg("钻石的数量为正数");
			return rds;
		}
		if (wallet.getCommission() < diamond) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您的俸禄不够");
			return rds;
		}

		MoneyFlow money = new MoneyFlow();
		money.setAccId(accId);
		money.setSourceCommission(wallet.getCommission());
		money.setDateTime(DateUtils.getNowTime());
		money.setIpAddress(clientIP);
		/*
		 * HotWallet hotWallet = hotWalletDao.findOne(accId); if (hotWallet !=
		 * null) { hotWallet.setCommission(wallet.getCommission() - diamond);
		 * hotWallet.setDiamond(wallet.getDiamond() + diamond);
		 * hotWalletDao.save(hotWallet); }
		 */
		wallet.setCommission(wallet.getCommission() - diamond);
		wallet.setDiamond(wallet.getDiamond() + diamond);
		walletPriDao.save(wallet);

		money.setTargetCommission(wallet.getCommission());
		money.setType(MoneyFlowType.CommissionToDiamond.getValue());
		money.setDescription(MoneyFlowType.CommissionToDiamond.getDesc());
		moneyFlowPriDao.save(money);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryWithdrawCash(String accId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "collectTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<WithdrawCash> withdrawCashs = withdrawCashSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(withdrawCashs));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getCommissionInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData rp = new ResponseData();
		Account acc = accountSecDao.findOne(accId);
		if (acc == null) {
			rds.setData(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!acc.isAnchor()) {
			rds.setData("不是主播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Wallet wallet = walletSecDao.findOne(accId);
		if (wallet == null) {
			rds.setData(ServerCode.NO_WALLET.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rp.put("commission", wallet.getCommission());
		AnchorContract ct;
		ct = anchorUtils.getAnchorContract(accId);
		if (ct != null) {
			rp.put("groupRate", ct.getGroupRate());
		}
		rds.setData(rp);
		return rds;
	}

	@Override
	public ResultDataSet getGuardCommissionConfigAll() throws IOException {
		ResultDataSet rds = new ResultDataSet();

		List<CommissionByGuardConfig> data = (List<CommissionByGuardConfig>) commissionByGuardConfigSecDao.findAll();
		rds.setData(data);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAnchorBankInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth anchor = authAnchorSecDao.findByAccIdAndAuthed(accId, AuthType.Success.getValue());
		if (anchor == null) {
			rds.setCode(ResultCode.AUTH.getCode());
			rds.setMsg("您尚未认证主播");
			return rds;
		}
		String bankCarNum = anchor.getBankCardNum();
		if (bankCarNum == null) {
			rds.setMsg("该主播未绑定银行卡");
			return rds;
		}
		ResponseData responseData = new ResponseData();
		String bankCarNum2 = StringUtils.addMask(bankCarNum, '*', 7, 4);
		responseData.put("bankCarNum", bankCarNum2);
		responseData.put("realName", anchor.getRealName());
		responseData.put("bankName", anchor.getBankName());
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getCommissionExchangeDetail(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData responseData = new ResponseData();
		// Page<MoneyFlow> money = moneyFlowSecDao.findByAccId(accId, pageable);
		List<MoneyFlow> cash = moneyFlowSecDao.findByAccIdAndType(accId, MoneyFlowType.CommissionToCash.getValue());
		List<MoneyFlow> diamon = moneyFlowSecDao.findByAccIdAndType(accId,
				MoneyFlowType.CommissionToDiamond.getValue());
		List<MoneyFlow> gameGold = moneyFlowSecDao.findByAccIdAndType(accId,
				MoneyFlowType.CommissionToGameGold.getValue());
		List<MoneyFlow> result = new ArrayList<>();
		result.addAll(cash);
		result.addAll(diamon);
		result.addAll(gameGold);
		Collections.sort(result, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				MoneyFlow stu1 = (MoneyFlow) o1;
				MoneyFlow stu2 = (MoneyFlow) o2;
				if (stu1.getDateTime() > stu2.getDateTime()) {
					return -1;
				} else if (stu1.getDateTime() == stu2.getDateTime()) {
					return 0;
				} else {
					return 1;
				}
			}
		});

		responseData.put("content", result);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getPushRecord(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth anchor = authAnchorSecDao.findByAccIdAndAuthed(accId, AuthType.Success.getValue());
		if (anchor == null) {
			rds.setCode(ResultCode.AUTH.getCode());
			rds.setMsg("您尚未认证主播");
			return rds;
		}

		if (toTime < fromTime) {
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}

		if (toTime - fromTime > DateUtils.monthMilliSecond * 2) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间间隔过长");
			return rds;
		}

		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		rds.setData(anchorPushRecords);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet calcActiveTime(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth anchor = authAnchorSecDao.findByAccIdAndAuthed(accId, AuthType.Success.getValue());
		if (anchor == null) {
			rds.setCode(ResultCode.AUTH.getCode());
			rds.setMsg("您尚未认证主播");
			return rds;
		}

		if (toTime < fromTime) {
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}

		if (toTime - fromTime > DateUtils.monthMilliSecond * 2) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("统计时间间隔过长");
			return rds;
		}

		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		long totalTime = 0;
		if (anchorPushRecords != null) {
			for (AnchorPushRecord record : anchorPushRecords) {
				if (record.getCloseTime() == 0) {
					continue;
				}
				if (record.getOpenTime() > record.getCloseTime()) {
					continue;
				}
				totalTime += record.getCloseTime() - record.getOpenTime();
			}
		}
		ResponseData rp = new ResponseData();
		rp.put("activeSeconds", totalTime / 1000);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryGuardByTime(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth anchor = authAnchorSecDao.findByAccIdAndAuthed(accId, AuthType.Success.getValue());
		if (anchor == null) {
			rds.setCode(ResultCode.AUTH.getCode());
			rds.setMsg("您尚未认证主播");
			return rds;
		}
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您尚未认绑定频道");
			return rds;
		}

		if (toTime < fromTime) {
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}

		if (toTime - fromTime > DateUtils.monthMilliSecond * 2) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("统计时间间隔过长");
			return rds;
		}
		List<ChannelGuard> guards = channelGuardSecDao.findByCIdAndBuyTime(channel.getcId(), fromTime, toTime);

		List<ResponseData> rebuilds = new ArrayList<>();
		for (ChannelGuard cg : guards) {
			ResponseData tmp = new ResponseData();
			tmp.put("accId", cg.getAccId());
			tmp.put("buyTime", cg.getBuyTime());
			tmp.put("cId", cg.getcId());
			tmp.put("startLine", cg.getStartLine());
			tmp.put("deadLine", cg.getDeadLine());
			tmp.put("guardLevel", cg.getGuardLevel());
			if (cg.getMountsId() > 0) {
				tmp.put("mountsId", cg.getMountsId());
				tmp.put("mountsName", cg.getMountsName());
			}
			Account account = accountSecDao.findOne(cg.getAccId());
			if (account != null) {// 用户名称
				tmp.put("accName", account.getNickName());
			}
			Channel ch = channelSecDao.findByCId(cg.getcId());
			if (ch != null) {// 频道名称
				tmp.put("chName", ch.getChannelName());
			}
			rebuilds.add(tmp);
		}
		rds.setData(rebuilds);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryMyTopGroup(String anchorId) {
		ResultDataSet rds = new ResultDataSet();
		
		Account account = accountSecDao.findOne(anchorId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		if (!account.isAnchor()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不是主播");
			return rds;
		}
		
		AnchorContract anchorContract = anchorUtils.getAnchorContract(anchorId);
		if (anchorContract == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播未签约");
			return rds;
		}
		
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(anchorContract.getgId());
		if (channelGroup == null){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("公会信息不存在");
			return rds;
		}
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(channelGroup);
		return rds;
	}

	@Override
	public ResultDataSet forceCancelContract(String accId, String ctId) {
		ResultDataSet rds=new ResultDataSet();
		Account account=accountSecDao.findOne(accId);
		if(account==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		if(!account.isAnchor()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不是主播");
			return rds;
		}
		
		List<ForceCancelContract> forceCancelContracts = forceCancelContractSecDao.findByCtIdAndStatus(ctId, 
				ForceCancelContractStatus.REQUESTED.getValue());
		if (forceCancelContracts != null && forceCancelContracts.size() > 0){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("正在处理中");
			return rds;
		}
		
		AnchorContract contract=anchorContractPriDao.findOne(ctId);
		if(contract==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("合约不存在");
			return rds;
		}
		if(contract.getStatus()!=AnchorContractStatus.AGREED.getValue()
				&&contract.getStatus()!=AnchorContractStatus.REQUEST_CANCEL.getValue()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("合约不可取消");
			return rds;
		}
		try{
		ForceCancelContract fcc=new ForceCancelContract();
		fcc.setAccId(accId);
		fcc.setCtId(contract.getCtId());
		fcc.setStatus(ForceCancelContractStatus.REQUESTED.getValue());
		fcc.setRequestedDateTime(DateUtils.getNowDate());
		fcc.setResponsedDateTime(0);
		fcc.setCancelDirection(0);
		forceCancelContractPriDao.save(fcc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
