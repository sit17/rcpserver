package com.i5i58.service.channel;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.i5i58.apis.channel.IChannelPlay;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.clubTask.ClubTaskUtils;
import com.i5i58.config.MyThreadPool;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.HotAccount;
import com.i5i58.data.account.MountStore;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.channel.ChGoodsType;
import com.i5i58.data.channel.ChStatusClk;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelAdminor;
import com.i5i58.data.channel.ChannelAuth;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.ChannelMajiaer;
import com.i5i58.data.channel.ChannelNotice;
import com.i5i58.data.channel.ChannelRecord;
import com.i5i58.data.channel.ConnectMicInfo;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelGift;
import com.i5i58.data.channel.HotChannelMajiaer;
import com.i5i58.data.channel.HotChannelMic;
import com.i5i58.data.channel.HotChannelMount;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.channel.HotConnectMicOrder;
import com.i5i58.data.channel.WSSessionCache;
import com.i5i58.data.channel.WeekOffer;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChStatusClkPriDao;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelGiftPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelMajiaerPriDao;
import com.i5i58.primary.dao.channel.ChannelMountPriDao;
import com.i5i58.primary.dao.channel.ChannelNoticePriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelRecordPriDao;
import com.i5i58.primary.dao.channel.ChannelStatus;
import com.i5i58.primary.dao.channel.WSSessionCachePriDao;
import com.i5i58.primary.dao.config.PlatformConfigPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.social.AttentionInfoPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelGiftDao;
import com.i5i58.redis.all.HotChannelMajiaerDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.redis.all.HotChannelMountDao;
import com.i5i58.redis.all.HotChannelViewerDao;
import com.i5i58.redis.all.HotConnectMicOrderDao;
import com.i5i58.secondary.dao.account.AccountConfigSecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.MountStoreSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelNoticeSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.service.channel.async.MsgYxChatIdentity;
import com.i5i58.service.channel.async.TaskChannelNotice;
import com.i5i58.service.channel.async.TaskDataBaseDriftComment;
import com.i5i58.service.channel.async.TaskDataBaseGiveGift;
import com.i5i58.service.channel.async.TaskYxChatConnMic;
import com.i5i58.service.channel.async.TaskYxChatDriftComment;
import com.i5i58.service.channel.async.TaskYxChatEnter;
import com.i5i58.service.channel.async.TaskYxChatExit;
import com.i5i58.service.channel.async.TaskYxChatGift;
import com.i5i58.service.channel.async.TaskYxChatGuardKick;
import com.i5i58.service.channel.async.TaskYxChatKick;
import com.i5i58.service.channel.async.TaskYxChatMajiaChanged;
import com.i5i58.service.channel.async.TaskYxChatMicSeqChanged;
import com.i5i58.service.channel.async.TaskYxChatSetMute;
import com.i5i58.service.channel.async.TaskYxChatSetTempMute;
import com.i5i58.service.channel.async.TaskYxOpenClosePush;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DataSaveThread;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPage;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.yunxin.CheckSumBuilder;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

import ch.qos.logback.core.status.Status;

@Service(protocol = "dubbo")
public class ChPlayService implements IChannelPlay {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	HotChannelGiftDao hotChannelGiftDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	AttentionInfoPriDao attentionInfoPriDao;

	@Autowired
	ChannelNoticePriDao channelNoticePriDao;

	@Autowired
	ChannelNoticeSecDao channelNoticeSecDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	HotChannelMicDao hotChannelMicDao;

	@Autowired
	HotChannelViewerDao hotChannelViewerDao;

	@Autowired
	ChannelRecordPriDao channelRecordPriDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	ChannelAdminorPriDao channelAdminPriDao;

	@Autowired
	EntityManager entityManager;

	// @Autowired
	// AuthVerify<ChannelAuth> channelAdminAuthVerify;

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	ChannelUtils channelUtils;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	HotConnectMicOrderDao hotConnectMicOrderDao;

	@Autowired
	ChannelGiftPriDao channelGiftPriDao;

	@Autowired
	PlatformConfigPriDao platformConfigPriDao;

	@Autowired
	HotChannelMountDao hotChannelMountDao;

	@Autowired
	ChannelMountPriDao channelMountPriDao;

	@Autowired
	HotChannelMajiaerDao hotChannelMajiaerDao;

	@Autowired
	ChannelMajiaerPriDao channelMajiaerPriDao;

	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;

	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ClubTaskUtils clubTaskUtils; // 粉丝团任务

	@Autowired
	TaskUtil taskUtil; // 平台任务

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;

	@Autowired
	AnchorUtils anchorUtils;

	@Autowired
	ChannelAdminorPriDao channelAdminorPriDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	RabbitMqBroadcastSender broadcastSender;

	@Autowired
	ConfigUtils configUtils;

	@Autowired
	AccountConfigSecDao accountConfigSecDao;

	@Autowired
	ChStatusClkPriDao chStatusClkPriDao;

	@Autowired
	WSSessionCachePriDao wsSessionCachePriDao;

	@Autowired
	MountStoreSecDao mountStoreSecDao;

