package com.i5i58.service.anchor;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformAnchorAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.anchor.AnchorAuth;
import com.i5i58.data.anchor.AnchorPushRecord;
import com.i5i58.data.anchor.AuthType;
import com.i5i58.data.anchor.CommissionByGuardConfig;
import com.i5i58.data.anchor.WithdrawCash;
import com.i5i58.data.anchor.WithdrawCashStatus;
import com.i5i58.data.anchor.WithdrawProcessRecord;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.ContractRequestDirection;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AuthAnchorPriDao;
import com.i5i58.primary.dao.anchor.WithdrawCashPriDao;
import com.i5i58.primary.dao.anchor.WithdrawProcessRecordPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.AuthAnchorSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.anchor.AnchorPushRecordSecDao;
import com.i5i58.secondary.dao.anchor.CommissionByGuardConfigSecDao;
import com.i5i58.secondary.dao.anchor.WithdrawCashSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;
import com.i5i58.secondary.dao.record.RecordConsumptionSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelOpeRecordUtil;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;

@Service(protocol = "dubbo")
public class AnchorAdminSuperService implements IPlatformAnchorAdmin {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	AuthAnchorPriDao authAnchorPriDao;

	@Autowired
	AuthAnchorSecDao authAnchorSecDao;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	AnchorPushRecordSecDao anchorPushRecordSecDao;
	
	@Autowired
	AnchorUtils	anchorUtils;
	
	@Autowired
	WalletSecDao walletSecDao;
	
	@Autowired
	WithdrawCashPriDao withdrawCashPriDao;
	
	@Autowired
	WithdrawProcessRecordPriDao withdrawProcessRecordPriDao;
	
	@Autowired
	WithdrawCashSecDao withdrawCashSecDao;
	
	@Autowired
	RecordConsumptionSecDao recordConsumptionSecDao;
	
	@Autowired
	CommissionByGuardConfigSecDao commissionByGuardConfigSecDao;
	
	@Autowired
	ChannelGuardSecDao channelGuardSecDao;
	
	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;
	
	@Autowired
	GroupProfileSecDao groupProfileSecDao;
	
	@Autowired
	ChannelGroupSecDao channelGroupSecDao;
	
	@Autowired
	ChannelOpeRecordUtil channelOpeRecordUtil;
	
	@Autowired
	HotChannelDao hotChannelDao;

