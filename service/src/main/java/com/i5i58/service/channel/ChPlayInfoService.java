package com.i5i58.service.channel;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.apis.channel.IChannelPlayInfo;
import com.i5i58.apis.constants.PageParams;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.clubTask.ClubTaskUtils;
import com.i5i58.config.MyThreadPool;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.HotDailyHeart;
import com.i5i58.data.account.MountStore;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.anchor.CommissionByGuardConfig;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelGift;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.ChannelGuardConfig;
import com.i5i58.data.channel.ChannelMount;
import com.i5i58.data.channel.ChannelNotice;
import com.i5i58.data.channel.ChannelSound;
import com.i5i58.data.channel.ClubDailyClock;
import com.i5i58.data.channel.ClubDailyClocksSummary;
import com.i5i58.data.channel.ClubTaskRecord;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelGift;
import com.i5i58.data.channel.HotChannelGiftType;
import com.i5i58.data.channel.HotChannelGuardConfig;
import com.i5i58.data.channel.HotChannelHeartUser;
import com.i5i58.data.channel.HotChannelMount;
import com.i5i58.data.channel.HotChannelRightInfo;
import com.i5i58.data.channel.HotChannelSound;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.channel.HotClubTaskConfig;
import com.i5i58.data.channel.HotFansClubConfig;
import com.i5i58.data.channel.TakeHeartRecord;
import com.i5i58.data.config.PlatformConfig;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.data.social.FollowInfo;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.MountStorePriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelGiftPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardConfigPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelMountPriDao;
import com.i5i58.primary.dao.channel.ChannelNoticePriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ClubDailyClockPriDao;
import com.i5i58.primary.dao.channel.ClubDailyClocksSummaryPriDao;
import com.i5i58.primary.dao.channel.ClubTaskRecordPriDao;
import com.i5i58.primary.dao.channel.TakeHeartRecordPriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.record.RecordConsumptionPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelGiftDao;
import com.i5i58.redis.all.HotChannelGiftTypeDao;
import com.i5i58.redis.all.HotChannelGuardConfigDao;
import com.i5i58.redis.all.HotChannelHeartUserDao;
import com.i5i58.redis.all.HotChannelMountDao;
import com.i5i58.redis.all.HotChannelRightInfoDao;
import com.i5i58.redis.all.HotChannelSoundDao;
import com.i5i58.redis.all.HotChannelViewerDao;
import com.i5i58.redis.all.HotClubTaskConfigDao;
import com.i5i58.redis.all.HotClubTaskRecordDao;
import com.i5i58.redis.all.HotDailyHeartDao;
import com.i5i58.redis.all.HotFansClubConfigDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.anchor.CommissionByGuardConfigSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGiftSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardConfigSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelMountSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.channel.ChannelSoundSecDao;
import com.i5i58.secondary.dao.channel.ClubDailyClockSecDao;
import com.i5i58.secondary.dao.channel.ClubDailyClocksSummarySecDao;
import com.i5i58.secondary.dao.channel.ClubTaskRecordSecDao;
import com.i5i58.secondary.dao.channel.TakeHeartRecordSecDao;
import com.i5i58.secondary.dao.config.PlatformConfigSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.service.channel.async.TaskYxChatFollowAnchor;
import com.i5i58.service.channel.async.TaskYxChatGiveHeart;
import com.i5i58.service.channel.async.TaskYxChatOpenClub;
import com.i5i58.service.channel.async.TaskYxChatOpenGuard;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.CheckUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MountPresentUtil;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class ChPlayInfoService implements IChannelPlayInfo {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	JsonUtils jsonUtils;

	@Autowired
	HotChannelGiftDao hotChannelGiftDao;

	@Autowired
	ClubDailyClockPriDao clubDailyClockPriDao;

	@Autowired
	ClubDailyClockSecDao clubDailyClockSecDao;

	@Autowired
	ClubDailyClocksSummaryPriDao clubDailyClocksSummaryPriDao;

	@Autowired
	ClubDailyClocksSummarySecDao clubDailyClocksSummarySecDao;

	@Autowired
	HotDailyHeartDao hotDailyHeartDao;

	@Autowired
	HotChannelHeartUserDao hotChannelHeartUserDao;

	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;

	@Autowired
	HotFansClubConfigDao hotFansClubConfigDao;

	@Autowired
	HotClubTaskConfigDao hotClubTaskConfigDao;

	@Autowired
	HotClubTaskRecordDao hotClubTaskRecordDao;

	@Autowired
	ChannelMountPriDao channelMountPriDao;

	@Autowired
	ChannelMountSecDao channelMountSecDao;

	@Autowired
	HotChannelMountDao hotChannelMountDao;

	@Autowired
	PlatformConfigPriDao platformConfigPriDao;

	@Autowired
	PlatformConfigSecDao platformConfigSecDao;

	@Autowired
	HotChannelGuardConfigDao hotChannelGuardConfigDao;

	@Autowired
	ChannelGuardConfigPriDao channelGuardConfigPriDao;

	@Autowired
	ChannelGuardConfigSecDao channelGuardConfigSecDao;

	@Autowired
	ChannelGiftPriDao channelGiftPriDao;

	@Autowired
	ChannelGiftSecDao channelGiftSecDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	HotChannelViewerDao hotChannelViewerDao;

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	HotChannelGiftTypeDao hotChannelGiftTypeDao;

	@Autowired
	HotChannelRightInfoDao hotChannelRightInfoDao;

	@Autowired
	MountStorePriDao mountStorePriDao;

	@Autowired
	RecordConsumptionPriDao recordConsumptionPriDao;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	MountPresentUtil mountPresentUtil;

	@Autowired
	ClubTaskUtils clubTaskUtils;

	@Autowired
	TakeHeartRecordPriDao takeHeartRecordPriDao;

	@Autowired
	TakeHeartRecordSecDao takeHeartRecordSecDao;

	@Autowired
	TaskUtil taskUtil;

	@Autowired
	ClubTaskRecordPriDao clubTaskRecordPriDao;

	@Autowired
	ClubTaskRecordSecDao clubTaskRecordSecDao;

	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;

	@Autowired
	HotChannelSoundDao hotChannelSoundDao;

	@Autowired
	ChannelSoundSecDao channelSoundSecDao;

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;
	
	@Autowired
	CommissionByGuardConfigSecDao commissionByGuardConfigSecDao;
	
	@Autowired
	ChannelNoticePriDao channelNoticePriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;
	
	@Autowired
	AnchorContractSecDao anchorContractSecDao;
	/**
	 * 礼物设置
	 */
	@Override
	public ResultDataSet getChannelGiftConfig() throws IOException {
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		List<HotChannelGift> hotChannelGift = (List<HotChannelGift>) hotChannelGiftDao.findAll();
		if (hotChannelGift == null || hotChannelGift.isEmpty()) {
			List<ChannelGift> data = channelGiftSecDao.findByNullity(false, sort);
			for (ChannelGift gift : data) {
				HotChannelGift hotGift = new HotChannelGift();
				hotGift.setSortId(gift.getSortId());
				hotGift.setMainId(gift.getId());
				hotGift.setName(gift.getName());
				hotGift.setForGuard(gift.isForGuard());
				hotGift.setForVip(gift.isForVip());
				hotGift.setPrice(gift.getPrice());
				hotGift.setAnchorPrice(gift.getAnchorPrice());
				hotGift.setUnit(gift.getUnit());
				hotGift.setMaxCount(gift.getMaxCount());
				hotGift.setVersion(gift.getVersion());
				hotGift.setPath(gift.getPath());
				hotGift.setFunction(gift.getFunction());
				hotGift.setCondition(gift.getCondition());
				hotGift.setNode(gift.getNode());
				hotGift.setFlashPath(gift.getFlashPath());
				hotChannelGiftDao.save(hotGift);
			}
			response.put("gift", data);
		} else {
			response.put("gift", hotChannelGift);
		}
		if (jedisUtils.exist(Constant.HOT_GIFT_CONFIG_VERSION_KEY)) {
			String value = jedisUtils.get(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_GIFT_CONFIG_VERSION_KEY, value);
		} else {
			PlatformConfig platformConfig = platformConfigSecDao.findOne(Constant.HOT_GIFT_CONFIG_VERSION_KEY);
			String version = "no_version";
			if (platformConfig != null) {
				version = platformConfig.getcValue();
			}
			jedisUtils.set(Constant.HOT_GIFT_CONFIG_VERSION_KEY, version);
			response.put(Constant.HOT_GIFT_CONFIG_VERSION_KEY, version);
		}
		Iterable<HotChannelGiftType> types = hotChannelGiftTypeDao.findAll();
		response.put("node", types);
		rds.setData(response);
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet getChannelMountConfig() throws IOException {
		// Define Response
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		List<HotChannelMount> hotChannelMount = (List<HotChannelMount>) hotChannelMountDao.findAll();
		if (hotChannelMount == null || hotChannelMount.isEmpty()) {
			List<ChannelMount> data = channelMountSecDao.findByNullity(false);
			for (ChannelMount mount : data) {
				HotChannelMount hotMount = new HotChannelMount();
				hotMount.setId(mount.getId());
				hotMount.setName(mount.getName());
				hotMount.setForGuard(mount.isForGuard());
				hotMount.setForVip(mount.isForVip());
				hotMount.setPrice(mount.getPrice());
				hotMount.setVersion(mount.getVersion());
				hotMount.setFunction(mount.getFunction());
				hotMount.setPath(mount.getPath());
				hotMount.setForFansClubs(mount.isForFansClubs());
				hotMount.setValidity((mount.getValidity()));
				hotChannelMountDao.save(hotMount);
			}
			response.put("mount", data);
		} else {
			response.put("mount", hotChannelMount);
		}
		if (jedisUtils.exist(Constant.HOT_MOUNT_CONFIG_VERSION_KEY)) {
			String value = jedisUtils.get(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, value);
		} else {
			PlatformConfig platformConfig = platformConfigSecDao.findOne(Constant.HOT_MOUNT_CONFIG_VERSION_KEY);
			String version = "no_version";
			if (platformConfig != null) {
				version = platformConfig.getcValue();
			}
			jedisUtils.set(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, version);
			response.put(Constant.HOT_MOUNT_CONFIG_VERSION_KEY, version);
		}
		rds.setData(response);
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet getAnimationConfig() {
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		if (!jedisUtils.exist(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY)
				|| !jedisUtils.exist(Constant.HOT_ANIMATION_RES_CONFIG_KEY)) {
			PlatformConfig animZipConfig = platformConfigSecDao.findOne(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
			PlatformConfig animResConfig = platformConfigSecDao.findOne(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
			if (animZipConfig == null || animResConfig == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("no_ch_animation");
				return rds;
			}
			jedisUtils.set(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig.getcValue());
			jedisUtils.set(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig.getcValue());
		} else {
			String animZipConfig = jedisUtils.get(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY);
			response.put(Constant.HOT_ANIMATION_ZIP_CONFIG_KEY, animZipConfig);
			String animResConfig = jedisUtils.get(Constant.HOT_ANIMATION_RES_CONFIG_KEY);
			response.put(Constant.HOT_ANIMATION_RES_CONFIG_KEY, animResConfig);
		}
		if (!jedisUtils.exist(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY)) {
			PlatformConfig versionConfig = platformConfigSecDao.findOne(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
			if (versionConfig == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("no_ch_html_version");
				return rds;
			}
			jedisUtils.set(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig.getcValue());
			response.put(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig.getcValue());
		} else {
			String versionConfig = jedisUtils.get(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY);
			response.put(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY, versionConfig);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelStatus(String cId) {
		ResultDataSet rds = new ResultDataSet();
		if (StringUtils.StringIsEmptyOrNull(cId)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("params_null");
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该频道不存在或不在线");
			return rds;
		}
		try {
			YXResultSet ret = Videocloud163.channelstatsChannel(hotChannel.getYunXinCId());
			if (ret.getCode().equals("200")) {
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setData(ret.getMap("ret").get("status"));
			} else {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(ret.getString("msg"));
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setMsg(e.getMessage());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet openClub(String accId, String cId, int month, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		// String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
		// if (serverKey != null && !serverKey.isEmpty()){
		// if (!serverKey.equalsIgnoreCase(cId)){
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setCode("您正在其他直播间观看，无法开通.");
		// return rds;
		// }
		//
		// Map<String, String> headers = new HashMap<>();
		// Map<String, String> params = new HashMap<>();
		// headers.put("accId", accId);
		//
		// params.put("cId", cId);
		// params.put("month", new Integer(month).toString());
		// params.put("clientIP", clientIp);
		//
		// rds = UniqueOperationCheck.httpPost(serverKey, "/channel/openClub",
		// headers, params);
		// return rds;
		// }
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Channel ch = channelPriDao.findByCId(cId);
		if (ch == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (accId.equals(ch.getOwnerId())) {
			rds.setMsg("主播无法加入自己的粉丝团");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		HotFansClubConfig hotFansClubConfig = hotFansClubConfigDao.findOne(month);
		if (hotFansClubConfig == null) {
			rds.setMsg("miss_fans_club_config");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		if (wallet.getiGold() < hotFansClubConfig.getDiscount() * month) {
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.OpenClub, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

		wallet.setiGold(wallet.getiGold() - (long) (hotFansClubConfig.getDiscount() * month)); // 是否可以这样转
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		ChannelFansClub fansClub = channelFansClubPriDao.findByCIdAndAccId(cId, accId);
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long nowTime = DateUtils.getNowTime();
		
		moneyFlow.setDateTime(nowTime);
		try {
			if (fansClub != null) {
				if (fansClub.getEndDate() > today) {
					fansClub.setEndDate(DateUtils.AddMonth(fansClub.getEndDate(), month));
					channelFansClubPriDao.save(fansClub);
				} else {
					fansClub.setEndDate(DateUtils.AddMonth(today, month));
					channelFansClubPriDao.save(fansClub);
				}
			} else {
				fansClub = new ChannelFansClub();
				fansClub.setcId(cId);
				fansClub.setAccId(accId);
				// fansClub.setClubName(hotChannel.getClubName());
				fansClub.setEndDate(DateUtils.AddMonth(today, month));
				channelFansClubPriDao.save(fansClub);
			}
		} catch (ParseException e) {
			logger.error("", e);
			rds.setMsg("parse_exception");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		ch.setClubMemberCount(ch.getClubMemberCount() + 1);
		channelPriDao.save(ch);
		hotChannel.setClubMemberCount(ch.getClubMemberCount());
		hotChannelDao.save(hotChannel);

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId(hotChannel.getChannelId());
		record.setAmount(hotFansClubConfig.getDiscount() * month);
		record.setClientIp(clientIp);
		record.setGoodsNumber(month);
		record.setDate(DateUtils.getNowTime());
		record.setDeadline(fansClub.getEndDate());
		record.setDescribe("");
		record.setGoodsId("");
		record.setGoodsType(GoodsType.BUY_FANSCLUBS.getValue());
		recordConsumptionPriDao.save(record);

		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer != null) {
			hotViewer.setClubDeadLine(fansClub.getEndDate());
			hotViewer.setClubName(hotChannel.getClubName());
			hotViewer.setClubLevel(hotChannel.getClubLevel());
			hotViewer.setFansClub(1);
			hotChannelViewerDao.save(hotViewer);
			TaskYxChatOpenClub yxChatOpenGuardThread = new TaskYxChatOpenClub(hotChannel.getYunXinRId(),
					hotViewer.getAccId(), hotViewer.getName(), hotViewer.getFaceSmallUrl(), hotViewer.getVip(),
					hotViewer.getVipDeadLine(), hotViewer.getGuardLevel(), hotViewer.getGuardDeadLine(),
					hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getFansClub(), hotViewer.getClubName(),
					hotViewer.getClubLevel(), hotViewer.getClubDeadLine(), hotViewer.isSuperUser(), jsonUtils);
			myThreadPool.getYunxinPool().execute(yxChatOpenGuardThread);
		}

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFansClubConfig() {
		ResultDataSet rds = new ResultDataSet();
		List<HotFansClubConfig> clubConfig = (List<HotFansClubConfig>) hotFansClubConfigDao.findAll();
		rds.setData(clubConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet openGuard(String accId, String cId, int level, int month, String clientIP) {
		ResultDataSet rds = new ResultDataSet();
		// String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
		// if (serverKey != null && !serverKey.isEmpty()){
		// if (!serverKey.equalsIgnoreCase(cId)){
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setCode("您正在其他直播间观看，无法开通.");
		// return rds;
		// }
		//
		// Map<String, String> headers = new HashMap<>();
		// Map<String, String> params = new HashMap<>();
		// headers.put("accId", accId);
		//
		// params.put("cId", cId);
		// params.put("level", new Integer(level).toString());
		// params.put("month", new Integer(month).toString());
		// params.put("clientIP", clientIP);
		//
		// rds = UniqueOperationCheck.httpPost(serverKey, "/channel/openGuard",
		// headers, params);
		// return rds;
		// }
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		if (accId.equals(hotChannel.getOwnerId())) {
			rds.setMsg("主播不能开通自己频道的骑士");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		// HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" +
		// accId);
		// if (hotViewer == null) {
		// rds.setMsg(ServerCode.NO_VIEWER.getCode());
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// return rds;
		// }
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannelGuardConfig hotChannelGuardConfig = hotChannelGuardConfigDao.findByLevelAndMonth(level, month);
		if (hotChannelGuardConfig == null) {
			Iterable<ChannelGuardConfig> channelGuardConfig = channelGuardConfigPriDao.findAll();
			for (ChannelGuardConfig hgc : channelGuardConfig) {
				HotChannelGuardConfig hcgc = new HotChannelGuardConfig();
				hcgc.setId(hgc.getId());
				hcgc.setLevel(hgc.getLevel());
				hcgc.setMonth(hgc.getMonth());
				hcgc.setPrice(hgc.getPrice());
				hotChannelGuardConfigDao.save(hcgc);
			}
			hotChannelGuardConfig = hotChannelGuardConfigDao.findByLevelAndMonth(level, month);
		}
		if (hotChannelGuardConfig == null) {
			rds.setMsg("miss_guard_config");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		long amount = hotChannelGuardConfig.getPrice();
		if (wallet.getiGold() < amount) {
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ChannelGuard channelGuard = channelGuardPriDao.findByAccIdAndCId(accId, cId);
		List<MountStore> mountStoreList = mountStorePriDao.findByAccIdAndCId(accId, cId);
		long today;
		try {
			today = DateUtils.getDate(new Date());
			long todayLong = new Date().getTime();
			if (channelGuard != null && channelGuard.getGuardLevel() > level) {
				if (channelGuard.getDeadLine() > today) {
					rds.setMsg("您已有更高级别的守护，无法开通该等级");
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					return rds;
				}
			}
			MoneyFlow moneyFlow = new MoneyFlow();
			moneyFlow.setAccId(accId);
			moneyFlow.setDateTime(todayLong);
			moneyFlow.setIpAddress(clientIP);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.OpenGuard, moneyFlow);
			HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

			wallet.setiGold(wallet.getiGold() - amount);
			walletPriDao.save(wallet);

			HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
			moneyFlowPriDao.save(moneyFlow);

			Long deadLine;
			if (channelGuard != null) {
				if (channelGuard.getDeadLine() > today && level == channelGuard.getGuardLevel()) {
					deadLine = DateUtils.AddMonth(channelGuard.getDeadLine(), month);
				} else {
					deadLine = DateUtils.AddMonth(today, month);
					channelGuard.setStartLine(todayLong);
				}
				channelGuard.setDeadLine(deadLine);
				channelGuard.setGuardLevel(level);
				channelGuard.setBuyTime(todayLong);
				channelGuardPriDao.save(channelGuard);
				for (MountStore g : mountStoreList) {
					ChannelMount mount = channelMountPriDao.findOne(g.getMountsId());
					if (mount.isForGuard()) {
						if (level < mount.getLevel()) {
							break;
						}
						g.setEndTime(deadLine);
						mountStorePriDao.save(g);
					}
				}
			} else {
				channelGuard = new ChannelGuard();
				channelGuard.setcId(cId);
				channelGuard.setAccId(accId);
				channelGuard.setGuardLevel(level);
				channelGuard.setDeadLine(DateUtils.AddMonth(today, month));
				channelGuard.setStartLine(todayLong);
				channelGuard.setBuyTime(todayLong);
				channelGuardPriDao.save(channelGuard);
			}

			mountPresentUtil.presentMountForOpenGuard(accId, level, cId, channelGuard.getDeadLine());

			RecordConsumption record = new RecordConsumption();
			record.setId(StringUtils.createUUID());
			record.setAccId(accId);
			record.setChannelId(hotChannel.getChannelId());
			record.setAmount(amount);
			record.setClientIp(clientIP);
			record.setGoodsNumber(month);
			record.setDate(todayLong);
			record.setDeadline(channelGuard.getDeadLine());
			record.setDescribe("");
			record.setGoodsId(String.valueOf(level));
			record.setGoodsType(GoodsType.BUY_GUARD.getValue());
			recordConsumptionPriDao.save(record);
			
			//主播俸禄增长
			Wallet anchorWallet = walletSecDao.findByAccId(hotChannel.getOwnerId());
			MoneyFlow anchorMoneyFlow = new MoneyFlow();
			anchorMoneyFlow.setAccId(hotChannel.getOwnerId());
			anchorMoneyFlow.setDateTime(todayLong);
			anchorMoneyFlow.setIpAddress(clientIP);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.CommissionOnOpenGuard, anchorMoneyFlow);
			HelperFunctions.setMoneyFlowSource(anchorWallet, anchorMoneyFlow);
			HelperFunctions.setMoneyFlowTarget(anchorWallet, anchorMoneyFlow);
			//guardLevel字段没有做唯一性限制
			//防止返回的数据行数太多
			List<CommissionByGuardConfig> commissionByGuardConfigs = 
					commissionByGuardConfigSecDao.findByGuardLevelOrderByMoneyOneMonth(level);
			if (commissionByGuardConfigs != null && commissionByGuardConfigs.size() > 0){
				CommissionByGuardConfig config = commissionByGuardConfigs.get(0);
				long anchorCommission = config.getMoneyOneMonth() * month;
				if (anchorCommission > 0){
					walletPriDao.updateCommission(hotChannel.getOwnerId(), anchorCommission);
					anchorMoneyFlow.setTargetCommission(anchorWallet.getCommission() + anchorCommission);
					moneyFlowPriDao.save(anchorMoneyFlow);
				}
			}
			
			HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (hotViewer != null) {
				hotViewer.setGuardLevel(channelGuard.getGuardLevel());
				hotViewer.setGuardDeadLine(channelGuard.getDeadLine());
				hotChannelViewerDao.save(hotViewer);

				TaskYxChatOpenGuard yxChatOpenGuardThread = new TaskYxChatOpenGuard(hotChannel.getYunXinRId(),
						hotViewer.getAccId(), hotViewer.getName(), hotViewer.getFaceSmallUrl(), hotViewer.getVip(),
						hotViewer.getVipDeadLine(), hotViewer.getGuardLevel(), hotViewer.getGuardDeadLine(),
						hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getFansClub(),
						hotViewer.getClubName(), hotViewer.getClubLevel(), hotViewer.getClubDeadLine(), hotViewer.isSuperUser(), jsonUtils);
				myThreadPool.getYunxinPool().execute(yxChatOpenGuardThread);
			}

			ResponseData responseData = new ResponseData();
			responseData.put("cId", channelGuard.getcId());
			responseData.put("guardLevel", channelGuard.getGuardLevel());
			responseData.put("guardDeadLine", channelGuard.getDeadLine());
			rds.setData(responseData);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("time_parse_error");
		}
		return rds;
	}

	@Override
	public ResultDataSet dailyClock(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		long today;

		try {
			today = DateUtils.getDate(new Date());
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId, today);
			if (fansClub == null || fansClub.getEndDate() < today) {
				rds.setMsg("不是粉丝团成员");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			ClubDailyClock clubDailyClock = clubDailyClockPriDao.findByCIdAndAccIdAndClockDate(cId, accId, today);
			if (clubDailyClock != null) {
				rds.setMsg("今天已打卡");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			ClubDailyClock cdc = new ClubDailyClock();
			cdc.setAccId(accId);
			cdc.setcId(cId);
			cdc.setClockDate(today);
			clubDailyClockPriDao.save(cdc);

			// 记录连续打卡次数
			ClubDailyClocksSummary clocksSummary = clubDailyClocksSummaryPriDao.findByAccIdAndCId(accId, cId);
			if (clocksSummary == null) {
				clocksSummary = new ClubDailyClocksSummary();
				clocksSummary.setAccId(accId);
				clocksSummary.setcId(cId);
				clocksSummary.setLastClockTime(today);
				clocksSummary.setCurTimes(1);
				clocksSummary.setMaxTimes(1);
			} else {
				int curTimes = clocksSummary.getCurTimes();
				int maxTimes = clocksSummary.getMaxTimes();
				long lastDay = DateUtils.addDay(today, -1);
				if (lastDay == clocksSummary.getLastClockTime()) {
					++curTimes;
					clocksSummary.setCurTimes(curTimes);
					if (curTimes > maxTimes) {
						++maxTimes;
						clocksSummary.setMaxTimes(maxTimes);
					}
				} else {
					clocksSummary.setCurTimes(1);
				}
				clocksSummary.setLastClockTime(today);
			}
			clubDailyClocksSummaryPriDao.save(clocksSummary);

			// 粉丝团任务
			clubTaskUtils.performDailyClockTask(accId, cId);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("time_parse_error");
		}
		return rds;
	}

	@Override
	public ResultDataSet dailyClockStatus(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData rp = new ResponseData();
		long today;
		try {
			today = DateUtils.getNowDate();

			ClubDailyClock clubDailyClock = clubDailyClockPriDao.findByCIdAndAccIdAndClockDate(cId, accId, today);
			if (clubDailyClock != null) {
				rp.put("status", 1);
			} else {
				rp.put("status", 0);
			}
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("");
			return rds;
		}
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getDailyClockHistory(String accId, String cId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();

		ResponseData responseData = new ResponseData();
		if (toTime < fromTime) {
			rds.setMsg("时间参数错误");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		long diff = toTime - fromTime;

		if (diff > DateUtils.monthMilliSecond * 2) {
			rds.setMsg("查询的时间过长");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		List<ClubDailyClock> clubDailyClocks = clubDailyClockSecDao.findByTime(cId, accId, fromTime, toTime);

		ClubDailyClocksSummary clubDailyClocksSummary = clubDailyClocksSummarySecDao.findByAccIdAndCId(accId, cId);

		if (clubDailyClocks != null) {
			responseData.put("history", clubDailyClocks);
			responseData.put("summary", clubDailyClocksSummary);
		}
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getDailyClockRankList(String cId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "curTimes");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ClubDailyClocksSummary> clubDailyClocksSummary = clubDailyClocksSummarySecDao.findByCId(cId, pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(clubDailyClocksSummary));
		return rds;
	}

	@Override
	public ResultDataSet getHeart(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotDailyHeart hotDailyHeart = hotDailyHeartDao.findOne(accId);
		AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
		if (accountProperty == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		if (hotDailyHeart == null) {// 没签到
			if (jedisUtils.hexist(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId)) {
				try {
					String conValue = jedisUtils.hget(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId);
					long conIntValue = Long.parseLong(conValue);
					long conDate;
					conDate = DateUtils.addDay(DateUtils.getNowDate(), -1);
					if (conIntValue / 100 * 100 == conDate) {
						int continuity = (int) (conIntValue - conIntValue / 100 * 100);
						response.put("continu", continuity);
						int count = getHeartCount(continuity, accountProperty);
						response.put("heartMax", count);
					} else {
						response.put("continu", 0);
						int count = getHeartCount(0, accountProperty);
						response.put("heartMax", count);
					}
				} catch (ParseException e) {
					logger.error("", e);
					rds.setMsg("ParseException");
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					return rds;
				}
			} else {
				response.put("continu", 0);
				int count = getHeartCount(0, accountProperty);
				response.put("heartMax", count);
			}
		} else {// 签到了
			if (jedisUtils.hexist(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId)) {
				try {
					String conValue = jedisUtils.hget(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId);
					long conIntValue = Long.parseLong(conValue);
					long conDate;
					conDate = DateUtils.getNowDate();
					if (conIntValue / 100 * 100 == conDate) {
						int continuity = (int) (conIntValue - conIntValue / 100 * 100);
						response.put("continu", continuity);
					} else {
						response.put("continu", 0);
					}
				} catch (ParseException e) {
					logger.error("", e);
					rds.setMsg("ParseException");
					rds.setCode(ResultCode.SERVICE_ERROR.getCode());
					return rds;
				}
			} else {
				response.put("continu", 0);
			}
			response.put("dailyHeart", hotDailyHeart);
			response.put("heartMax", hotDailyHeart.getHeartMax());
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel != null) {
			response.put("bright", hotChannel.getBrightness());
			response.put("HeartUserCount", hotChannel.getHeartUserCount());
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet takeHeart(String accId) {
		ResultDataSet rds = new ResultDataSet();
		if (hotDailyHeartDao.exists(accId)) {
			rds.setMsg("今天已签到");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		try {
			long nowDate = DateUtils.getNowDate();
			TakeHeartRecord thr = takeHeartRecordPriDao.findByAccIdAndTakeDate(accId, nowDate);
			if (thr != null) {
				logger.error("签到记录数据不一致");
				rds.setMsg("今天已签到");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}

			AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
			if (accountProperty == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			// [start] 连续签到
			int continuity = 0;
			long conIntValue = 0; // 最后两位数用来记录连续签到的次数，最后两位数置0后表示上次签到的日期的毫秒数
			if (jedisUtils.hexist(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId)) {
				String conValue = jedisUtils.hget(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId);
				conIntValue = Long.parseLong(conValue);
				long conDate = DateUtils.addDay(nowDate, -1);
				if (conIntValue / 100 * 100 == conDate) {
					continuity = (int) (conIntValue - conIntValue / 100 * 100);
				}
			}
			long continuityDate = nowDate + continuity + 1;
			jedisUtils.hset(Constant.HOT_DAILY_HEART_CONTINUITY_HSET, accId, Long.toString(continuityDate));
			HotDailyHeart hotDailyHeart = new HotDailyHeart();
			hotDailyHeart.setAccId(accId);
			int count = getHeartCount(continuity, accountProperty);
			hotDailyHeart.setHeart(count);
			hotDailyHeart.setHeartMax(count);
			hotDailyHeartDao.save(hotDailyHeart);
			thr = new TakeHeartRecord();
			thr.setAccId(accId);
			thr.setTakeDate(nowDate);
			takeHeartRecordPriDao.save(thr);
			rds.setData(hotDailyHeart);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("date_parse_error");
			return rds;
		}
	}

	@Override
	public ResultDataSet getHeartTakenHistory(String accId, long fromTime, long toTime) {
		ResultDataSet rds = new ResultDataSet();

		ResponseData responseData = new ResponseData();
		if (toTime < fromTime) {
			rds.setMsg("时间参数错误");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		long diff = toTime - fromTime;

		if (diff > DateUtils.monthMilliSecond * 2) {
			rds.setMsg("查询的时间不能超过60天");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		List<TakeHeartRecord> takeHeartRecords = takeHeartRecordSecDao
				.findByAccIdAndTakeDateGreaterThanAndTakeDateLessThan(accId, fromTime, toTime);
		if (takeHeartRecords != null) {
			responseData.put("history", takeHeartRecords);
		}
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	private int getHeartCount(int continuity, AccountProperty accountProperty) {
		int vip = accountProperty.getVip();
		if (accountProperty.getVipDeadline() < DateUtils.getNowTime()){
			vip = 0;
		}
		int count = 0;
		switch (continuity) {
		case 2:
			count = 10;
			break;
		case 3:
			count = 15;
			break;
		case 5:
			count = 20;
			break;
		case 7:
			count = 30;
			break;
		case 10:
			count = 50;
			break;
		case 15:
			count = 66;
			break;
		case 28:
			count = 88;
			break;
		}
		switch (vip) {
		case 0:
			count += 5;
			break;
		case 1:
			count += 20;
			break;
		case 2:
			count += 50;
			break;
		case 3:
			count += 100;
			break;
		}
		return count;
	}

	@Override
	public ResultDataSet giveHeart(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotDailyHeart hotDailyHeart = hotDailyHeartDao.findOne(accId);
		if (hotDailyHeart == null) {
			rds.setMsg("今天未签到");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotDailyHeart.getHeart() > 0) {
			hotDailyHeart.setHeart(hotDailyHeart.getHeart() - 1);
			hotDailyHeartDao.save(hotDailyHeart);
			hotChannel.setHeartCount(hotChannel.getHeartCount() + 1);
			HotChannelHeartUser hotChannelHeartUser = hotChannelHeartUserDao.findByCIdAndAccId(cId, accId);
			if (hotChannelHeartUser == null) {
				HotChannelHeartUser hchu = new HotChannelHeartUser();
				hchu.setId(StringUtils.createUUID());
				hchu.setAccId(accId);
				hchu.setcId(cId);
				hotChannelHeartUserDao.save(hchu);
				hotChannel.setHeartUserCount(hotChannel.getHeartUserCount() + 1);
			}
			HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			long bright = 100;
			if (hotViewer != null) {
				switch (hotViewer.getGuardLevel()) {
				case 1:
					bright = 110;
					break;
				case 2:
					bright = 115;
					break;
				case 3:
					bright = 120;
					break;
				}
			}
			hotChannel.setBrightness(hotChannel.getBrightness() + bright);
			hotChannelDao.save(hotChannel);
			TaskYxChatGiveHeart taskGiveHeart = new TaskYxChatGiveHeart(hotChannel.getYunXinRId(), accId, jsonUtils);
			myThreadPool.getYunxinPool().execute(taskGiveHeart);
			rds.setData(hotDailyHeart);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} else {
			rds.setMsg("您的星光不足，每天签到可领取星光，开通守护星光值加成！");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
	}

	@Override
	@Transactional
	public ResultDataSet buyMount(String accId, int mountId, String cId,String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		// String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
		// if (serverKey != null && !serverKey.isEmpty()){
		// Map<String, String> headers = new HashMap<>();
		// Map<String, String> params = new HashMap<>();
		// headers.put("accId", accId);
		//
		// params.put("mountId", new Integer(mountId).toString());
		// params.put("cId", cId);
		//
		// rds = UniqueOperationCheck.httpPost(serverKey,
		// "/channel/buyGuardMount", headers, params);
		// return rds;
		// }
		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findOne(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		HotChannelMount mount = hotChannelMountDao.findOne(mountId);
		if (mount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_MOUNT.getCode());
			return rds;
		}
		if (mount.getPrice() > wallet.getiGold()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		if (!mount.isForGuard()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该坐骑不是守护坐骑");
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(cId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有指定频道");
			return rds;
		}
		long nowDate = 0L;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		ChannelGuard guard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, nowDate);
		if (guard == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该坐骑需要在频道中开通守护");
			return rds;
		}

		long date = 0;
		try {
			date = DateUtils.getNowDate();
			if (guard.getDeadLine() < date) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该频道守护已过期");
				return rds;
			}
		} catch (ParseException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("date_error");
			return rds;
		}

		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(DateUtils.getNowTime());
		moneyFlow.setIpAddress(clientIp);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.BuyGuardMount, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
		wallet.setiGold(wallet.getiGold() - mount.getPrice());
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		MountStore newMountstore = mountStorePriDao.findByAccIdAndMountsIdAndCId(accId, mountId, cId);
		if (newMountstore == null) {
			newMountstore = new MountStore();
			newMountstore.setId(StringUtils.createUUID());
			newMountstore.setAccId(accId);
			newMountstore.setMountsId(mountId);
			newMountstore.setcId(cId);
			newMountstore.setStartTime(date);
			newMountstore.setEndTime(guard.getDeadLine());
		}
		newMountstore.setEndTime(guard.getDeadLine());
		mountStorePriDao.save(newMountstore);

		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId(channel.getChannelId());
		record.setAmount(mount.getPrice());
		record.setClientIp("");
		record.setDate(DateUtils.getNowTime());
		record.setDeadline(guard.getDeadLine());
		record.setDescribe("");
		record.setGoodsId(String.valueOf(mountId));
		record.setGoodsType(GoodsType.BUY_MOUNT.getValue());
		record.setGoodsNumber(1);
		recordConsumptionPriDao.save(record);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelConfig(int device, String giftVersion, String mountVersion,
			String animationVersion) {
		ResultDataSet rds = new ResultDataSet();
		Map<String, Serializable> response = new HashMap<String, Serializable>();
		if (!jedisUtils.get(Constant.HOT_GIFT_CONFIG_VERSION_KEY).equals(giftVersion)) {
			response.put("gift", true);
		}
		if (!jedisUtils.get(Constant.HOT_MOUNT_CONFIG_VERSION_KEY).equals(mountVersion)) {
			response.put("mount", true);
		}
		if (!jedisUtils.get(Constant.HOT_ANIMATION_CONFIG_VERSION_KEY).equals(animationVersion)) {
			response.put("animation", true);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getViewer(String cId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannelViewer viewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (viewer == null) {
			rds.setMsg("没有该观众");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(viewer);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelInfo(String cId) {
		ResultDataSet rds = new ResultDataSet();
		HashMap<String, Object> response = new HashMap<String, Object>();
		Channel channel = channelSecDao.findOne(cId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		response.put("channel", channel);
		if (!StringUtils.StringIsEmptyOrNull(channel.getOwnerId())) {
			Account account = accountSecDao.findOne(channel.getOwnerId());
			response.put("account", account);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelGuardConfig() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotChannelGuardConfig> hotChannelGuardConfig = (List<HotChannelGuardConfig>) hotChannelGuardConfigDao
				.findAll();
		if (hotChannelGuardConfig == null || hotChannelGuardConfig.isEmpty()) {
			List<ChannelGuardConfig> data = (List<ChannelGuardConfig>) channelGuardConfigSecDao.findAll();
			for (ChannelGuardConfig config : data) {
				HotChannelGuardConfig hotConfig = new HotChannelGuardConfig();
				hotConfig.setId(config.getId());
				hotConfig.setLevel(config.getLevel());
				hotConfig.setMonth(config.getMonth());
				hotConfig.setPrice(config.getPrice());
				hotChannelGuardConfigDao.save(hotConfig);
			}
			rds.setData(data);
		} else {
			rds.setData(hotChannelGuardConfig);
		}
		// set code and message
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("Server Suc");
		return rds;
	}

	@Override
	public ResultDataSet getChannelRightInfo(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotChannelRightInfo> hotChannelRightInfos = (List<HotChannelRightInfo>) hotChannelRightInfoDao.findAll();
		if (hotChannelRightInfos.size() < 5) {
			Sort sort = new Sort(Direction.fromString(PageParams.SORT_ASC), "status");
			Sort sort1 = new Sort(Direction.fromString(PageParams.SORT_DESC), "weekOffer");
			Sort sort2 = new Sort(Direction.fromString(PageParams.SORT_DESC), "brightness");
			Sort sort3 = new Sort(Direction.fromString(PageParams.SORT_DESC), "playerCount");
			Pageable pageable = new PageRequest(0, 5, sort.and(sort1).and(sort2).and(sort3));
			Page<Channel> data = channelPriDao.findByStatusNotAndTypeNotAndTypeNotAndNullityAndOwnerIdNot(0, 0, 1000, false, "",
					pageable);
			for (Channel hc : data) {
				HotChannelRightInfo channelInfo = new HotChannelRightInfo();
				channelInfo.setAction(CrossPlatformAction.NATIVE.getCode());
				channelInfo.setTarget(CrossPlatformTarget.ENTER_CHANNEL.getCode());
				channelInfo.setImgUrl(hc.getCoverUrl());
				if (StringUtils.StringIsEmptyOrNull(hc.getTitle())) {
					channelInfo.setName(hc.getChannelName());
				} else {
					channelInfo.setName(hc.getTitle());
				}
				channelInfo.setParams(hc.getcId());
				hotChannelRightInfos.add(channelInfo);
			}
		}
		rds.setData(hotChannelRightInfos);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getPlayerTimes(String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		int count = hotChannel.getPlayerCount() * 150 + (hotChannel.getPlayerTimes() - hotChannel.getPlayerCount());
		rds.setData(count);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet follow(String accId, String attAccId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		if (accId.equals(attAccId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能关注自己");
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}

		Account attAcc = accountPriDao.findOne(attAccId);
		if (attAcc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (followInfoPriDao.findByAccIdAndFansId(attAccId, accId) != null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("已关注");
			return rds;
		}
		FollowInfo followInfo = new FollowInfo();
		followInfo.setAccId(attAccId);
		followInfo.setFansId(accId);
		followInfoPriDao.save(followInfo);
		
		//默认直播时通知
		ChannelNotice channelNotice = channelNoticePriDao.findByAccIdAndCId(accId, cId);
		if (channelNotice == null) {
			ChannelNotice noticeNew = new ChannelNotice();
			noticeNew.setAccId(accId);
			noticeNew.setcId(cId);
			channelNoticePriDao.save(noticeNew);
		}
		
		if (hotChannel.getOwnerId().equals(attAccId)) {
			taskUtil.performTaskOnFollowAnchor(accId);
			HotChannelViewer viewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (viewer != null) {
				TaskYxChatFollowAnchor taskFollowAnchor = new TaskYxChatFollowAnchor(hotChannel.getYunXinRId(), accId,
						viewer.getName(), viewer.getFaceSmallUrl(), viewer.getVip(), viewer.getVipDeadLine(),
						viewer.getGuardLevel(), viewer.getGuardDeadLine(), viewer.getRichScore(), viewer.getScore(),
						viewer.getFansClub(), viewer.getClubName(), viewer.getClubLevel(), viewer.getClubDeadLine(),
						"关注了主播", viewer.isSuperUser(), jsonUtils);
				myThreadPool.getYunxinPool().execute(taskFollowAnchor);
			}
		}
		rds.setData("关注成功，快来加入粉丝团");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet selectClub(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		// long deadLine = 0;
		String clubName = "";
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		/*
		 * HotChannel hotChannel = hotChannelDao.findOne(cId); if (hotChannel ==
		 * null) { rds.setMsg(ServerCode.NO_CHANNEL.getCode());
		 * rds.setCode(ResultCode.PARAM_INVALID.getCode()); return rds; }
		 */
		if (!StringUtils.StringIsEmptyOrNull(cId)) {
			Channel channel = channelPriDao.findByCId(cId);
			if (channel == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				return rds;
			}
			long today = 0L;
			try {
				today = DateUtils.getNowDate();
			} catch (ParseException e1) {
				logger.error(e1);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("时间解析错误");
				return rds;
			}
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId, today);
			if (fansClub == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("您没有开通该频道粉丝团");
				return rds;
			}
//			try {
//				if (fansClub.getEndDate() < DateUtils.getNowDate()) {
//					rds.setCode(ResultCode.PARAM_INVALID.getCode());
//					rds.setMsg("粉丝团已过期");
//					return rds;
//				}
//			} catch (ParseException e) {
//				logger.error("", e);
//				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
//				rds.setMsg("parse_exception");
//				return rds;
//			}

			// deadLine = fansClub.getEndDate();
			clubName = channel.getClubName();
		}
		AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
		if (accountProperty != null) {
			accountProperty.setClubCid(cId);
			accountProperty.setClubName(clubName);
			accountPropertyPriDao.save(accountProperty);
		}

		rds.setData("您选择了 " + clubName);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getJoinedClubs(String accId, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(accId);
		if (acc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		long today = 0;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelFansClub> fansClubs = channelFansClubSecDao.findByAccIdAndEndDateGreaterThan(accId, 
				today, pageable);
		rds.setData(MyPageUtils.getMyPage(fansClubs));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getClubFansList(String cId, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		long nowDate = 0;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "personalScore");
		Page<ChannelFansClub> clubFans = channelFansClubSecDao.findByCIdAndEndDateGreaterThan(cId, nowDate, pageable);
		rds.setData(MyPageUtils.getMyPage(clubFans));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet isFansClubOpened(String cId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId, today);
		boolean opened = false;
		if (fansClub != null) {
//			long now = DateUtils.getNowTime();
//			if (fansClub.getEndDate() > now) {
				opened = true;
//			}
		}
		ResponseData rp = new ResponseData();
		rp.put("opened", opened);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	public ResultDataSet getClubInfoByChannel(String cId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId, today);
		if (fansClub == null) {
			rds.setData(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
//		if (fansClub != null) {
//			long now = DateUtils.getNowTime();
//			if (fansClub.getEndDate() <= now) {
//				fansClub = null;
//			}
//		}
		long todayScore = clubTaskUtils.getTodayTaskScore(accId, cId);
		ResponseData rp = new ResponseData();
		if (fansClub != null) {
			rp.put("endDate", fansClub.getEndDate());
			rp.put("personalScore", fansClub.getPersonalScore());
			rp.put("todayScore", todayScore);
		}
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getClubTaskConfig() {
		ResultDataSet rds = new ResultDataSet();
		List<HotClubTaskConfig> taskConfig = (List<HotClubTaskConfig>) hotClubTaskConfigDao.findAll();
		rds.setData(taskConfig);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyTaskDetails(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData taskCount = new ResponseData();
		HotChannel hc = hotChannelDao.findOne(cId);
		if (hc == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		long today;
		long weekStartTime;

		try {
			today = DateUtils.getNowDate();
			weekStartTime = DateUtils.getWeekStartTime();
		} catch (ParseException e) {
			rds.setMsg("parse time error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		int clockDoneCount = clubTaskRecordSecDao.findCountByCIdAndTaskIdAndEndDate(cId, 1, today);
		int giftGivenDoneCount = clubTaskRecordSecDao.findCountByCIdAndTaskIdAndEndDate(cId, 2, today);
		int shareDoneCount = clubTaskRecordSecDao.findCountByCIdAndTaskIdAndEndDateGreaterThan(cId, 3, weekStartTime);
		taskCount.put("clockDoneCount", clockDoneCount);
		taskCount.put("giftDoneCount", giftGivenDoneCount);
		taskCount.put("shareDoneCount", shareDoneCount);
		List<HotClubTaskConfig> taskConfig = (List<HotClubTaskConfig>) hotClubTaskConfigDao.findAll();
		ResponseData rp = new ResponseData();
		List<ClubTaskRecord> taskRecodes = clubTaskRecordSecDao.findByAccIdAndCIdAndCompleteDate(accId, cId, today);
		int flag = 0;
		for (ClubTaskRecord x : taskRecodes) {
			if (x.getTaskId() == 3) {
				flag = 1; // 当分享任务在今天执行时
			}
		}
		ClubTaskRecord shareTask = clubTaskRecordSecDao.findByAccIdAndCIdAndTaskIdAndCompleteDateGreaterThanEqual(accId,
				cId, 3, weekStartTime);
		if (flag == 0 && shareTask != null) {
			taskRecodes.add(shareTask);
		}
		rp.put("taskConfig", taskConfig);
		rp.put("taskRecords", taskRecodes);
		rp.put("today", today);
		rp.put("todayDoneCount", taskCount);
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;

	}

	@Override
	public ResultDataSet getUserChannelRights(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ResponseData rp = new ResponseData();
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			logger.error("", e);
		}
		ChannelGuard channelGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, today);
		if (channelGuard != null) {
			rp.put("guardLevel", channelGuard.getGuardLevel());
			rp.put("guardDeadLine", channelGuard.getDeadLine());
		}
		ChannelFansClub channelFansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, 
				accId, today);
		if (channelFansClub != null) {
			rp.put("clubName", hotChannel.getClubName());
			rp.put("clubDeadLine", channelFansClub.getEndDate());
		}
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAchorGuardList(String accId, String cId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByCId(cId);

		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		if (!channel.getOwnerId().equals(accId)) {
			rds.setMsg("请输入正确的cId");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;

		}
		long nowDate = 0;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGuard> guard = channelGuardSecDao.findByCIdAndDeadLineGreaterThan(cId, nowDate, pageable);
		List<ChannelGuard> guardList = guard.getContent();
		ResponseData result = new ResponseData();
		List<ResponseData> guardsInfo = new ArrayList<ResponseData>();
		for (ChannelGuard g : guardList) {
			ResponseData rp = new ResponseData();
			Account acc = accountSecDao.findOne(g.getAccId());
			if (acc != null) {
				String uerName = acc.getNickName();
				rp.put("userName", uerName);
				rp.put("deadLine", g.getDeadLine());
				rp.put("startLine", g.getStartLine());
				int months = DateUtils.getMonthInterval(g.getStartLine(), g.getDeadLine());
				rp.put("months", months);
				rp.put("guardLevle", g.getGuardLevel());
				guardsInfo.add(rp);
			}

		}
		result.put("content", guardsInfo);
		result.put("count", guard.getTotalElements());
		result.put("size", guard.getSize());
		result.put("pages", guard.getTotalPages());
		result.put("num", guard.getNumber());

		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;

	}

	@Override
	public ResultDataSet socailShareSuccess(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		try {
			long today = DateUtils.getNowDate();
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(
					cId, accId, today);
			if (fansClub != null) {
				clubTaskUtils.performSocialShareTask(accId, cId);
			}
		} catch (ParseException e) {
			logger.error("", e);
		}
		taskUtil.performTaskOnShareChannel(accId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelByOwner(String owner) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findByOwnerId(owner);
		if (hotChannel != null) {
			HotChannel hc = new HotChannel();
			hc.setId(hotChannel.getId());
			rds.setData(hc);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet editFansClubName(String accId, String cId, String clubName) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("非频道拥有者，不能修改粉丝团名称");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (clubName.length() != 3 || !CheckUtils.IsChinese(clubName)) {
			rds.setMsg("粉丝团名称必须是三个字符的中文");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		channel.setClubName(clubName);
		hotChannel.setClubName(clubName);
		channelPriDao.save(channel);
		hotChannelDao.save(hotChannel);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet editFansClubIcon(String accId, String cId, String clubIcon) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("非频道拥有者，不能修改粉丝团图标");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		channel.setClubIcon(clubIcon);
		hotChannel.setClubIcon(clubIcon);
		channelPriDao.save(channel);
		hotChannelDao.save(hotChannel);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelSoundConfig() {
		ResultDataSet rds = new ResultDataSet();
		List<HotChannelSound> hotChannelSounds = (List<HotChannelSound>) hotChannelSoundDao.findAll();
		if (hotChannelSounds == null || hotChannelSounds.isEmpty()) {
			List<ChannelSound> data = (List<ChannelSound>) channelSoundSecDao.findAll();
			for (ChannelSound sound : data) {
				HotChannelSound hotSound = new HotChannelSound();
				hotSound.setId(sound.getGuid());
				hotSound.setName(sound.getName());
				hotSound.setPath(sound.getPath());
				hotChannelSoundDao.save(hotSound);
			}
			rds.setData(data);
			rds.setMsg("mysql");
		} else {
			rds.setData(hotChannelSounds);
			rds.setMsg("redis");
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTopGroupByAnchor(String accId) {
		ResultDataSet rds = new ResultDataSet();
		AnchorContract anchorContract = anchorContractSecDao.findByAccIdAndStatus(accId, 
				AnchorContractStatus.AGREED.getValue());
		if (anchorContract == null){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("主播未签约");
			return rds;
		}
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(anchorContract.getgId());
		if (channelGroup == null){
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("分组不存在");
			return rds;
		}
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())){
			ChannelGroup cg = channelGroupSecDao.findByGId(channelGroup.getParentId());
			if (cg == null){
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("公会不存在");
				return rds;
			}
			rds.setData(cg);
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		rds.setData(channelGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