	/**
	 * 送礼物
	 */
	@Override
	@Transactional
	public ResultDataSet giveChannelGift(String accId, int device, String cId, int giftId, int giftCount,
			int continuous, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		try {
			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			if (StringUtils.StringIsEmptyOrNull(hotChannel.getOwnerId())) {
				rds.setMsg("该频道没有主播");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			// 查寻主播的钱包，不加锁
			Wallet walletAnchor = walletPriDao.findOne(hotChannel.getOwnerId());
			if (walletAnchor == null) {
				rds.setMsg("该主播已经很久没上线了");
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			HotChannelGift hotGift = hotChannelGiftDao.findByMainId(giftId);
			if (hotGift == null) {
				rds.setMsg(ServerCode.NO_GIFT.getCode());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}

			AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
			Account account = accountPriDao.findOne(accId);
			if (accountProperty == null || account == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			Wallet wallet = walletPriDao.findByAccId(accId);
			if (wallet == null) {
				rds.setMsg(ServerCode.NO_WALLET.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			HotChannelViewer hotChannelViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (hotChannelViewer == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			long time = DateUtils.getNowTime();
			if (hotGift.isForVip() && (accountProperty.getVip() == 0 || accountProperty.getVipDeadline() < time)) {
				rds.setCode(ResultCode.VIP_RIGHT.getCode());
				rds.setMsg("只有Vip才能赠送该礼物！");
				return rds;
			}
			if (hotGift.isForGuard()) {
				if (hotChannelViewer.getGuardLevel() == 0 || hotChannelViewer.getGuardDeadLine() < time) {
					rds.setCode(ResultCode.GUARD_RIGHT.getCode());
					rds.setMsg("只有守护才能赠送该礼物！");
					return rds;
				}
			}
			long amount = hotGift.getPrice() * giftCount;
			long anchorAmount = hotGift.getAnchorPrice() * giftCount;
			if (amount > wallet.getGiftTicket() && amount > wallet.getRedDiamond() && amount > wallet.getDiamond()
					&& amount > wallet.getiGold()) {
				rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
				rds.setCode(ResultCode.IGOLD_NOT_ENOUGH.getCode());
				return rds;
			}
			long dateTime = DateUtils.getNowTime();
			MoneyFlow moneyFlow = new MoneyFlow();
			MoneyFlow moneyFlowAnchor = new MoneyFlow();

			moneyFlow.setAccId(accId);
			moneyFlow.setDateTime(dateTime);
			moneyFlow.setIpAddress(clientIp);
			HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.GiveGift, moneyFlow);

			moneyFlowAnchor.setAccId(hotChannel.getOwnerId());
			moneyFlowAnchor.setDateTime(dateTime);
			moneyFlowAnchor.setIpAddress(clientIp);
			HelperFunctions.setMoneyFlowSource(walletAnchor, moneyFlowAnchor);
			HelperFunctions.setMoneyFlowTarget(walletAnchor, moneyFlowAnchor);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.GiftCommission, moneyFlowAnchor);

			if (wallet.getGiftTicket() >= amount) {
				anchorAmount = 0L;
				wallet.setGiftTicket(wallet.getGiftTicket() - amount);
				walletPriDao.save(wallet);
			} else if (wallet.getRedDiamond() >= amount) {
				anchorAmount = 0L;
				wallet.setRedDiamond(wallet.getRedDiamond() - amount);
				walletPriDao.save(wallet);
			} else if (wallet.getDiamond() >= amount) {
				wallet.setDiamond(wallet.getDiamond() - amount);
				walletPriDao.save(wallet);
				walletPriDao.updateCommission(hotChannel.getOwnerId(), anchorAmount);
			} else if (wallet.getiGold() >= amount) {
				wallet.setiGold(wallet.getiGold() - amount);
				walletPriDao.save(wallet);
				walletPriDao.updateCommission(hotChannel.getOwnerId(), anchorAmount);
			}
			HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
			moneyFlowAnchor.setTargetCommission(walletAnchor.getCommission() + anchorAmount);

			hotChannel.setWeekOffer(hotChannel.getWeekOffer() + amount);
			hotChannelDao.save(hotChannel);

			int preRichScoreLevel = AccountUtils.getRichScoreLevel(accountProperty.getRichScore());
			hotChannelViewer.setRichScore(hotChannelViewer.getRichScore() + amount);
			accountProperty.setRichScore(accountProperty.getRichScore() + amount);
			int curRichScoreLevel = AccountUtils.getRichScoreLevel(accountProperty.getRichScore());
			accountPropertyPriDao.save(accountProperty);
			// 缓存周榜数据，非该业务关键数据，及时性要求较高，采用高优先级线程池处理
			long offer = channelUtils.addWeekOffer(cId, accId, amount, account.getNickName(), account.getFaceSmallUrl(),
					accountProperty.getVip(), hotChannelViewer.getGuardLevel(), hotChannelViewer.getRichScore());
			channelUtils.addViewer(cId, hotChannelViewer);
			long indexByViewer = channelUtils.getIndexInViewer(cId, hotChannelViewer);
			channelUtils.addRichman(cId, hotChannelViewer);
			long indexByRicher = channelUtils.getIndexInRicher(cId, hotChannelViewer);
			boolean condition = giftAnmiCondition(giftCount, continuous, hotGift.getCondition());
			// 云信发送聊天室刷礼物消息，用户需要尽快看见刷出的礼物，非该业务关键数据，及时性要求较高，采用云信专属线程池处理
			TaskYxChatGift yxChatGiftThread = new TaskYxChatGift(hotChannel.getYunXinRId(), hotChannelViewer.getAccId(),
					hotChannelViewer.getName(), hotChannelViewer.getFaceSmallUrl(), hotChannelViewer.getVip(),
					hotChannelViewer.getVipDeadLine(), hotChannelViewer.getGuardLevel(),
					hotChannelViewer.getGuardDeadLine(), hotChannelViewer.getRichScore(), hotChannelViewer.getScore(),
					hotChannelViewer.getFansClub(), hotChannelViewer.getClubName(), hotChannelViewer.getClubLevel(),
					hotChannelViewer.getClubDeadLine(), giftId, giftCount, continuous, condition,
					hotChannel.getWeekOffer(), offer, indexByViewer, indexByRicher,
					curRichScoreLevel > preRichScoreLevel, hotChannelDao, hotGift.isBroadcast(),
					hotChannelViewer.isSuperUser(), jsonUtil);
			myThreadPool.getYunxinPool().execute(yxChatGiftThread);

			// 刷礼物mysql记录，非该业务关键数据，及时性要求较低，采用低优先级线程池处理
			ChannelRecord cgr = new ChannelRecord(cId, accId, giftId, giftCount, ChGoodsType.CHANNEL_GIFT.getValue(),
					amount, dateTime, hotGift.getName(), clientIp);
			DataSaveThread<ChannelRecord, Long> dataSaveThread = new DataSaveThread<ChannelRecord, Long>(cgr,
					channelRecordPriDao);
			// 用户现金流记录
			DataSaveThread<MoneyFlow, Long> moneyFlowSaveThread = new DataSaveThread<MoneyFlow, Long>(moneyFlow,
					moneyFlowPriDao);
			// 主播现金流记录
			DataSaveThread<MoneyFlow, Long> moneyFlowAnchorSaveThread = new DataSaveThread<MoneyFlow, Long>(
					moneyFlowAnchor, moneyFlowPriDao);
			TaskDataBaseGiveGift dataBaseGiveGiftThread = new TaskDataBaseGiveGift(dataSaveThread, moneyFlowSaveThread,
					moneyFlowAnchorSaveThread);
			myThreadPool.getLowPrioritytPool().execute(dataBaseGiveGiftThread);
			try {
				long today = DateUtils.getNowDate();
				ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId,
						today);
				if (fansClub != null) {
					clubTaskUtils.performGiftGivenTask(accId, cId);
				}
			} catch (ParseException e) {
				logger.error("", e);
			}
			rds.setData(wallet);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	private boolean giftAnmiCondition(int count, int continuous, int condition) {
		if (condition == 0) {
			return true;
		}
		if (count >= condition) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public ResultDataSet driftComment(String accId, int device, String cId, String content, String clientIp) {
		ResultDataSet rds = new ResultDataSet();
		try {
			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}

			AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
			if (accountProperty == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			Wallet wallet = walletPriDao.findByAccId(accId);
			if (wallet == null) {
				rds.setMsg(ServerCode.NO_WALLET.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			HotChannelViewer hotChannelViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (hotChannelViewer == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			if (wallet.getiGold() < Constant.DRIFT_COMMENT_PRICE) {
				rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
				rds.setCode(ResultCode.IGOLD_NOT_ENOUGH.getCode());
				return rds;
			}
			long dateTime = DateUtils.getNowTime();
			MoneyFlow moneyFlow = new MoneyFlow();
			moneyFlow.setAccId(accId);
			moneyFlow.setDateTime(dateTime);
			moneyFlow.setIpAddress(clientIp);
			HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);
			HelperFunctions.setMoneyFlowType(MoneyFlowType.DriftComment, moneyFlow);

			wallet.setiGold(wallet.getiGold() - Constant.DRIFT_COMMENT_PRICE);
			walletPriDao.save(wallet);

			HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
			// moneyFlowPriDao.save(moneyFlow);

			int preRichScoreLevel = AccountUtils.getRichScoreLevel(accountProperty.getRichScore());
			accountProperty.setRichScore(accountProperty.getRichScore() + Constant.DRIFT_COMMENT_PRICE);
			int curRichScoreLevel = AccountUtils.getRichScoreLevel(accountProperty.getRichScore());
			accountPropertyPriDao.save(accountProperty);
			// 云信发送聊天室刷礼物消息，用户需要尽快看见刷出的礼物，非该业务关键数据，及时性要求较高，采用云信专属线程池处理
			TaskYxChatDriftComment taskYxChatDriftComment = new TaskYxChatDriftComment(hotChannel.getYunXinRId(),
					hotChannelViewer.getAccId(), hotChannelViewer.getName(), hotChannelViewer.getFaceSmallUrl(),
					hotChannelViewer.getVip(), hotChannelViewer.getVipDeadLine(), hotChannelViewer.getGuardLevel(),
					hotChannelViewer.getGuardDeadLine(), hotChannelViewer.getRichScore(), hotChannelViewer.getScore(),
					hotChannelViewer.getFansClub(), hotChannelViewer.getClubName(), hotChannelViewer.getClubLevel(),
					hotChannelViewer.getClubDeadLine(), content, curRichScoreLevel > preRichScoreLevel,
					hotChannelViewer.isSuperUser(), jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxChatDriftComment);

			// 刷弹幕mysql记录，非该业务关键数据，及时性要求较低，采用低优先级线程池处理
			ChannelRecord cgr = new ChannelRecord(cId, accId, 0, 1, ChGoodsType.CHANNEL_DRIFT.getValue(),
					Constant.DRIFT_COMMENT_PRICE, DateUtils.getNowTime(), "弹幕", clientIp);
			DataSaveThread<ChannelRecord, Long> dataSaveThread = new DataSaveThread<ChannelRecord, Long>(cgr,
					channelRecordPriDao);
			// 用户现金流记录
			DataSaveThread<MoneyFlow, Long> moneyFlowSaveThread = new DataSaveThread<MoneyFlow, Long>(moneyFlow,
					moneyFlowPriDao);
			TaskDataBaseDriftComment dataBaseDriftCommentThread = new TaskDataBaseDriftComment(dataSaveThread,
					moneyFlowSaveThread);
			myThreadPool.getLowPrioritytPool().execute(dataBaseDriftCommentThread);

			rds.setData(wallet);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 获取沙发列表
	 */
	@Override
	public ResultDataSet getSofaList(String cId, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Set<String> sofaList = channelUtils.getRicher(cId, pageNum);
		List<HotChannelViewer> sofas = new ArrayList<HotChannelViewer>();
		for (String accId : sofaList) {
			HotChannelViewer viewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (viewer != null) {
				sofas.add(viewer);
			}
		}
		rds.setData(sofas);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 获取麦序
	 */
	@Override
	public ResultDataSet getMicSequence(String cId, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		// HotChannelMic mic = new HotChannelMic();
		// mic.setcId("1");
		// mic.setAccId("111");
		// mic.setId("1_111");
		// mic.setIndexId(1);
		// mic.setName("111");mic.setSitTime(DateUtils.getNowTime());
		// hotChannelMicDao.save(mic);
		// mic.setcId("1");
		// mic.setAccId("222");
		// mic.setId("1_222");
		// mic.setIndexId(2);
		// mic.setName("222");mic.setSitTime(DateUtils.getNowTime());
		// hotChannelMicDao.save(mic);
		// mic.setcId("1");
		// mic.setAccId("333");
		// mic.setId("1_333");
		// mic.setIndexId(3);
		// mic.setName("333");mic.setSitTime(DateUtils.getNowTime());
		// hotChannelMicDao.save(mic);
		// mic.setcId("1");
		// mic.setAccId("444");
		// mic.setId("1_444");
		// mic.setIndexId(2);
		// mic.setName("444");mic.setSitTime(DateUtils.getNowTime());
		// hotChannelMicDao.save(mic);
		Sort sort = new Sort(Direction.fromString("asc"), "indexId");
		Sort sort1 = new Sort(Direction.fromString("desc"), "sitTime");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		Page<HotChannelMic> hotMic = hotChannelMicDao.findByCId(cId, pageable);
		MyPage myPage = MyPageUtils.getMyPage(hotMic);
		rds.setData(myPage);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 获取在线观众信息
	 */
	@Override
	public ResultDataSet getViewerList(String cId, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel channel = hotChannelDao.findOne(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		Set<String> viewerList = channelUtils.getViewer(cId, pageNum);
		List<HotChannelViewer> viewers = new ArrayList<HotChannelViewer>();
		for (String accId : viewerList) {
			HotChannelViewer viewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (viewer != null) {
				viewers.add(viewer);
			}
		}
		long count = channelUtils.getViewerCount(cId);
		response.put("viewer", viewers);
		response.put("count", count);
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChatRoomAddr(String roomId, String accid) {
		ResultDataSet rds = new ResultDataSet();
		try {
			YXResultSet ResultR = YunxinIM.requestChatRoomAddr(roomId, accid, "1");
			if (ResultR.getCode().equals("200")) {
				rds.setData(ResultR.getList("addr"));
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ResultR.getString("msg"));
				return rds;
			}
		} catch (IOException e) {
			rds.setMsg(e.getMessage());
			return rds;
		}
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

	/**
	 * 无登录进入直播间
	 */
	@Override
	public ResultDataSet enterChannelNoAcc(int device, String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel ch = hotChannelDao.findOne(cId);
		Channel channel = channelSecDao.findByCId(cId);
		if (null == ch || null == channel) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}

		HotAccount owner;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ ch.getOwnerId() + "'");
			owner = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			owner = null;
		}

		Map<String, Serializable> response = new HashMap<String, Serializable>();
		response.put("channel", ch);
		response.put("owner", owner);
		response.put("zegoRtmpUrl", channel.getZegoRtmpUrl());
		response.put("zegoStreamAlias", channel.getZegoStreamAlias());

		if (!StringUtils.StringIsEmptyOrNull(ch.getConnCid())) {
			HotChannel connHotChannel = hotChannelDao.findOne(ch.getConnCid());
			Account connOwner = accountPriDao.findOne(connHotChannel.getOwnerId());
			if (connHotChannel != null && connOwner != null) {
				ConnectMicInfo connMicInfo = new ConnectMicInfo();
				connMicInfo.setcId(connHotChannel.getId());
				connMicInfo.setHlsPullUrl(connHotChannel.getHlsPullUrl());
				connMicInfo.setHttpPullUrl(connHotChannel.getHttpPullUrl());
				connMicInfo.setRtmpPullUrl(connHotChannel.getRtmpPullUrl());
				connMicInfo.setFaceUrl(connOwner.getFaceSmallUrl());
				connMicInfo.setOwnerId(connOwner.getAccId());
				connMicInfo.setStageName(connOwner.getStageName());
				response.put("connMicInfo", connMicInfo);
			}
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 进入直播间
	 */
	@Override
	@Transactional
	public ResultDataSet enterChannel(String accId, int device, String cId, String sessionId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
		if (acc == null) {
			rds.setCode(ResultCode.TOKEN_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		HotChannel ch = hotChannelDao.findOne(cId);
		if (null == ch || channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}

		String cacheId = cId + "_" + accId;
		WSSessionCache sessionCache = wsSessionCachePriDao.findByCacheId(cacheId);
		if (sessionCache == null) {
			sessionCache = new WSSessionCache();
			sessionCache.setCacheId(cacheId);
		}
		sessionCache.setSessionId(sessionId);
		wsSessionCachePriDao.save(sessionCache);

		HotChannelViewer hotViewer = new HotChannelViewer();
		hotViewer.setId(cId + "_" + accId);
		hotViewer.setcId(cId);
		hotViewer.setAccId(accId);
		hotViewer.setName(acc.getNickName());
		hotViewer.setFaceSmallUrl(acc.getFaceSmallUrl());
		// hotViewer.setVip(accountProperty.getVip());
		// hotViewer.setVipDeadLine(accountProperty.getVipDeadline());
		try {
			if (DateUtils.isTimeExpire(accountProperty.getVipDeadline())) {
				hotViewer.setVip(0);
				hotViewer.setVipDeadLine(0);
			} else {
				hotViewer.setVip(accountProperty.getVip());
				hotViewer.setVipDeadLine(accountProperty.getVipDeadline());
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		hotViewer.setRichScore(accountProperty.getRichScore());
		hotViewer.setScore(accountProperty.getScore());
		hotViewer.setAndroid(false);

		if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
			hotViewer.setSuperUser(true);
		}
		ChannelAdminor channelAdmin = channelAdminPriDao.findByCIdAndAccId(cId, accId);
		if (channelAdmin != null) {
			hotViewer.setAdminRight(channelAdmin.getAdminRight());
		} else {
			hotViewer.setAdminRight(0);
		}
		if (ch.getOwnerId().equals(acc.getAccId())) {
			hotViewer.setAdminRight(65535);
		}
		long nowDate = 0;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}

		ChannelGuard channelGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, nowDate);
		try {
			if (channelGuard != null && !DateUtils.isTimeExpire(channelGuard.getDeadLine())) {
				if (channelGuard.getMountsId() != 0) {
					hotViewer.setMountsId(channelGuard.getMountsId());
					hotViewer.setMountsName(channelGuard.getMountsName());
				}
				hotViewer.setGuardLevel(channelGuard.getGuardLevel());
				hotViewer.setGuardDeadLine(channelGuard.getDeadLine());
			} else {
				hotViewer.setMountsId(0);
				hotViewer.setMountsName("");
				hotViewer.setGuardLevel(0);
				hotViewer.setGuardDeadLine(0);
			}
			if (accountProperty.getMountsId() != 0 && hotViewer.getMountsId() == 0) {
				HotChannelMount channelMount = hotChannelMountDao.findOne(accountProperty.getMountsId());
				MountStore mountStore = mountStoreSecDao.findByAccIdAndMountsId(accId, accountProperty.getMountsId());
				if (channelMount != null && mountStore != null && !DateUtils.isTimeExpire(mountStore.getEndTime())) {
					hotViewer.setMountsId(mountStore.getMountsId());
					hotViewer.setMountsName(channelMount.getName());
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!StringUtils.StringIsEmptyOrNull(accountProperty.getClubCid())) {

			ChannelFansClub channelFansClub = channelFansClubSecDao
					.findByCIdAndAccIdAndEndDateGreaterThan(accountProperty.getClubCid(), accId, nowDate);
			Channel clubChannel = null;
			if (channelFansClub != null)
				clubChannel = channelSecDao.findOne(channelFansClub.getcId());
			if (clubChannel != null && channelFansClub != null
					&& channelFansClub.getEndDate() >= DateUtils.getNowTime()) {
				hotViewer.setFansClub(1);
				hotViewer.setClubName(clubChannel.getClubName());
				hotViewer.setClubLevel(clubChannel.getClubLevel());
				hotViewer.setClubDeadLine(channelFansClub.getEndDate());
				hotViewer.setFansClubScore(channelFansClub.getPersonalScore());
			} else {
				hotViewer.setFansClub(0);
				hotViewer.setClubName("");
				hotViewer.setClubLevel(0);
				hotViewer.setClubDeadLine(0);
				hotViewer.setFansClubScore(0);
			}
		}
		hotChannelViewerDao.save(hotViewer);
		channelUtils.addViewer(cId, hotViewer);
		long indexByViewer = channelUtils.getIndexInViewer(cId, hotViewer);
		channelUtils.addRichman(cId, hotViewer);
		long indexByRicher = channelUtils.getIndexInRicher(cId, hotViewer);
		try {
			channelUtils.channelWatchingStart(cId, accId);
			taskUtil.performTaskOnEnterChannel(accId, cId);
			taskUtil.performTaskOnWatchChannel(accId);
		} catch (ParseException e) {
			logger.error("", e);
		}

		// 设置主播麦序在0位========================================
		if (hotViewer.getAccId().equals(ch.getOwnerId())) {
			HotChannelMic findMic = hotChannelMicDao.findOne(cId + "_" + accId);
			long time = DateUtils.getNowTime();
			if (findMic != null) {
				findMic.setFaceSmallUrl(hotViewer.getFaceSmallUrl());
				findMic.setGuardLevel(hotViewer.getGuardLevel());
				findMic.setGuardDeadLine(hotViewer.getGuardDeadLine());
				findMic.setIndexId(0);
				findMic.setSitTime(time);
				findMic.setName(hotViewer.getName());
				findMic.setVip(hotViewer.getVip());
				findMic.setVipDeadLine(hotViewer.getVipDeadLine());
				findMic.setRichScore(hotViewer.getRichScore());
				hotChannelMicDao.save(findMic);
			} else {
				HotChannelMic newMic = new HotChannelMic();
				newMic.setId(cId + "_" + accId);
				newMic.setAccId(accId);
				newMic.setcId(cId);
				newMic.setFaceSmallUrl(hotViewer.getFaceSmallUrl());
				newMic.setGuardLevel(hotViewer.getGuardLevel());
				newMic.setGuardDeadLine(hotViewer.getGuardDeadLine());
				newMic.setIndexId(0);
				newMic.setSitTime(time);
				newMic.setName(hotViewer.getName());
				newMic.setVip(hotViewer.getVip());
				newMic.setVipDeadLine(hotViewer.getVipDeadLine());
				newMic.setRichScore(hotViewer.getRichScore());
				newMic.setAudioRight(true);
				hotChannelMicDao.save(newMic);
			}
		}
		ch.setStatus(channel.getStatus());
		ch.setPlayerCount(ch.getPlayerCount() + 1);
		ch.setPlayerTimes(ch.getPlayerTimes() + 1);
		hotChannelDao.save(ch);
		// ====================================================

		// Account owner = accountPriDao.findOne(ch.getOwnerId());
		HotAccount owner;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ ch.getOwnerId() + "'");
			owner = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			owner = null;
		}
		Map<String, Serializable> response = new HashMap<String, Serializable>();
		response.put("channel", ch);
		response.put("owner", owner);
		response.put("zegoRtmpUrl", channel.getZegoRtmpUrl());
		response.put("zegoStreamAlias", channel.getZegoStreamAlias());

		MsgYxChatIdentity self = new MsgYxChatIdentity();
		self.setAccId(hotViewer.getAccId());
		self.setClubLevel(hotViewer.getClubLevel());
		self.setClubName(hotViewer.getClubName());
		self.setFace(hotViewer.getFaceSmallUrl());
		self.setFansClub(hotViewer.getFansClub());
		self.setFansClubDeadLine(hotViewer.getClubDeadLine());
		self.setGuard(hotViewer.getGuardLevel());
		self.setGuardDeadLine(hotViewer.getGuardDeadLine());
		self.setName(hotViewer.getName());
		self.setRichScore(hotViewer.getRichScore());
		self.setScore(hotViewer.getScore());
		self.setSuperUser(hotViewer.isSuperUser());
		self.setVip(hotViewer.getVip());
		self.setVipDeadLine(hotViewer.getVipDeadLine());
		response.put("self", self);
		if (!StringUtils.StringIsEmptyOrNull(ch.getConnCid())) {
			HotChannel connHotChannel = hotChannelDao.findOne(ch.getConnCid());
			Account connHotOwner = accountPriDao.findOne(connHotChannel.getOwnerId());
			if (connHotChannel != null && connHotOwner != null) {
				ConnectMicInfo connMicInfo = new ConnectMicInfo();
				connMicInfo.setcId(connHotChannel.getId());
				connMicInfo.setHlsPullUrl(connHotChannel.getHlsPullUrl());
				connMicInfo.setHttpPullUrl(connHotChannel.getHttpPullUrl());
				connMicInfo.setRtmpPullUrl(connHotChannel.getRtmpPullUrl());
				connMicInfo.setFaceUrl(connHotOwner.getFaceSmallUrl());
				connMicInfo.setOwnerId(connHotOwner.getAccId());
				connMicInfo.setStageName(connHotOwner.getStageName());
				response.put("connMicInfo", connMicInfo);
			}
		}
		response.put("welcome",
				"胖虎官方倡导绿色直播，对直播内容24小时在线巡查。任何传播违法、违规、低俗、暴力等不良信息将会封停账号。安全提示：聊天中若有涉及财产安全信息，一定要先核实对方身份，谨防受骗！");
		rds.setData(response);
		TaskYxChatEnter taskYxChatEnter = new TaskYxChatEnter(ch.getYunXinRId(), accId, hotViewer.getName(),
				hotViewer.getFaceSmallUrl(), hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getGuardLevel(),
				hotViewer.getGuardDeadLine(), hotViewer.getVip(), hotViewer.getVipDeadLine(), hotViewer.getFansClub(),
				hotViewer.getClubName(), hotViewer.getClubLevel(), hotViewer.getClubDeadLine(), hotViewer.getMountsId(),
				hotViewer.getMountsId(), indexByViewer, indexByRicher, hotViewer.isSuperUser(), jsonUtil);
		myThreadPool.getYunxinPool().execute(taskYxChatEnter);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet reEnterChannel(String accId, int device, String cId, String sessionId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		AccountProperty accountProperty = accountPropertyPriDao.findByAccId(accId);
		if (acc == null) {
			rds.setCode(ResultCode.TOKEN_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		HotChannel ch = hotChannelDao.findOne(cId);
		if (null == ch || channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		String cacheId = cId + "_" + accId;
		WSSessionCache sessionCache = wsSessionCachePriDao.findByCacheId(cacheId);
		if (sessionCache == null) {
			sessionCache = new WSSessionCache();
			sessionCache.setCacheId(cacheId);
		}
		sessionCache.setSessionId(sessionId);
		wsSessionCachePriDao.save(sessionCache);
		HotChannelViewer hotViewer = new HotChannelViewer();
		hotViewer.setId(cId + "_" + accId);
		hotViewer.setcId(cId);
		hotViewer.setAccId(accId);
		hotViewer.setName(acc.getNickName());
		hotViewer.setFaceSmallUrl(acc.getFaceSmallUrl());
		// hotViewer.setVip(accountProperty.getVip());
		// hotViewer.setVipDeadLine(accountProperty.getVipDeadline());
		try {
			if (DateUtils.isTimeExpire(accountProperty.getVipDeadline())) {
				hotViewer.setVip(0);
				hotViewer.setVipDeadLine(0);
			} else {
				hotViewer.setVip(accountProperty.getVip());
				hotViewer.setVipDeadLine(accountProperty.getVipDeadline());
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		hotViewer.setRichScore(accountProperty.getRichScore());
		hotViewer.setScore(accountProperty.getScore());
		hotViewer.setAndroid(false);
		if (acc.getUserRight() == Constant.USER_RIGHT_SUPER) {
			hotViewer.setSuperUser(true);
		}
		ChannelAdminor channelAdmin = channelAdminPriDao.findByCIdAndAccId(cId, accId);
		if (channelAdmin != null) {
			hotViewer.setAdminRight(channelAdmin.getAdminRight());
		} else {
			hotViewer.setAdminRight(0);
		}
		if (ch.getOwnerId().equals(acc.getAccId())) {
			hotViewer.setAdminRight(65535);
		}
		long nowDate = 0;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		ChannelGuard channelGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, nowDate);
		try {
			if (channelGuard != null && !DateUtils.isTimeExpire(channelGuard.getDeadLine())) {
				if (channelGuard.getMountsId() != 0) {
					hotViewer.setMountsId(channelGuard.getMountsId());
					hotViewer.setMountsName(channelGuard.getMountsName());
				}
				hotViewer.setGuardLevel(channelGuard.getGuardLevel());
				hotViewer.setGuardDeadLine(channelGuard.getDeadLine());
			} else {
				hotViewer.setMountsId(0);
				hotViewer.setMountsName("");
				hotViewer.setGuardLevel(0);
				hotViewer.setGuardDeadLine(0);
			}
			if (accountProperty.getMountsId() != 0 && hotViewer.getMountsId() == 0) {
				HotChannelMount channelMount = hotChannelMountDao.findOne(accountProperty.getMountsId());
				MountStore mountStore = mountStoreSecDao.findByAccIdAndMountsId(accId, accountProperty.getMountsId());
				if (channelMount != null && mountStore != null && !DateUtils.isTimeExpire(mountStore.getEndTime())) {
					hotViewer.setMountsId(mountStore.getMountsId());
					hotViewer.setMountsName(channelMount.getName());
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (!StringUtils.StringIsEmptyOrNull(accountProperty.getClubCid())) {
			ChannelFansClub channelFansClub = channelFansClubSecDao
					.findByCIdAndAccIdAndEndDateGreaterThan(accountProperty.getClubCid(), accId, nowDate);
			Channel clubChannel = null;
			if (channelFansClub != null)
				clubChannel = channelSecDao.findOne(channelFansClub.getcId());
			if (clubChannel != null && channelFansClub != null
					&& channelFansClub.getEndDate() >= DateUtils.getNowTime()) {
				hotViewer.setFansClub(1);
				hotViewer.setClubName(clubChannel.getClubName());
				hotViewer.setClubLevel(clubChannel.getClubLevel());
				hotViewer.setClubDeadLine(channelFansClub.getEndDate());
				hotViewer.setFansClubScore(channelFansClub.getPersonalScore());
			} else {
				hotViewer.setFansClub(0);
				hotViewer.setClubName("");
				hotViewer.setClubLevel(0);
				hotViewer.setClubDeadLine(0);
				hotViewer.setFansClubScore(0L);
			}
		}
		hotChannelViewerDao.save(hotViewer);
		channelUtils.addViewer(cId, hotViewer);
		channelUtils.addRichman(cId, hotViewer);

		// 设置主播麦序在0位========================================
		if (hotViewer.getAccId().equals(ch.getOwnerId())) {
			HotChannelMic findMic = hotChannelMicDao.findOne(cId + "_" + accId);
			long time = DateUtils.getNowTime();
			if (findMic != null) {
				findMic.setFaceSmallUrl(hotViewer.getFaceSmallUrl());
				findMic.setGuardLevel(hotViewer.getGuardLevel());
				findMic.setGuardDeadLine(hotViewer.getGuardDeadLine());
				findMic.setIndexId(0);
				findMic.setSitTime(time);
				findMic.setName(hotViewer.getName());
				findMic.setVip(hotViewer.getVip());
				findMic.setVipDeadLine(hotViewer.getVipDeadLine());
				findMic.setRichScore(hotViewer.getRichScore());
				hotChannelMicDao.save(findMic);
			} else {
				HotChannelMic newMic = new HotChannelMic();
				newMic.setId(cId + "_" + accId);
				newMic.setAccId(accId);
				newMic.setcId(cId);
				newMic.setFaceSmallUrl(hotViewer.getFaceSmallUrl());
				newMic.setGuardLevel(hotViewer.getGuardLevel());
				newMic.setGuardDeadLine(hotViewer.getGuardDeadLine());
				newMic.setIndexId(0);
				newMic.setSitTime(time);
				newMic.setName(hotViewer.getName());
				newMic.setVip(hotViewer.getVip());
				newMic.setVipDeadLine(hotViewer.getVipDeadLine());
				newMic.setRichScore(hotViewer.getRichScore());
				hotChannelMicDao.save(newMic);
			}
		}
		ch.setPlayerCount(ch.getPlayerCount() + 1);
		hotChannelDao.save(ch);

		try {
			channelUtils.channelWatchingStart(cId, accId);
			taskUtil.performTaskOnEnterChannel(accId, cId);
			taskUtil.performTaskOnWatchChannel(accId);
		} catch (ParseException e) {
			logger.error("", e);
		}
		// ====================================================

		HotAccount owner;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ ch.getOwnerId() + "'");
			owner = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			owner = null;
		}
		Map<String, Serializable> response = new HashMap<String, Serializable>();
		response.put("channel", ch);
		response.put("owner", owner);
		response.put("zegoRtmpUrl", channel.getZegoRtmpUrl());
		response.put("zegoStreamAlias", channel.getZegoStreamAlias());
		MsgYxChatIdentity self = new MsgYxChatIdentity();
		self.setAccId(hotViewer.getAccId());
		self.setClubLevel(hotViewer.getClubLevel());
		self.setClubName(hotViewer.getClubName());
		self.setFace(hotViewer.getFaceSmallUrl());
		self.setFansClub(hotViewer.getFansClub());
		self.setFansClubDeadLine(hotViewer.getClubDeadLine());
		self.setGuard(hotViewer.getGuardLevel());
		self.setGuardDeadLine(hotViewer.getGuardDeadLine());
		self.setName(hotViewer.getName());
		self.setRichScore(hotViewer.getRichScore());
		self.setScore(hotViewer.getScore());
		self.setSuperUser(hotViewer.isSuperUser());
		self.setVip(hotViewer.getVip());
		self.setVipDeadLine(hotViewer.getVipDeadLine());
		response.put("self", self);
		if (!StringUtils.StringIsEmptyOrNull(ch.getConnCid())) {
			HotChannel connHotChannel = hotChannelDao.findOne(ch.getConnCid());
			Account connHotOwner = accountPriDao.findOne(connHotChannel.getOwnerId());
			if (connHotChannel != null && connHotOwner != null) {
				ConnectMicInfo connMicInfo = new ConnectMicInfo();
				connMicInfo.setcId(connHotChannel.getId());
				connMicInfo.setHlsPullUrl(connHotChannel.getHlsPullUrl());
				connMicInfo.setHttpPullUrl(connHotChannel.getHttpPullUrl());
				connMicInfo.setRtmpPullUrl(connHotChannel.getRtmpPullUrl());
				connMicInfo.setFaceUrl(connHotOwner.getFaceSmallUrl());
				connMicInfo.setOwnerId(connHotOwner.getAccId());
				connMicInfo.setStageName(connHotOwner.getStageName());
				response.put("connMicInfo", connMicInfo);
			}
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 退出直播间
	 */
	@Override
	@Transactional
	public ResultDataSet exitChannel(String accId, String cId, String sessionId) {
		ResultDataSet rds = new ResultDataSet();
		if (StringUtils.StringIsEmptyOrNull(cId) || StringUtils.StringIsEmptyOrNull(accId)) {
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}

		String cacheId = cId + "_" + accId;
		WSSessionCache sessionCache = wsSessionCachePriDao.findByCacheId(cacheId);
		if (sessionCache == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("没有session缓存，不需要处理登录记录");
			return rds;
		}

		if (!sessionCache.getSessionId().equals(sessionId)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("有另外的登录途径, 退出不清理登录记录");
			return rds;
		}
		wsSessionCachePriDao.delete(sessionCache);

		channelUtils.removeViewer(cId, accId);// 删除观众
		channelUtils.removeRicher(cId, accId);// 删除贵宾
		try {
			channelUtils.channelWatchingFinish(cId, accId);

			long today = DateUtils.getNowDate();
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(cId, accId, today);
			if (fansClub != null) {
				clubTaskUtils.performDailyClockTask(accId, cId);
			}

			taskUtil.performTaskOnExitChannel(accId, cId);
		} catch (ParseException e) {
			logger.error("", e);
		}
		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotViewer != null && hotChannel != null) {
			if (hotChannel.getPlayerCount() > 0) {
				hotChannel.setPlayerCount(hotChannel.getPlayerCount() - 1);
				hotChannelDao.save(hotChannel);
			} else {
				hotChannel.setPlayerCount(0);
				hotChannelDao.save(hotChannel);
				logger.error("退出房间时频道人数小于1");
			}
			TaskYxChatExit taskYxChatExit = new TaskYxChatExit(hotChannel.getYunXinRId(), accId, hotViewer.getName(),
					hotViewer.getFaceSmallUrl(), hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getVip(),
					hotViewer.getVipDeadLine(), hotViewer.getGuardLevel(), hotViewer.getGuardDeadLine(),
					hotViewer.getFansClub(), hotViewer.getClubName(), hotViewer.getClubLevel(),
					hotViewer.getClubDeadLine(), hotViewer.isSuperUser(), jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxChatExit);
			hotChannelViewerDao.delete(hotViewer);
		}
		if (hotChannel != null && !hotChannel.getOwnerId().equals(accId)) {
			HotChannelMic hcm = hotChannelMicDao.findOne(cId + "_" + accId);
			if (hcm != null) {
				hotChannelMicDao.delete(hcm);
			}
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 踢出直播间
	 */
	@Override
	public ResultDataSet kickChannel(String admin, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		if (admin.equals(accId)) {
			rds.setMsg("不能操作本人");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(cId) || StringUtils.StringIsEmptyOrNull(accId)) {
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		if (superAdminUtils.isSuperAdmin(accId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("权限不足");
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.KICKOUT_ROOM)) {
			return guardKick(admin, accId, cId);
		}

		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer == null) {
			rds.setMsg("对方用户信息不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg("频道信息不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		// if (hotViewer != null && hotChannel != null) {
		TaskYxChatKick taskYxChatKick = new TaskYxChatKick(hotChannel.getYunXinRId(), admin, accId, hotViewer.getName(),
				hotViewer.getFaceSmallUrl(), hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getVip(),
				hotViewer.getVipDeadLine(), hotViewer.getGuardLevel(), hotViewer.getGuardDeadLine(),
				hotViewer.getFansClub(), hotViewer.getClubName(), hotViewer.getClubLevel(), hotViewer.getClubDeadLine(),
				hotViewer.isSuperUser(), jsonUtil);
		myThreadPool.getYunxinPool().execute(taskYxChatKick);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
		// }
		// rds.setMsg("频道或用户不存在");
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// return rds;

	}

	/**
	 * 守护踢人 可以踢vip等级比自己的守护等级低的用户
	 */
	public ResultDataSet guardKick(String admin, String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("时间解析错误");
			return rds;
		}

		ChannelGuard adminGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(admin, cId, today);
		ChannelGuard kickedGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, today);
		if (adminGuard == null || kickedGuard != null) {
			rds.setMsg("您没有该权限");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		// 被踢人的vip等级
		int kickedVip = accountUtils.getVip(accId);
		if (adminGuard.getGuardLevel() <= kickedVip) {
			rds.setMsg("您守护等级不够");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannelViewer guardViewer = hotChannelViewerDao.findOne(cId + "_" + admin);
		if (guardViewer == null) {
			rds.setMsg("您的用户信息不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		HotChannelViewer kickedViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (kickedViewer == null) {
			rds.setMsg("对方用户信息不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg("频道信息不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		TaskYxChatGuardKick taskYxChatGuardKick = new TaskYxChatGuardKick(hotChannel.getYunXinRId(), admin, jsonUtil);
		taskYxChatGuardKick.setGuard(guardViewer);
		taskYxChatGuardKick.setKicked(kickedViewer);
		myThreadPool.getYunxinPool().execute(taskYxChatGuardKick);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 获取关注主播个数
	 */
	@Override
	public ResultDataSet getNoticeCount(String accId) {
		ResultDataSet rds = new ResultDataSet();
		int x = 0;
		List<ChannelNotice> noticeList = channelNoticeSecDao.findByAccId(accId);
		if (noticeList != null) {
			x = noticeList.size();
		}
		rds.setData(x);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 是否发送直播通知
	 */
	@Override
	public ResultDataSet noticeLive(String accid, String cId, boolean isNotify) {
		ResultDataSet rds = new ResultDataSet();
		if (isNotify) {
			ChannelNotice channelNotice = channelNoticePriDao.findByAccIdAndCId(accid, cId);
			if (channelNotice != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("您已经关注该主播。");
				return rds;
			}
			ChannelNotice noticeNew = new ChannelNotice();
			noticeNew.setAccId(accid);
			noticeNew.setcId(cId);
			channelNoticePriDao.save(noticeNew);
			rds.setCode(ResultCode.SUCCESS.getCode());
			rds.setMsg("关注成功");
			return rds;
			// channelUtils.addNoticeLive(cId, accid);
		} else {
			// channelUtils.removeNoticeLive(cId, accid);
			ChannelNotice channelNotice = channelNoticePriDao.findByAccIdAndCId(accid, cId);
			if (channelNotice != null) {
				channelNoticePriDao.delete(channelNotice);
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setMsg("您已经取消关注该主播");
				return rds;
			}
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您没有关注过该主播");
			return rds;
		}

	}

	/**
	 * 获取直播时通知状态
	 */
	@Override
	public ResultDataSet getNoticeLiveStatus(String accid, String cId) {
		ResultDataSet rds = new ResultDataSet();
		ResponseData responseData = new ResponseData();
		// boolean isNotice = channelUtils.isNoticeLive(cId, accid);
		ChannelNotice channelNotice = channelNoticeSecDao.findByAccIdAndCId(accid, cId);
		if (channelNotice != null) {
			responseData.put("isNotice", true);
		} else {
			responseData.put("isNotice", false);
		}
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getLivingChannelNoticeMe(String accId) {
		ResultDataSet rds = new ResultDataSet();
		List<ChannelNotice> channelNotices = channelNoticeSecDao.findByAccId(accId);
		if (channelNotices == null || channelNotices.size() == 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("您没有关注任何频道");
			return rds;
		}
		List<Channel> channels = channelSecDao.findNoticeChannels(accId, ChannelStatus.OPEN.getValue());
		List<ResponseData> datas = new ArrayList<>();
		for(Channel c:channels){
			Account anchor = accountSecDao.findOne(c.getOwnerId());
			if (anchor == null) {
				continue;
			}
			ResponseData item = new ResponseData();
			item.put("cId", c.getcId());
			item.put("channelName", c.getChannelName());
			item.put("channelTitle", c.getTitle());
			item.put("yunXinRId", c.getYunXinRId());
			item.put("channelId", c.getChannelId());
			item.put("coverUrl", c.getCoverUrl());
			item.put("httpPullUrl", c.getHttpPullUrl());
			item.put("playerTimes", c.getPlayerTimes());
			item.put("playerCount", c.getPlayerCount());
			item.put("owner", c.getOwnerId());
			item.put("ownerName", anchor.getNickName());
			item.put("stageName", anchor.getStageName());
			item.put("channelOpenTime",c.getStatusChanged());
			datas.add(item);
		}
		rds.setData(datas);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
	
	/**
	 * 设置麦序
	 */
	@Override
	public ResultDataSet setMicSequence(String accId, String micAccId, String cId, int index) {
		ResultDataSet rds = new ResultDataSet();
		if (index < 1) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("index_error");
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("no_viewer_0");
			return rds;
		}
		HotChannelViewer micViewer = hotChannelViewerDao.findOne(cId + "_" + micAccId);
		if (micViewer == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("no_viewer_1");
			return rds;
		}
		// if
		// (!channelAdminAuthVerify.Verify(ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY,
		// hotViewer.getAdminRight())) {
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("没有管理员权限");
		// return rds;
		// }
		if (!channelUtils.verifyHotChannelAuth(hotChannel, accId, ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有管理员权限");
			return rds;
		}
		HotChannelMic findMic = hotChannelMicDao.findOne(cId + "_" + micAccId);
		long time = DateUtils.getNowTime();
		if (findMic != null) {
			findMic.setIndexId(index);
			findMic.setSitTime(time);
			hotChannelMicDao.save(findMic);
		} else {
			HotChannelMic newMic = new HotChannelMic();
			newMic.setId(cId + "_" + micAccId);
			newMic.setAccId(micAccId);
			newMic.setcId(cId);
			newMic.setFaceSmallUrl(micViewer.getFaceSmallUrl());
			newMic.setGuardLevel(micViewer.getGuardLevel());
			newMic.setIndexId(index);
			newMic.setSitTime(time);
			newMic.setName(micViewer.getName());
			newMic.setVip(micViewer.getVip());
			newMic.setRichScore(micViewer.getRichScore());
			hotChannelMicDao.save(newMic);
		}
		TaskYxChatMicSeqChanged TaskMicSeqChanged = new TaskYxChatMicSeqChanged(hotChannel.getYunXinRId(), micAccId,
				index, time, jsonUtil);
		myThreadPool.getYunxinPool().execute(TaskMicSeqChanged);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet removeMicSequence(String accId, String micAccId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		if (hotChannel.getOwnerId().equals(micAccId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不可移除主播");
			return rds;
		}
		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("no_viewer_0");
			return rds;
		}
		HotChannelViewer micViewer = hotChannelViewerDao.findOne(cId + "_" + micAccId);
		if (micViewer == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("no_viewer_1");
			return rds;
		}
		// if
		// (!channelAdminAuthVerify.Verify(ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY,
		// hotViewer.getAdminRight())) {
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("没有管理员权限");
		// return rds;
		// }
		if (!channelUtils.verifyHotChannelAuth(hotChannel, accId, ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有管理员权限");
			return rds;
		}
		HotChannelMic findMic = hotChannelMicDao.findOne(cId + "_" + micAccId);
		long time = DateUtils.getNowTime();
		if (findMic == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该用户不在麦序中");
			return rds;
		}
		hotChannelMicDao.delete(findMic);
		TaskYxChatMicSeqChanged TaskMicSeqChanged = new TaskYxChatMicSeqChanged(hotChannel.getYunXinRId(), micAccId, -1, // -1表示移除麦序
				time, jsonUtil);
		myThreadPool.getYunxinPool().execute(TaskMicSeqChanged);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet clearMicSequence(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("no_viewer_0");
			return rds;
		}
		// if
		// (!channelAdminAuthVerify.Verify(ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY,
		// hotViewer.getAdminRight())) {
		// rds.setCode(ResultCode.PARAM_INVALID.getCode());
		// rds.setMsg("没有管理员权限");
		// return rds;
		// }
		if (!channelUtils.verifyHotChannelAuth(hotChannel, accId, ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有管理员权限");
			return rds;
		}
		HotChannelMic hotChannelMic = hotChannelMicDao.findByCIdAndIndexId(cId, 0);
		hotChannelMicDao.deleteAll();
		if (hotChannelMic != null) {
			hotChannelMicDao.save(hotChannelMic);
		}
		TaskYxChatMicSeqChanged TaskMicSeqChanged = new TaskYxChatMicSeqChanged(hotChannel.getYunXinRId(), accId, -2, //
				// -2表示清空麦序
				DateUtils.getNowTime(), jsonUtil);
		myThreadPool.getYunxinPool().execute(TaskMicSeqChanged);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	/**
	 * 获取周贡献榜
	 */
	@Override
	public ResultDataSet getWeekOffer(String cId) {
		ResultDataSet rds = new ResultDataSet();
		List<WeekOffer> weekoffer = channelUtils.getWeekOffer(cId);
		/*
		 * List<HotChannelViewer> offer = new ArrayList<HotChannelViewer>(); for
		 * (String accId : weekoffer) { HotChannelViewer viewer =
		 * hotChannelViewerDao.findByCIdAndAccId(cId, accId); if (viewer !=
		 * null) { offer.add(viewer); } }
		 */
		rds.setData(weekoffer);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet requestConnectMic(String accId, String cId, String connCid) {
		ResultDataSet rds = new ResultDataSet();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getStatus() != ChannelStatus.OPEN.getValue()) {
			rds.setMsg("请在直播中进行连麦");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel targetHotChannel = hotChannelDao.findOne(connCid);
		if (targetHotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (targetHotChannel.getStatus() != ChannelStatus.OPEN.getValue()) {
			rds.setMsg("请在对方频道直播中进行连麦");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannelMic hotMic = hotChannelMicDao.findByCIdAndIndexId(connCid, 0);
		if (hotMic == null) {
			rds.setMsg("请在对方频道直播中进行连麦");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotConnectMicOrder hotConnectMicOrder = new HotConnectMicOrder();
		hotConnectMicOrder.setId(StringUtils.createUUID());
		hotConnectMicOrder.setRequestAccid(accId);
		hotConnectMicOrder.setRequestCId(cId);
		hotConnectMicOrder.setTargetCId(connCid);
		hotConnectMicOrder.setTargetAccid(hotMic.getAccId());
		hotConnectMicOrder.setExpire(Constant.HOT_CHANNEL_CONNECT_MIC_ORDER_EXPIRE);
		hotConnectMicOrderDao.save(hotConnectMicOrder);
		rds.setData(hotConnectMicOrder);
		rds.setCode(ResultCode.SUCCESS.getCode());
		try {
			String body = jsonUtil.toJson(hotConnectMicOrder);
			YunxinIM.sendMessage(hotConnectMicOrder.getRequestAccid(), "0", hotConnectMicOrder.getTargetAccid(), "100",
					body, "", "", "", "", "", "", "", "", false);
		} catch (IOException e) {
			logger.error("", e);
		}
		return rds;
	}

	@Override
	public ResultDataSet responseConnectMic(String accId, String connMicOrderId, boolean agree) {
		ResultDataSet rds = new ResultDataSet();
		HotConnectMicOrder hotConnectMicOrder = hotConnectMicOrderDao.findOne(connMicOrderId);
		if (hotConnectMicOrder == null) {
			rds.setMsg("已过期");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		String targetCid = hotConnectMicOrder.getTargetCId();
		String requestCid = hotConnectMicOrder.getRequestCId();
		if (agree) {
			HotChannel targetHotChannel = hotChannelDao.findOne(targetCid);
			if (targetHotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			if (targetHotChannel.getStatus() != ChannelStatus.OPEN.getValue()) {
				rds.setMsg("请在直播中进行连麦");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			HotChannel requestHotChannel = hotChannelDao.findOne(requestCid);
			if (requestHotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			if (requestHotChannel.getStatus() != ChannelStatus.OPEN.getValue()) {
				rds.setMsg("请在对方频道直播中进行连麦");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			HotChannelMic hotMic = hotChannelMicDao.findByCIdAndIndexId(requestCid, 0);
			if (hotMic == null) {
				rds.setMsg("请在对方频道直播中进行连麦");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			targetHotChannel.setConnCid(requestCid);
			hotChannelDao.save(targetHotChannel);

			requestHotChannel.setConnCid(targetCid);
			hotChannelDao.save(requestHotChannel);

			TaskYxChatConnMic yxChatConnMicThread = new TaskYxChatConnMic(accountPriDao, requestHotChannel,
					targetHotChannel, hotConnectMicOrder.getRequestAccid(), hotConnectMicOrder.getTargetAccid(),
					jsonUtil);
			myThreadPool.getYunxinPool().execute(yxChatConnMicThread);
		} else {
			hotConnectMicOrderDao.delete(hotConnectMicOrder);
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet getPushAddr(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		if (!channelUtils.checkFirstMic(accId, cId)) {
			rds.setMsg("只有第一号麦序才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("只有频道拥有者才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(hotChannel.getPushUrl());
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet openPush(String accId, String cId, int device) {
		ResultDataSet rds = new ResultDataSet();

		// Channel channel;
		// channel = channelDao.findByCId(cId);

		if (!channelUtils.checkFirstMic(accId, cId)) {
			rds.setMsg("只有第一号麦序才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getType() == 0) {
			rds.setMsg("请先分配频道类型");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("只有频道拥有者才能直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getCoverUrl().isEmpty()) {
			rds.setMsg("请先上传频道封面图");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (hotChannel.getTitle().isEmpty()) {
			rds.setMsg("请先设置频道主题");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		hotChannel.setPushDevice(device);
		hotChannel.setStatus(ChannelStatus.OPEN.getValue());
		hotChannelDao.save(hotChannel);
		// // 记录主播开播时间
		// anchorUtils.anchorStart(accId, cId);

		// TaskYxOpenClosePush taskYxOpenPush = new
		// TaskYxOpenClosePush(hotChannel.getYunXinRId(), accId, "openPush",
		// jsonUtil);
		// myThreadPool.getYunxinPool().execute(taskYxOpenPush);

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet closePush(String accId, String cId) {
		ResultDataSet rds = new ResultDataSet();
		if (!channelUtils.checkFirstMic(accId, cId)) {
			rds.setMsg("只有第一号麦序才能关闭直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!hotChannel.getOwnerId().equals(accId)) {
			rds.setMsg("只有频道拥有者才能关闭直播");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		hotChannel.setStatus(ChannelStatus.Close.getValue());
		hotChannelDao.save(hotChannel);

		// // 记录主播开播时间
		// anchorUtils.anchorClose(accId, cId);
		//
		// TaskYxOpenClosePush taskYxOpenPush = new
		// TaskYxOpenClosePush(hotChannel.getYunXinRId(), accId, "closePush",
		// jsonUtil);
		// myThreadPool.getYunxinPool().execute(taskYxOpenPush);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMajiaList(String cId, Integer pageNum, Integer pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "majia", "guardLevel");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotChannelMajiaer> hotMajiaers = hotChannelMajiaerDao.findByCId(cId, pageable);
		rds.setData(MyPageUtils.getMyPage(hotMajiaers));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addMajia(String admin, String cId, String accId, int majia) {
		ResultDataSet rds = new ResultDataSet();
		if (majia < 1 || majia > 3) {
			rds.setMsg("请选择正确的马甲");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HotChannelViewer hotViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
		if (hotViewer == null) {
			rds.setMsg("该用户不在频道内");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg("该频道不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.ASSIGN_CHANNEL_USER_HONOR_AUTHORITY)) {
			rds.setMsg("您没有该权限");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setMsg("该用户不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ChannelMajiaer channelMajiaer = channelMajiaerPriDao.findByCIdAndAccId(cId, accId);
		if (channelMajiaer != null) {
			channelMajiaer.setMajia(majia);
			channelMajiaerPriDao.save(channelMajiaer);
		} else {
			ChannelMajiaer newChannelMajiaer = new ChannelMajiaer();
			newChannelMajiaer.setAccId(accId);
			newChannelMajiaer.setcId(cId);
			newChannelMajiaer.setMajia(majia);
			channelMajiaerPriDao.save(newChannelMajiaer);
		}
		ChannelAdminor channelAdminor = channelAdminorPriDao.findByCIdAndAccId(cId, accId);
		if (majia == 1 && channelAdminor == null) {
			if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.ASSIGN_ADMIN_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			ChannelAdminor newAdminor = new ChannelAdminor();
			newAdminor.setAccId(accId);
			newAdminor.setcId(cId);
			newAdminor.setAdminRight(channelUtils.getChannelAdminPermission());
			channelAdminorPriDao.save(newAdminor);
		}
		HotChannelMajiaer hotChannelMajiaer = hotChannelMajiaerDao.findByCIdAndAccId(cId, accId);
		if (hotChannelMajiaer != null) {
			hotChannelMajiaer.setMajia(majia);
			hotChannelMajiaerDao.save(hotChannelMajiaer);
		} else {
			HotChannelMajiaer newHotChannelMajiaer = new HotChannelMajiaer();
			newHotChannelMajiaer.setAccId(accId);
			newHotChannelMajiaer.setcId(cId);
			newHotChannelMajiaer.setMajia(majia);
			newHotChannelMajiaer.setGuardLevel(hotViewer.getGuardLevel());
			hotChannelMajiaerDao.save(newHotChannelMajiaer);
		}
		TaskYxChatMajiaChanged taskYxChatMajiaChanged = new TaskYxChatMajiaChanged(channel.getYunXinRId(), accId,
				hotViewer.getName(), hotViewer.getFaceSmallUrl(), hotViewer.getRichScore(), hotViewer.getScore(),
				hotViewer.getGuardLevel(), hotViewer.getGuardDeadLine(), hotViewer.getVip(), hotViewer.getVipDeadLine(),
				hotViewer.getFansClub(), hotViewer.getClubName(), hotViewer.getClubLevel(), hotViewer.getClubDeadLine(),
				"add", majia, hotViewer.isSuperUser(), jsonUtil);
		myThreadPool.getYunxinPool().execute(taskYxChatMajiaChanged);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet removeMajia(String admin, String cId, String accId) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setMsg("该频道不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.ASSIGN_CHANNEL_USER_HONOR_AUTHORITY)) {
			rds.setMsg("您没有该权限");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setMsg("该用户不存在");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ChannelMajiaer channelMajiaer = channelMajiaerPriDao.findByCIdAndAccId(cId, accId);
		if (channelMajiaer == null) {
			rds.setMsg("该玩家不在老虎席中");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		ChannelAdminor channelAdminor = channelAdminorPriDao.findByCIdAndAccId(cId, accId);
		if (channelMajiaer.getMajia() == 1 && channelAdminor != null) {
			if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.ASSIGN_ADMIN_AUTHORITY)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("没有权限");
				return rds;
			}
			channelAdminorPriDao.delete(channelAdminor);
		}
		channelMajiaerPriDao.delete(channelMajiaer);
		HotChannelMajiaer hotChannelMajiaer = hotChannelMajiaerDao.findByCIdAndAccId(cId, accId);
		if (hotChannelMajiaer != null) {
			hotChannelMajiaerDao.delete(hotChannelMajiaer);
		}
		TaskYxChatMajiaChanged taskYxChatMajiaChanged = new TaskYxChatMajiaChanged(channel.getYunXinRId(), accId,
				account.getNickName(), "", 0, 0, 0, 0, 0, 0, 0, "", 0, 0, "rem", 0, false, jsonUtil);
		myThreadPool.getYunxinPool().execute(taskYxChatMajiaChanged);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setMute(String admin, String cId, String accId, String optValue) {
		ResultDataSet rds = new ResultDataSet();
		if (admin.equals(accId)) {
			rds.setMsg("不能操作本人");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (superAdminUtils.isSuperAdmin(accId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("权限不足");
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		Account account = accountPriDao.findOne(accId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.PROHIBIT_SPEAK)) {
			rds.setMsg("您没有该权限");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		try {
			YXResultSet yxResultSet = YunxinIM.setChatRoomMemberRole(channel.getYunXinRId(), channel.getCreatorId(),
					accId, "-2", optValue, "");
			if (!yxResultSet.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(CodeToString.getString(yxResultSet.getCode()));
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());

			TaskYxChatSetMute taskYxChatSetMute = new TaskYxChatSetMute(channel.getYunXinRId(), admin,
					account.getNickName(), optValue, jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxChatSetMute);

			return rds;

		} catch (IOException e) {
			logger.error("", e);
			rds.setMsg("设置发生异常");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet setTempMute(String admin, String cId, String accId, Long duration) {
		ResultDataSet rds = new ResultDataSet();
		if (admin.equals(accId)) {
			rds.setMsg("不能操作本人");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (superAdminUtils.isSuperAdmin(accId)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("权限不足");
			return rds;
		}
		Channel channel = channelPriDao.findByCId(cId);
		Account account = accountPriDao.findOne(accId);
		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (!channelUtils.verifyChannelAuth(channel, admin, ChannelAuth.PROHIBIT_SPEAK)) {
			rds.setMsg("您没有该权限");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		if (duration > DateUtils.dayMilliSecond / 1000) {
			rds.setMsg("最长禁言24小时");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		try {
			YXResultSet yxResultSet = YunxinIM.setChatRoomTemporaryMute(channel.getYunXinRId(), channel.getCreatorId(),
					accId, duration.toString(), "true", "");

			if (!yxResultSet.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(CodeToString.getString(yxResultSet.getCode()));
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());

			TaskYxChatSetTempMute taskYxChatSetTempMute = new TaskYxChatSetTempMute(channel.getYunXinRId(), admin,
					accId, account.getNickName(), duration, jsonUtil);
			myThreadPool.getYunxinPool().execute(taskYxChatSetTempMute);
			return rds;
		} catch (IOException e) {
			logger.error("", e);
			rds.setMsg("设置发生异常");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}

	@Override
	public ResultDataSet getChannelsByMajia(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();

		Account account = accountSecDao.findOne(accId);
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "majia", "guardLevel");

		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotChannelMajiaer> hotChannelMajiaers = hotChannelMajiaerDao.findByAccId(accId, pageable);
		ResponseData result = new ResponseData();
		result.put("majia", MyPageUtils.getMyPage(hotChannelMajiaers));
		if (channel != null) {
			result.put("anchorCId", channel.getcId());
		}
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet chStatusCallabck(String body, String sign) {
		ResultDataSet rds = new ResultDataSet();

		JSONObject object = JSON.parseObject(body);
		String cid = object.getString("cid");
		long time = object.getLongValue("time");
		int status = object.getIntValue("status");

		long arrivalTime = DateUtils.getNowTime();
		ChStatusClk chStausClk = new ChStatusClk();
		chStausClk.setArrivalTime(arrivalTime);
		chStausClk.setBody(body);
		chStausClk.setSign(sign);
		chStausClk.setYunXinCId(cid);
		chStausClk.setTime(time);
		chStausClk.setStatus(status);
		chStatusClkPriDao.save(chStausClk);

		if (sign == null || sign.equals("")) {
			logger.error("频道状态通知失败, 缺少签名字段");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("签名不存在");
			return rds;
		}
		// 获取key
		String signKey = "";
		try {
			signKey = configUtils.getPlatformConfig(Constant.VIDEO_CLOUD_SIGN_KEY);
		} catch (Exception e) {
			signKey = "vcloud";
		}
		if (object.containsKey("nId")) {
			String nId = object.getString("nId");
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			for (String key : object.keySet()) {
				Object value = object.get(key);
				parameters.put(key, value);
			}
			StringBuffer sb = new StringBuffer();
			Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (null != v && !"".equals(v)) {
					if (sb.length() > 0) {
						sb.append("&");
					}
					sb.append(k + "=" + v);
				}
			}
			sb.append(signKey);
			String checkResult = CheckSumBuilder.getMD5(sb.toString()).toUpperCase();
			if (!sign.toUpperCase().equals(checkResult)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("签名错误");
				logger.error("频道状态通知签名验证失败, body = " + body + ", sign = " + sign);
				logger.error("signKey = " + signKey);
				logger.error("checkResult = " + checkResult);
				return rds;
			}
		} else {
			String checkResult = CheckSumBuilder.getMD5(body + signKey).toUpperCase();
			if (!sign.toUpperCase().equals(checkResult)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("签名错误");
				logger.error("频道状态通知签名验证失败, body = " + body + ", sign = " + sign);
				logger.error("signKey = " + signKey);
				logger.error("checkResult = " + checkResult);
				return rds;
			}
		}

		Channel channel = channelPriDao.findByYunXinCId(cid);
		if (channel == null) {
			logger.error("频道不存在");
			rds.setMsg("频道不存在");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		// （0：空闲； 1：直播； 2：禁用； 3：直播录制）
		ChannelStatus channelStatus;
		if (status == 1) {//
			channelStatus = ChannelStatus.OPEN;
			anchorUtils.onLiveOpenCallback(channel.getOwnerId(), channel.getcId(), time);
		} else {
			channelStatus = ChannelStatus.Close;
			anchorUtils.onLiveCloseCallback(channel.getOwnerId(), channel.getcId(), time);
		}

		if (time > channel.getStatusChanged()) {
			channel.setStatus(channelStatus.getValue());
			channel.setStatusChanged(time);
			channelPriDao.save(channel);

			if (channelStatus == ChannelStatus.OPEN) {
				TaskYxOpenClosePush taskYxOpenPush = new TaskYxOpenClosePush(channel.getYunXinRId(),
						channel.getOwnerId(), "openPush", jsonUtil);
				myThreadPool.getYunxinPool().execute(taskYxOpenPush);
			} else {
				TaskYxOpenClosePush taskYxOpenPush = new TaskYxOpenClosePush(channel.getYunXinRId(),
						channel.getOwnerId(), "closePush", jsonUtil);
				myThreadPool.getYunxinPool().execute(taskYxOpenPush);
			}

			if (status == 1) {
				TaskChannelNotice taskChannelNotice = new TaskChannelNotice(time, accountSecDao, channelSecDao,
						channelNoticeSecDao, accountConfigSecDao, channel.getcId(), broadcastSender, jedisUtils);
				myThreadPool.getYunxinPool().execute(taskChannelNotice);
			}
		}

		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