	@Override
	public ResultDataSet verifyAnchorAuth(String superAccId, String accId, boolean agree) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorAuth authAnchor = null;
		try {
			Account account = accountPriDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			if (account.isAuthed() && account.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("已经是认证用户");
				return rds;
			}
			if (!account.isAnchor()) {
				authAnchor = authAnchorPriDao.findByAccIdAndAuthed(accId, AuthType.Check.getValue());
				if (authAnchor == null) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("此用户未申请审核");
					return rds;
				}

				if (agree) {
					authAnchor.setAuthed(AuthType.Success.getValue());
					authAnchorPriDao.save(authAnchor);

					account.setAuthed(true);
					account.setAnchor(true);
					account.setStageName(account.getNickName());
					accountPriDao.save(account);
				}
				else{
					authAnchor.setAuthed(AuthType.Rejected.getValue());
					authAnchorPriDao.save(authAnchor);
				}
				rds.setData(authAnchor);
				rds.setCode(ResultCode.SUCCESS.getCode());
			}
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet noticeAnchor(String superAccid, String title, String content) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet queryAnchorList(String param, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "registDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<Account> acc = accountSecDao.findAnchorByParam(param, pageable);
		rds.setData(MyPageUtils.getMyPage(acc));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorVerifyList(String sortDir, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString(sortDir), "createTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<AnchorAuth> acc = authAnchorSecDao.findByAuthed(AuthType.Check.getValue(), pageable);
		rds.setData(MyPageUtils.getMyPage(acc));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAnchorInfo(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			Account account = accountSecDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			if (!account.isAuthed() || !account.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不是主播");
				return rds;
			}
			AnchorAuth authAnchor = authAnchorSecDao.findOne(accId);
			if (authAnchor == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("未查到主播身份资料");
				return rds;
			}
			Channel channel = channelSecDao.findByOwnerId(accId);
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("account", account);
			response.put("authAnchor", authAnchor);
			if (channel != null){
				response.put("channel", channel);
			}
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet getAnchorChannelAndGroup(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			Account account = accountPriDao.findOne(accId);
			if (account == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不存在此账号" + accId);
				return rds;
			}
			if (!account.isAuthed() || !account.isAnchor()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不是主播");
				return rds;
			}
			AnchorAuth authAnchor = authAnchorPriDao.findByAccIdAndAuthed(accId, AuthType.Success.getValue());

			if (authAnchor == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("未查到主播身份资料");
				return rds;
			}
			AnchorContract anchorContract = anchorUtils.getAnchorContract(accId);
			Channel channel = channelSecDao.findByOwnerId(accId);
			ResponseData response = new ResponseData();
			if (anchorContract == null) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setMsg("该主播未签约经纪公司");
				return rds;
			}
			if (channel == null) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				response.put("gId", anchorContract.getgId());
				rds.setData(response);
				return rds;
			}
			response.put("cId", channel.getcId());
			response.put("gId", anchorContract.getgId());
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());

		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet getAnchorPushRecord(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		if (toTime < fromTime){
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}
		
		if (toTime - fromTime > DateUtils.monthMilliSecond * 2){
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
	public ResultDataSet calcAnchorActiveTime(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		if (toTime < fromTime){
			long tmp = toTime;
			toTime = fromTime;
			fromTime = tmp;
		}
		
		if (toTime - fromTime > DateUtils.monthMilliSecond * 2){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("统计时间间隔过长");
			return rds;
		}
		
		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		long totalTime = 0;
		if(anchorPushRecords != null){
			for (AnchorPushRecord record : anchorPushRecords){
				if (record.getCloseTime() == 0){
					continue;
				}
				if (record.getOpenTime() > record.getCloseTime()){
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
	public ResultDataSet getAnchorCommissionInfo(int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort =new Sort(Sort.Direction.DESC,"commission");
		Pageable pageable=new PageRequest(pageNum, pageSize, sort);
		Page<Wallet> anchorCommissionInfo=walletSecDao.findAnchorWallet(pageable);
		if(anchorCommissionInfo == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());		
		}
		rds.setData(MyPageUtils.getMyPage(anchorCommissionInfo));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAnchorWithdrawCashsInfo( int status,int pageSize, int pageNum) {
	ResultDataSet rds  = new ResultDataSet();
	Pageable pageable = new PageRequest(pageNum, pageSize);
	Page<WithdrawCash> AnchorWithdrawCashsInfo=withdrawCashSecDao.findByStatus(status, pageable);
	if(AnchorWithdrawCashsInfo == null){
		rds.setCode(ResultCode.PARAM_INVALID.getCode());
		rds.setMsg("无人提现");
		return rds;
	}
	rds.setData(AnchorWithdrawCashsInfo);
	rds.setCode(ResultCode.SUCCESS.getCode());	
	return rds;
	}

	@Override
	public ResultDataSet processWithdrawCash(String superAccId, String withdrawId, int status) {
		ResultDataSet rds = new ResultDataSet();
		WithdrawCash withdrawCash = withdrawCashPriDao.findOne(withdrawId);
		if (withdrawCash == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有提现请求");
			return rds;
		}
		if (WithdrawCashStatus.values()[status] == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("状态值不存在");
			return rds;
		}
		if (status == withdrawCash.getStatus()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能重复设置同一状态");
			return rds;
		}
		
		WithdrawProcessRecord record = new WithdrawProcessRecord();
		record.setSupperId(superAccId);
		record.setWithdrawId(withdrawId);
		record.setOldStatus(withdrawCash.getStatus());
		record.setNewStatus(status);
		record.setDateTime(DateUtils.getNowTime());
		withdrawProcessRecordPriDao.save(record);
		
		withdrawCash.setStatus(status);
		withdrawCashPriDao.save(withdrawCash);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAnchorStatus(String superAccId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		if(accId==null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;	
		}
		AnchorContract contract= anchorUtils.getAnchorContract(accId);
		Channel channel = channelSecDao.findByOwnerId(accId);
		HashMap<String,Object> respone=new HashMap<String,Object>();
		respone.put("contract",	contract != null);
		respone.put("assigned",	channel != null);
		rds.setData(respone);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	

	@Override
	public ResultDataSet queryOpenGuardRecord(String accId, long fromTime, long toTime,
			int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		if (toTime - fromTime > DateUtils.monthMilliSecond * 2){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间过长");
			return rds;
		}
		
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询守护信息");
			return rds;
		}
		
		Iterable<CommissionByGuardConfig> commissionByGuardConfigs = commissionByGuardConfigSecDao.findAll();
		if (commissionByGuardConfigs == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("缺少俸禄配置");
			return rds;
		}
		Map<Integer, Long> guardCommissionConfig = new HashMap<>();
		for (CommissionByGuardConfig config : commissionByGuardConfigs){
			guardCommissionConfig.put(config.getGuardLevel(), config.getMoneyOneMonth());
		}
		
		Pageable pageable = new PageRequest(pageNum, pageSize, Direction.ASC, "date");
		Page<RecordConsumption> page = recordConsumptionSecDao.findByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(
				channel.getChannelId(), GoodsType.BUY_GUARD.getValue(), fromTime - 1, toTime + 1, pageable);

		List<RecordConsumption> records = page.getContent();
		List<ResponseData> rebuilds = new ArrayList<ResponseData>();
		ResponseData result = new ResponseData();
		for (RecordConsumption rd : records){
			ResponseData tmp = new ResponseData();
			tmp.put("accId", 		rd.getAccId());
			tmp.put("level", 		rd.getGoodsId());
			tmp.put("date", 		rd.getDate());
			tmp.put("month", 		rd.getGoodsNumber());
			tmp.put("amount", 		rd.getAmount());
			
			int level = Integer.parseInt(rd.getGoodsId());
			Long commission = guardCommissionConfig.get(level);
			if (commission != null){				
				tmp.put("commission", 		commission.longValue() * rd.getGoodsNumber());
			}else{
				tmp.put("commission", 		0);
			}
			rebuilds.add(tmp);
		}
		
		result.put("content", 	rebuilds);
		result.put("count", 	page.getTotalElements());
		result.put("size", 		page.getSize());
		result.put("pages", 	page.getTotalPages());
		result.put("num", 		page.getNumber());
		
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getIncreasedGuardCount(String accId,long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询守护信息");
			return rds;
		}
		int increasedCount = recordConsumptionSecDao.countByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(
				channel.getChannelId(), GoodsType.BUY_GUARD.getValue(), fromTime, toTime);
		ResponseData rp = new ResponseData();
		rp.put("count", increasedCount);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTotalGuardCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询守护信息");
			return rds;
		}

		long nowDay = 0;
		try {
			nowDay = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalCount = channelGuardSecDao.countByCIdAndDeadLineGreaterThan(channel.getcId(), nowDay);
		
		ResponseData rp = new ResponseData();
		rp.put("count", totalCount);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryOpenClubRecord(String accId, long fromTime, long toTime, int pageNum,
			int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		if (toTime - fromTime > DateUtils.monthMilliSecond * 2){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间过长");
			return rds;
		}
		
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询粉丝团信息");
			return rds;
		}
		
		Pageable pageable = new PageRequest(pageNum, pageSize, Direction.ASC, "date");
		Page<RecordConsumption> page = recordConsumptionSecDao.findByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(
				channel.getChannelId(), GoodsType.BUY_FANSCLUBS.getValue(), fromTime - 1, toTime + 1, pageable);

		rds.setData(MyPageUtils.getMyPage(page));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getIncreasedClubMemberCount(String accId,long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();

		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询粉丝团信息");
			return rds;
		}
		int increasedCount = recordConsumptionSecDao.countByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(
				channel.getChannelId(), GoodsType.BUY_FANSCLUBS.getValue(), fromTime, toTime);
		ResponseData rp = new ResponseData();
		rp.put("count", increasedCount);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTotalClubMemberCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道,无法查询粉丝团信息");
			return rds;
		}

		long nowDay = 0;
		try {
			nowDay = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int totalCount = channelFansClubSecDao.countByCIdAndEndDateGreaterThan(channel.getcId(), nowDay);
		ResponseData rp = new ResponseData();
		rp.put("count", totalCount);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAnchorChannel(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播没有频道");
			return rds;
		}
		rds.setData(channel);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet assignChannel(String superAccId, String accId, String fId, String gId, 
			String cId, int type, int groupRate, int settleMode) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
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
		AnchorContract anchorContract = anchorUtils.getAnchorContract(accId);
		if (anchorContract != null && !anchorContract.getgId().equals(gId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播已经签约了其他经纪公司");
			return rds;
		}
		try {
			anchorContract = anchorContractPriDao.findByAccIdAndStatusAndEndDateGreaterThan(accId,
					AnchorContractStatus.REQUESTED.getValue(), DateUtils.getNowDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(anchorContract != null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播已经提交了签约申请，请现行处理。");
			return rds;
		}
		// 验证参数合法性
		GroupProfile groupProfile = groupProfileSecDao.findByFId(fId);
		if (groupProfile == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("指定的经纪公司不存在");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会id错误");
			return rds;
		}
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请选择公会，不要选择公会下属分组");
			return rds;
		}
		if (!channelGroup.getProfileId().equals(fId)){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请选择当前经纪公司下属的公会");
			return rds;
		}
		Channel channel = channelSecDao.findByCId(cId);
		if (channel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("指定的频道不存在");
			return rds;
		}
		if (!StringUtils.StringIsEmptyOrNull(channel.getOwnerId())){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道已分配给其他主播");
			return rds;
		}
		
		//频道所在分组
		ChannelGroup cgChannel = channelGroupSecDao.findByGId(channel.getgId());
		if (cgChannel == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道数据错误");
			return rds;
		}
		
		//验证频道所在分组是否属于公会
		boolean belongTo = false;
		if (gId.equals(cgChannel.getgId())){
			belongTo = true;
		}else if (!StringUtils.StringIsEmptyOrNull(cgChannel.getParentId())){
			if (gId.equals(cgChannel.getParentId())){
				belongTo = true;
			}
		}
		if (!belongTo){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("频道不属于公会");
			return rds;
		}
		// 创建虚拟合约
		long nowTime = DateUtils.getNowTime();
		anchorContract = new AnchorContract();
		anchorContract.setAccId(accId);
		anchorContract.setgId(gId);
		anchorContract.setCreateTime(nowTime);
		anchorContract.setCtId(StringUtils.createUUID());
		anchorContract.setEndDate(0);
		anchorContract.setGroupRate(groupRate);
		anchorContract.setSettleMode(settleMode);
		anchorContract.setStartDate(nowTime);
		anchorContract.setStatus(AnchorContractStatus.AGREED.getValue());
		anchorContract.setDirection(ContractRequestDirection.SuperAdmin.getValue());
		anchorContractPriDao.save(anchorContract);
		
		channel.setOwnerId(accId);
		channel.setType(type);
		channel.setChannelName(account.getStageName());
		channel.setClubLevel(1);
		channel.setClubScore(0L);
		channelPriDao.save(channel);
		
		channelOpeRecordUtil.assignOwner(channel.getcId(), accId, superAccId, gId, true);
		
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			hotChannel = new HotChannel();
		}
		hotChannel.setId(channel.getcId());
		hotChannel.setChannelId(channel.getChannelId());
		hotChannel.setOwnerId(channel.getOwnerId());
		hotChannel.setChannelName(channel.getChannelName());
		hotChannel.setTitle(channel.getTitle());
		hotChannel.setgId(channel.getgId());
		hotChannel.setStatus(channel.getStatus());
		hotChannel.setType(channel.getType());
		hotChannel.setYunXinCId(channel.getYunXinCId());
		hotChannel.setYunXinRId(channel.getYunXinRId());
		hotChannel.setHlsPullUrl(channel.getHlsPullUrl());
		hotChannel.setHttpPullUrl(channel.getHttpPullUrl());
		hotChannel.setPushUrl(channel.getPushUrl());
		hotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
		hotChannel.setLocation(account.getLocation());
		hotChannel.setWeekOffer(new Long(0));
		hotChannel.setBrightness(0);
		hotChannel.setClubLevel(channel.getClubLevel());
		hotChannel.setClubScore(channel.getClubScore());
		hotChannelDao.save(hotChannel);
		return rds;
	}
}
