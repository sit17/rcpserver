package com.i5i58.service.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.platform.IPlatformTopGroupAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.anchor.AnchorPushRecord;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelMic;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.ForceCancelContract;
import com.i5i58.data.group.ForceCancelContractStatus;
import com.i5i58.data.group.GroupAdminor;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.ForceCancelContractPriDao;
import com.i5i58.primary.dao.profile.GroupProfilePriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.anchor.AnchorPushRecordSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.group.ForceCancelContractSecDao;
import com.i5i58.secondary.dao.group.GroupAdminorSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;
import com.i5i58.secondary.dao.record.MoneyFlowSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelOpeRecordUtil;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.ContractOpeRecordUtil;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CreateChannelResult;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class TopGroupAdminSuperService implements IPlatformTopGroupAdmin {
	Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	GroupProfileSecDao groupProfileSecDao;

	@Autowired
	GroupAdminorSecDao groupAdminorSecDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	@Autowired
	AnchorUtils anchorUtils;
	
	@Autowired
	ChannelOpeRecordUtil channelOpeRecordUtil;
	
	@Autowired
	ContractOpeRecordUtil contractOpeRecordUtil;
	
	@Autowired
	HotChannelMicDao hotChannelMicDao;
	
	@Autowired
	ForceCancelContractPriDao forceCancelContractPriDao;
	
	@Autowired
	ForceCancelContractSecDao forceCancelContractSecDao;
	
	@Autowired
	MoneyFlowSecDao moneyFlowSecDao;
	
	@Autowired
	AccountSecDao accountSecDao;
	
	@Autowired
	AnchorPushRecordSecDao anchorPushRecordSecDao;
	
	@Autowired
	GroupProfilePriDao groupProfilePriDao;
	
	@Autowired
	ChannelUtils channelUtils;
	
	@Autowired
	OpenIdUtils openIdUtils;
	
	@Override
	public ResultDataSet queryTopGroupList(String param, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "channelCount");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGroup> data = channelGroupSecDao.findTopGroupByParam(param, pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet enableTopGroup(String superAccid, String gId, boolean enable) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupPriDao.findByGId(gId);
		group.setNullity(!enable);
		channelGroupPriDao.save(group);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryProfileByTopGroup(String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupSecDao.findByGId(gId);
		if (group != null) {
			GroupProfile profile = groupProfileSecDao.findByFId(group.getProfileId());
			rds.setData(profile);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAdminorsByTopGroup(String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupPriDao.findTopGroupByGId(gId);

		if (group != null) {
			List<GroupAdminor> groupAdminors = groupAdminorSecDao.findByGId(gId);
			rds.setData(groupAdminors);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet noticeTopGroup(String superAccid, String title, String content) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultDataSet getChannelByTopGroup(String gId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupPriDao.findTopGroupByGId(gId);
		if (group != null) {
			List<String> gids = new ArrayList<String>();
			gids.add(group.getgId());
			List<ChannelGroup> groups = channelGroupPriDao.findByParentId(gId);
			if (groups != null && groups.size() > 0) {
				for (ChannelGroup cg : groups) {
					gids.add(cg.getgId());
				}
			}
			List<Channel> channels = new ArrayList<Channel>();
			for (String id : gids) {
				List<Channel> chs = channelSecDao.findByGId(id);
				channels.addAll(chs);
			}
			ResponseData response = new ResponseData();
			response.put("pageSize", pageSize);
			response.put("pageNum", pageNum);
			response.put("count", channels.size());
			response.put("pages", channels.size() / pageSize + 1);
			List<Channel> resultList = new ArrayList<Channel>();
			if (channels.size() >= pageSize * pageNum + pageSize) {
				for (int i = pageSize * pageNum; i < pageSize * pageNum + pageSize; i++) {
					resultList.add(channels.get(i));
				}
			} else if (channels.size() > pageSize * pageNum) {
				for (int i = pageSize * pageNum; i < channels.size(); i++) {
					resultList.add(channels.get(i));
				}
			}

			response.put("content", resultList);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		rds.setData("不存在该公会");
		rds.setCode(ResultCode.PARAM_INVALID.getCode());
		return rds;

	}

	@Override
	public ResultDataSet getAnchorByTopGroup(String gId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupPriDao.findTopGroupByGId(gId);
		if (group != null) {
			List<String> gids = new ArrayList<String>();
			gids.add(group.getgId());
			List<ChannelGroup> groups = channelGroupPriDao.findByParentId(gId);
			if (groups != null && groups.size() > 0) {
				for (ChannelGroup cg : groups) {
					gids.add(cg.getgId());
				}
			}

			List<AnchorContract> cts = new ArrayList<AnchorContract>();
			for (String id : gids) {
				List<AnchorContract> contracts = anchorUtils.getActiveContractByGId(id);
				if (contracts != null){					
					cts.addAll(contracts);
				}
			}

			ResponseData response = new ResponseData();
			response.put("pageSize", pageSize);
			response.put("pageNum", pageNum);
			response.put("pages", cts.size() / pageSize + 1);
			response.put("count", cts.size());
			List<AnchorContract> resultList = new ArrayList<AnchorContract>();
			if (cts.size() >= pageSize * pageNum + pageSize) {
				for (int i = pageSize * pageNum; i < pageSize * pageNum + pageSize; i++) {
					resultList.add(cts.get(i));
				}
			} else if (cts.size() > pageSize * pageNum) {
				for (int i = pageSize * pageNum; i < cts.size(); i++) {
					resultList.add(cts.get(i));
				}
			}

			response.put("content", resultList);
			rds.setData(response);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		rds.setData("不存在该公会");
		rds.setCode(ResultCode.PARAM_INVALID.getCode());
		return rds;

	}

	@Override
	public ResultDataSet responseCancelContract(String ctId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract contract = anchorContractPriDao.findOne(ctId);
		if (contract == null || contract.getStatus() != AnchorContractStatus.REQUEST_CANCEL.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约或已取消");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupPriDao.findByGId(contract.getgId());
		if (channelGroup == null||contract.getStatus() != AnchorContractStatus.REQUEST_CANCEL.getValue()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("公会ID错误");
			return rds;
		}

		Channel channel = channelPriDao.findByOwnerId(contract.getAccId());
		if (channel != null) {
			channel.setOwnerId("");
			HotChannel hotChannel = hotChannelDao.findOne(channel.getChannelId());
			channelPriDao.save(channel);
			channelOpeRecordUtil.removeOwner(channel.getcId(), contract.getAccId(), "", contract.getgId());
			
			HotChannelMic hotChannelMic = hotChannelMicDao.findOne(channel.getcId() + "_" + contract.getAccId());
			if (hotChannelMic != null){
				hotChannelMicDao.delete(hotChannelMic);
			}
			
			if (hotChannel == null) {
				HotChannel newHotChannel = new HotChannel();
				newHotChannel.setId(channel.getcId());
				newHotChannel.setChannelId(channel.getChannelId());
				newHotChannel.setOwnerId(channel.getOwnerId());
				newHotChannel.setChannelName(channel.getChannelName());
				newHotChannel.setType(channel.getType());
				newHotChannel.setStatus(channel.getStatus());
				newHotChannel.setCoverUrl(channel.getCoverUrl());
				newHotChannel.setgId(channel.getgId());
				newHotChannel.setChannelNotice(channel.getChannelNotice());
				newHotChannel.setYunXinCId(channel.getYunXinCId());
				newHotChannel.setYunXinRId(channel.getYunXinRId());
				newHotChannel.setPushDevice(0);
				newHotChannel.setPushUrl(channel.getPushUrl());
				newHotChannel.setHlsPullUrl(channel.getHlsPullUrl());
				newHotChannel.setHttpPullUrl(channel.getHttpPullUrl());
				newHotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
				newHotChannel.setConnCid("");
				newHotChannel.setLocation("");
				newHotChannel.setPlayerCount(0);
				newHotChannel.setWeekOffer(0);
				newHotChannel.setHeartCount(0);
				newHotChannel.setHeartUserCount(0);
				newHotChannel.setBrightness(0);
				newHotChannel.setTitle(channel.getTitle());
				hotChannelDao.save(newHotChannel);
			} else {
				hotChannel.setOwnerId("");
				hotChannelDao.save(hotChannel);
			}
		}

		contract.setStatus(AnchorContractStatus.CANCELED.getValue());
		contract.setStartDate(DateUtils.getNowTime());
		anchorContractPriDao.save(contract);
		
		contractOpeRecordUtil.agreeCancel(contract.getAccId(), contract.getgId());
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTopGroupByGId(String gId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup group = channelGroupPriDao.findByGId(gId);
		if (group == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("分组Id不存在");
			return rds;
		}
		// 已经是顶级分组了
		if (group.getParentId() == null || group.getParentId().isEmpty()) {
			rds.setData(group);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		// 若不是顶级分组，继续查找其上层分组
		ChannelGroup topGroup = channelGroupPriDao.findByGId(group.getParentId());
		if (topGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("上层分组不存在");
			return rds;
		}
		rds.setData(topGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryCancelContract(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<AnchorContract> data = anchorContractPriDao.findByStatusAndEndDateGreaterThan(
				AnchorContractStatus.REQUEST_CANCEL.getValue(), DateUtils.getNowTime(), pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryForceCancelContract(int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<ForceCancelContract> data = forceCancelContractSecDao.findByStatus(
				ForceCancelContractStatus.REQUESTED.getValue(), pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet responseForceCancelContract(String ctId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ForceCancelContract forceCancelContract =forceCancelContractPriDao.findByCtIdAndStatus(ctId, ForceCancelContractStatus.REQUESTED.getValue());
		if (forceCancelContract == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该请求");
			return rds;
		}
		AnchorContract contract = anchorContractPriDao.findOne(forceCancelContract.getCtId());
		if (contract == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("未找到该合约");
			return rds;
		}
		if(contract.getStatus()!=AnchorContractStatus.AGREED.getValue()
				&&contract.getStatus()!=AnchorContractStatus.REQUEST_CANCEL.getValue()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该合约不可取消");
			return rds;
		}
		Channel channel = channelPriDao.findByOwnerId(contract.getAccId());
		if (channel != null) {
			channel.setOwnerId("");
			channelPriDao.save(channel);
			channelOpeRecordUtil.removeOwner(channel.getcId(), contract.getAccId(), forceCancelContract.getAccId(), contract.getgId());
			
			HotChannelMic hotChannelMic = hotChannelMicDao.findOne(channel.getcId() + "_" + contract.getAccId());
			if (hotChannelMic != null){
				hotChannelMicDao.delete(hotChannelMic);
			}
			
			HotChannel hotChannel = hotChannelDao.findOne(channel.getChannelId());
			if (hotChannel == null) {
				HotChannel newHotChannel = new HotChannel();
				newHotChannel.setId(channel.getcId());
				newHotChannel.setChannelId(channel.getChannelId());
				newHotChannel.setOwnerId(channel.getOwnerId());
				newHotChannel.setChannelName(channel.getChannelName());
				newHotChannel.setType(channel.getType());
				newHotChannel.setStatus(channel.getStatus());
				newHotChannel.setCoverUrl(channel.getCoverUrl());
				newHotChannel.setgId(channel.getgId());
				newHotChannel.setChannelNotice(channel.getChannelNotice());
				newHotChannel.setYunXinCId(channel.getYunXinCId());
				newHotChannel.setYunXinRId(channel.getYunXinRId());
				newHotChannel.setPushDevice(0);
				newHotChannel.setPushUrl(channel.getPushUrl());
				newHotChannel.setHlsPullUrl(channel.getHlsPullUrl());
				newHotChannel.setHttpPullUrl(channel.getHttpPullUrl());
				newHotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
				newHotChannel.setConnCid("");
				newHotChannel.setLocation("");
				newHotChannel.setPlayerCount(0);
				newHotChannel.setWeekOffer(0);
				newHotChannel.setHeartCount(0);
				newHotChannel.setHeartUserCount(0);
				newHotChannel.setBrightness(0);
				newHotChannel.setTitle(channel.getTitle());
				hotChannelDao.save(newHotChannel);
			} else {
				hotChannel.setOwnerId("");
				hotChannelDao.save(hotChannel);
			}
		}
		contract.setStatus(AnchorContractStatus.CANCELED.getValue());
//		contract.setStartDate(DateUtils.getNowTime());
		anchorContractPriDao.save(contract);
		
		contractOpeRecordUtil.forceCancelContract(contract.getAccId(), contract.getgId(),forceCancelContract.getCancelDirection());	
		forceCancelContract.setStatus(ForceCancelContractStatus.AGREED.getValue());
		forceCancelContract.setResponsedDateTime(DateUtils.getNowTime());
		forceCancelContractPriDao.save(forceCancelContract);
		
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAverageGiftCommision(String accId,long fromTime,long toTime) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("用户不存在");
			return rds;
		}
		if (!account.isAnchor()){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("只能查询主播");
			return rds;
		}
		
		//long fromTime = DateUtils.getLastMonthBeginning();
		//long toTime = DateUtils.getLastMonthEnd();
		if(toTime-fromTime>DateUtils.monthMilliSecond*2){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("查询时间过长");
			return rds;
		}
		List<AnchorPushRecord> anchorPushRecords = anchorPushRecordSecDao.findByTime(accId, fromTime, toTime);
		
		int effectiveDay = 0;
		for (AnchorPushRecord a : anchorPushRecords){
			if (a.getCloseTime() <= 0)
				continue;
			if (a.isIgnored())
				continue;
			if (a.getOpenTime() >= a.getCloseTime())
				continue;
			if (a.getCloseTime() - a.getOpenTime() >= DateUtils.dayMilliSecond * 2){
				effectiveDay++;
			}
		}
		
		System.out.println(effectiveDay);
		
		if (effectiveDay <= 0){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("当月没有有效开播记录");
			return rds;
		}
		
		List<MoneyFlow> giftCommissionMoneyFlow = moneyFlowSecDao.getTotalGiftCommission(accId, 
				MoneyFlowType.GiftCommission.getValue(), fromTime, toTime);
		long giftCommission = 0;
		if (giftCommissionMoneyFlow != null && giftCommissionMoneyFlow.size() > 0){
			for(MoneyFlow moneyFlow : giftCommissionMoneyFlow){
				giftCommission += moneyFlow.getTargetCommission() - moneyFlow.getSourceCommission();
			}
		}
		double average = giftCommission * 1.0 / effectiveDay;
		ResponseData rp = new ResponseData();
		rp.put("days", effectiveDay);
		rp.put("dailyCommission", average);
		
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	@Override
	public ResultDataSet createChannel(String superAccId, String gId, String name, String createIp) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel;
		try {
			ChannelGroup channelGroup = channelGroupPriDao.findByGId(gId);
			if (channelGroup == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有该频道组");
				return rds;
			}
			if (!channelUtils.checkChannelCount(channelGroup)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("可创建频道数量已满");
				return rds;
			}
			
			String strOpenId = openIdUtils.getRandomChannelId(true);
			if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("id exception");
				return rds;
			}

			// 创建频道
			String yunXinCId = "";
			String pushUrl = "";
			String httpPullUrl = "";
			String rtmpPullUrl = "";
			String hlsPullUrl = "";
			CreateChannelResult resultC = Videocloud163.createChannel(strOpenId, "0");
			if (resultC.getCode().equals("200")) {
				yunXinCId = resultC.getRet().getCid();
				pushUrl = resultC.getRet().getPushUrl();
				httpPullUrl = resultC.getRet().getHttpPullUrl();
				rtmpPullUrl = resultC.getRet().getRtmpPullUrl();
				hlsPullUrl = resultC.getRet().getHlsPullUrl();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(resultC.getString("msg"));
				return rds;
			}
			// >>>创建聊天室
			String yunXinRId = "";
			YXResultSet resultR = YunxinIM.createChatRoom(superAccId, strOpenId, "", pushUrl, "");
			if (resultR.getCode().equals("200")) {
				yunXinRId = resultR.getMap("chatroom").get("roomid").toString();
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(resultR.getString("desc"));
				return rds;
			}

			String uuidStr = StringUtils.createUUID();
			channel = new Channel();
			channel.setCreatorId(superAccId);
			channel.setcId(uuidStr);
			channel.setChannelId(strOpenId);
			channel.setOwnerId("");
			channel.setChannelName(name);
			channel.setTitle("");
			channel.setgId(gId);
			channel.setStatus(0);
			channel.setType(0);
			channel.setYunXinCId(yunXinCId);
			channel.setYunXinRId(yunXinRId);
			channel.setPushUrl(pushUrl);
			channel.setHttpPullUrl(httpPullUrl);
			channel.setRtmpPullUrl(rtmpPullUrl);
			channel.setHlsPullUrl(hlsPullUrl);
			channel.setCreateIp(createIp);
			channel.setCreateDate(DateUtils.getNowTime());
			channel.setClubLevel(1);
			channel.setClubScore(0L);
			channelPriDao.save(channel);

			channelUtils.updateChannelCount(channelGroup, 1);
			String topGId = gId;
			if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
				topGId = channelGroup.getParentId();
			}
			channelOpeRecordUtil.createChannel(channel.getcId(), topGId, superAccId, true);
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("trans_exception");
			return rds;
		}
		if (channel != null) {
			rds.setData(channel);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

}
