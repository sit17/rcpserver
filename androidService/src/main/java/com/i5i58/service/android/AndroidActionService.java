package com.i5i58.service.android;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.android.IAndroidAction;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.clubTask.ClubTaskUtils;
import com.i5i58.config.MyThreadPool;
import com.i5i58.config.SqlserverConfig;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.HotDailyHeart;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.channel.ChGoodsType;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelAdminor;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.ChannelRecord;
import com.i5i58.data.channel.ConnectMicInfo;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelGift;
import com.i5i58.data.channel.HotChannelHeartUser;
import com.i5i58.data.channel.HotChannelMic;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.social.FollowInfo;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelRecordPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelGiftDao;
import com.i5i58.redis.all.HotChannelHeartUserDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.redis.all.HotChannelViewerDao;
import com.i5i58.redis.all.HotDailyHeartDao;
import com.i5i58.redis.all.HotSuperAdminDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.service.android.channel.async.TaskDataBaseGiveGift;
import com.i5i58.service.android.channel.async.TaskYxChatEnter;
import com.i5i58.service.android.channel.async.TaskYxChatExit;
import com.i5i58.service.android.channel.async.TaskYxChatFollowAnchor;
import com.i5i58.service.android.channel.async.TaskYxChatGift;
import com.i5i58.service.android.channel.async.TaskYxChatGiveHeart;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.ChannelUtils;
import com.i5i58.util.DataSaveThread;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JedisUtils;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.OpenIdUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.RegisterAccountResult;

@Service(protocol = "dubbo")
public class AndroidActionService implements IAndroidAction {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	JsonUtils jsonUtil;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	HotSuperAdminDao hotSuperAdminDao;

	@Autowired
	OpenIdUtils openIdUtils;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	HotChannelDao hotChannelDao;
	
	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	SqlserverConfig sqlserverConfig;

	@Autowired
	ChannelAdminorPriDao channelAdminPriDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;

	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;

	@Autowired
	HotChannelViewerDao hotChannelViewerDao;

	@Autowired
	ChannelUtils channelUtils;

	@Autowired
	HotChannelMicDao hotChannelMicDao;

	@Autowired
	MyThreadPool myThreadPool;

	@Autowired
	ClubTaskUtils clubTaskUtils;

	@Autowired
	HotChannelGiftDao hotChannelGiftDao;

	@Autowired
	ChannelRecordPriDao channelRecordPriDao;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	HotDailyHeartDao hotDailyHeartDao;

	@Autowired
	HotChannelHeartUserDao hotChannelHeartUserDao;

	@Override
	public ResultDataSet addAndroidAccount(String nickName, String gameName, byte gender, String location,
			String faceUrl) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		GameDataHelper dbHelper = new GameDataHelper(sqlserverConfig);
		String strOpenId = openIdUtils.getRandomOpenId();
		if (StringUtils.StringIsEmptyOrNull(strOpenId)) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("id exception");
			return rds;
		}
		String accid = StringUtils.createUUID();
		String tokenStr = StringUtils.createUUID();

		RegisterAccountResult registerAccountResult = null;
		try {
			registerAccountResult = YunxinIM.registerAccount(accid, strOpenId, tokenStr);
		} catch (Exception ex) {
			logger.error("", ex);
		}
		if (registerAccountResult == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("yxException:reg null");
			return rds;
		}
		if (!registerAccountResult.getCode().equals("200")) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(registerAccountResult.getString("desc"));
			return rds;
		}

		Account acc = new Account();
		acc.setOpenId(strOpenId);
		acc.setAccId(accid);
		acc.setNickName(nickName);
		acc.setFaceSmallUrl(faceUrl);
		acc.setFaceOrgUrl(faceUrl);
		acc.setFaceUseInGame(true);
		acc.setPassword("d7hfU2Fbf7eJBF5J");
		acc.setRegistDate(DateUtils.getNowTime());
		acc.setRegistIp("0.0.0.0");
		acc.setVersion(1);
		acc.setAndroid(true);
		acc.setLocation(location);
		acc.setGender(gender);
		accountPriDao.save(acc);

		AccountProperty accountProperty = new AccountProperty();
		accountProperty.setAccId(acc.getAccId());
		accountProperty.setEssayCount(0);
		accountProperty.setFansCount(0);
		accountProperty.setFocusCount(0);
		accountProperty.setMedals("");
		accountProperty.setMountsId(0);
		accountProperty.setMountsName("");
		accountProperty.setRichScore(0);
		accountProperty.setScore(0);
		accountProperty.setVip(0);
		accountProperty.setVipPurchase((byte) 0);
		accountPropertyPriDao.save(accountProperty);

		/*
		 * HotAccount hotAccount = new HotAccount(accid, strOpenId, "",
		 * strOpenId, "", false, (byte) 0, 0, "", "", 1, 0, 0, 0, 0, 0, "", "",
		 * "", 0, 0, 0, 0, "", "", "", "", Constant.ACC_TOKEN_TIME_TO_LIVE);
		 * hotAccount.setAndroid(true); hotAccountsDao.save(hotAccount);
		 */

		// jedisUtils.set(Constant.HOT_ACCOUNT_TOKEN_SET_KEY + accid, tokenStr);

		int returnValue = dbHelper.addAndroidAccount(accid, strOpenId, "android_" + accid, gameName, gender, location,
				"", "", "d7hfU2Fbf7eJBF5J");
		// if (returnValue != 0) {
		//
		// }
		if (null != acc) {
			rds.setData(acc);
			rds.setCode(ResultCode.SUCCESS.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet androidLogin(String accId, String password) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findByAccIdAndPassword(accId, password);
		if (null != acc) {
			if (acc.isNullity()) {
				rds.setCode(ResultCode.AUTH.getCode());
				rds.setMsg("账号被禁用");
				return rds;
			}
			if (!acc.isAndroid()) {
				rds.setCode(ResultCode.AUTH.getCode());
				rds.setMsg("非法登录");
				return rds;
			}
			/*
			 * AccountProperty accountProperty =
			 * accountPropertyDao.findOne(acc.getAccId()); HotAccount hotAccount
			 * = new HotAccount(acc.getAccId(), acc.getOpenId(),
			 * acc.getPhoneNo(), acc.getNickName(), acc.getStageName(),
			 * acc.isAnchor(), acc.getGender(), acc.getBirthDate(),
			 * acc.getFaceSmallUrl(), acc.getFaceOrgUrl(), acc.getVersion(),
			 * accountProperty.getVip(), accountProperty.getVipDeadline(),
			 * accountProperty.getRichScore(), accountProperty.getScore(),
			 * accountProperty.getMountsId(), accountProperty.getMountsName(),
			 * accountProperty.getClubCId(), accountProperty.getClubName(), 0,
			 * accountProperty.getFansCount(), accountProperty.getFocusCount(),
			 * accountProperty.getEssayCount(), accountProperty.getMedals(),
			 * acc.getLocation(), acc.getSignature(), acc.getPersonalBrief(),
			 * Constant.ACC_TOKEN_TIME_TO_LIVE);
			 * hotAccount.setAndroid(acc.isAndroid());
			 * hotAccountDao.save(hotAccount);
			 */
			rds.setCode(ResultCode.SUCCESS.getCode());
		} else {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("账户或密码错误");
		}
		return rds;
	}

	@Override
	public ResultDataSet getAndroidList(int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "registDate");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<Account> data = accountSecDao.findByNullityAndIsAndroid(false, true, pageable);
		rds.setData(MyPageUtils.getMyPage(data));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelList(int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<Channel> channels = channelPriDao.findByNullity(false, pageable);
		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet enterChannel(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
		if (acc == null || accountProperty == null) {
			rds.setCode(ResultCode.TOKEN_INVALID.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		HotChannel ch = hotChannelDao.findOne(cId);
		if (null == ch) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		HotChannelViewer hotViewer = new HotChannelViewer();
		hotViewer.setId(cId + "_" + accId);
		hotViewer.setcId(cId);
		hotViewer.setAccId(accId);
		hotViewer.setName(acc.getNickName());
		hotViewer.setFaceSmallUrl(acc.getFaceSmallUrl());
		hotViewer.setVip(accountProperty.getVip());
		hotViewer.setVipDeadLine(accountProperty.getVipDeadline());
		hotViewer.setRichScore(accountProperty.getRichScore());
		hotViewer.setScore(accountProperty.getScore());
		hotViewer.setAndroid(true);
		ChannelAdminor channelAdmin = channelAdminPriDao.findByCIdAndAccId(cId, accId);
		if (channelAdmin != null) {
			hotViewer.setAdminRight(channelAdmin.getAdminRight());
		} else {
			hotViewer.setAdminRight(0);
		}
		if (ch.getOwnerId().equals(acc.getAccId())) {
			hotViewer.setAdminRight(65535);
		}
		long today = 0L;
		try {
			today = DateUtils.getNowDate();
		} catch (ParseException e) {
			logger.error(e);
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}

		ChannelGuard channelGuard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, cId, today);
		if (channelGuard != null) {
			hotViewer.setMountsId(channelGuard.getMountsId());
			hotViewer.setMountsName(channelGuard.getMountsName());
			hotViewer.setGuardLevel(channelGuard.getGuardLevel());
			hotViewer.setGuardDeadLine(channelGuard.getDeadLine());
		} else {
			hotViewer.setMountsId(0);
			hotViewer.setMountsName("");
			hotViewer.setGuardLevel(0);
			hotViewer.setGuardDeadLine(0);
		}
		if (!StringUtils.StringIsEmptyOrNull(accountProperty.getClubCid())) {

			ChannelFansClub channelFansClub = channelFansClubSecDao
					.findByCIdAndAccIdAndEndDateGreaterThan(accountProperty.getClubCid(), accId, today);
			if (channelFansClub != null && channelFansClub.getEndDate() >= DateUtils.getNowTime()) {
				hotViewer.setFansClub(1);
				hotViewer.setClubName(ch.getClubName());
				hotViewer.setClubName(ch.getClubName());
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
				hotChannelMicDao.save(newMic);
			}
		}
		ch.setPlayerCount(ch.getPlayerCount() + 1);
		ch.setPlayerTimes(ch.getPlayerTimes() + 1);
		hotChannelDao.save(ch);
		// ====================================================

		Account owner = accountPriDao.findOne(ch.getOwnerId());
		Map<String, Serializable> response = new HashMap<String, Serializable>();
		response.put("channel", ch);
		response.put("owner", owner);
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
		TaskYxChatEnter taskYxChatEnter = new TaskYxChatEnter(ch.getYunXinRId(), accId, hotViewer.getName(),
				hotViewer.getFaceSmallUrl(), hotViewer.getRichScore(), hotViewer.getScore(), hotViewer.getGuardLevel(),
				hotViewer.getGuardDeadLine(), hotViewer.getVip(), hotViewer.getVipDeadLine(), hotViewer.getFansClub(),
				hotViewer.getClubName(), hotViewer.getClubDeadLine(), hotViewer.getMountsId(),
				accountProperty.getMountsId(), indexByViewer, indexByRicher);
		myThreadPool.getYunxinPool().execute(taskYxChatEnter);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet exitChannel(String accId, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (StringUtils.StringIsEmptyOrNull(cId) || StringUtils.StringIsEmptyOrNull(accId)) {
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		channelUtils.removeViewer(cId, accId);// 删除观众
		channelUtils.removeRicher(cId, accId);// 删除贵宾
		try {
			channelUtils.channelWatchingFinish(cId, accId);
			clubTaskUtils.performDailyClockTask(accId, cId);
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
					hotViewer.getFansClub(), hotViewer.getClubName(), hotViewer.getClubDeadLine());
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

	@Override
	public ResultDataSet getAndroidViewerList(int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Pageable pageable = new PageRequest(pageNum, pageSize);
		Page<HotChannelViewer> viewers = hotChannelViewerDao.findByIsAndroid(true, pageable);
		rds.setData(MyPageUtils.getMyPage(viewers));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	private boolean giftAnmiCondition(int count, int continuous, int condition) {
		if (condition == 0) {
			return true;
		}
		if (count == condition) {
			return true;
		}
		return false;
	}

	/**
	 * 获取娱乐积分对应的等级
	 * 
	 * @return
	 * @author frank
	 */
	public static int getRichScoreLevel(long richScore) {
		richScore = richScore / 100;
		if (richScore <= 10) {
			return 1;
		}
		if (richScore <= 100) {
			return 2;
		}
		if (richScore <= 200) {
			return 3;
		}
		if (richScore <= 500) {
			return 4;
		}
		if (richScore <= 800) {
			return 5;
		}
		if (richScore <= 2000) {
			return 6;
		}
		if (richScore <= 5000) {
			return 7;
		}
		if (richScore <= 10000) {
			return 8;
		}
		if (richScore <= 20000) {
			return 9;
		}
		if (richScore <= 50000) {
			return 10;
		}
		if (richScore <= 100000) {
			return 11;
		}
		if (richScore <= 200000) {
			return 12;
		}
		if (richScore <= 300000) {
			return 13;
		}
		if (richScore <= 400000) {
			return 14;
		}
		if (richScore <= 500000) {
			return 15;
		}
		if (richScore <= 600000) {
			return 16;
		}
		if (richScore <= 800000) {
			return 17;
		}
		if (richScore <= 1000000) {
			return 18;
		}
		if (richScore <= 2000000) {
			return 19;
		}
		if (richScore <= 3000000) {
			return 20;
		}
		return 20;
	}

	@Override
	public ResultDataSet giveGift(String accId, String cId, int giftId, int giftCount, int continuous)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		try {
			HotChannel hotChannel = hotChannelDao.findOne(cId);
			if (hotChannel == null) {
				rds.setMsg(ServerCode.NO_CHANNEL.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			if (StringUtils.StringIsEmptyOrNull(hotChannel.getOwnerId())) {
				rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
				rds.setCode("该频道没有主播");
				return rds;
			}
			Wallet ownerHotWallet = walletPriDao.findOne(hotChannel.getOwnerId());
			if (ownerHotWallet == null) {
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
			Account account = accountPriDao.findOne(accId);
			AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
			if (account == null || accountProperty == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}

			// HotWallet hotWallet = hotWalletDao.findOne(accId);
			// if (hotWallet == null) {
			// rds.setMsg(ServerCode.NO_WALLET.getCode());
			// rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			// return rds;
			// }
			HotChannelViewer hotChannelViewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (hotChannelViewer == null) {
				rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				return rds;
			}
			long time = DateUtils.getNowTime();
			if (hotGift.isForVip() && (accountProperty.getVip() == 0 || accountProperty.getVipDeadline() < time)) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("只有Vip才能赠送该礼物！");
				return rds;
			}
			if (hotGift.isForGuard()) {
				if (hotChannelViewer.getGuardLevel() == 0 || hotChannelViewer.getGuardDeadLine() < time) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("只有守护才能赠送该礼物！");
					return rds;
				}
			}
			long amount = hotGift.getPrice() * giftCount;
			long anchorAmount = hotGift.getAnchorPrice() * giftCount;
			// if (amount > hotWallet.getGiftTicket() && amount >
			// hotWallet.getDiamond()
			// && amount > hotWallet.getiGold()) {
			// rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			// rds.setCode(ResultCode.IGOLD_NOT_ENOUGH.getCode());
			// return rds;
			// }
			// if (hotWallet.getGiftTicket() >= amount) {
			// hotWallet.setGiftTicket(hotWallet.getGiftTicket() - amount);
			// hotWalletDao.save(hotWallet);
			// } else if (hotWallet.getDiamond() >= amount) {
			// hotWallet.setDiamond(hotWallet.getDiamond() - amount);
			// hotWalletDao.save(hotWallet);
			// ownerHotWallet.setCommission(ownerHotWallet.getCommission() +
			// anchorAmount);
			// } else if (hotWallet.getiGold() >= amount) {
			// hotWallet.setiGold(hotWallet.getiGold() - amount);
			// hotWalletDao.save(hotWallet);
			// ownerHotWallet.setCommission(ownerHotWallet.getCommission() +
			// anchorAmount);
			// }
			// hotWalletDao.save(ownerHotWallet);
			hotChannel.setWeekOffer(hotChannel.getWeekOffer() + amount);
			hotChannelDao.save(hotChannel);

			int preRichScoreLevel = getRichScoreLevel(accountProperty.getRichScore());
			accountProperty.setRichScore(accountProperty.getRichScore() + amount);
			int curRichScoreLevel = getRichScoreLevel(accountProperty.getRichScore());
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
					hotChannelViewer.getFansClub(), hotChannelViewer.getClubName(), hotChannelViewer.getClubDeadLine(),
					giftId, giftCount, continuous, condition, hotChannel.getWeekOffer(), offer, indexByViewer,
					indexByRicher, curRichScoreLevel > preRichScoreLevel, hotChannelDao, hotGift.isBroadcast());
			myThreadPool.getYunxinPool().execute(yxChatGiftThread);

			// 刷礼物mysql记录，非该业务关键数据，及时性要求较低，采用低优先级线程池处理
			ChannelRecord cgr = new ChannelRecord(cId, accId, giftId, giftCount, ChGoodsType.CHANNEL_GIFT.getValue(),
					amount, DateUtils.getNowTime(), hotGift.getName(), "127.0.0.1");
			DataSaveThread<ChannelRecord, Long> dataSaveThread = new DataSaveThread<ChannelRecord, Long>(cgr,
					channelRecordPriDao);
			TaskDataBaseGiveGift dataBaseGiveGiftThread = new TaskDataBaseGiveGift(accId, hotChannel.getOwnerId(),
					walletPriDao, amount, anchorAmount, accountPropertyPriDao, dataSaveThread);
			myThreadPool.getLowPrioritytPool().execute(dataBaseGiveGiftThread);

			clubTaskUtils.performGiftGivenTask(accId, cId);
			// rds.setData(hotWallet);
			rds.setCode(ResultCode.SUCCESS.getCode());
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
		}
		return rds;
	}

	@Override
	public ResultDataSet followInChannel(String accId, String attAccId, String cId) throws IOException {
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
		if (hotChannel.getOwnerId().equals(attAccId)) {
			HotChannelViewer viewer = hotChannelViewerDao.findOne(cId + "_" + accId);
			if (viewer != null) {
				TaskYxChatFollowAnchor taskFollowAnchor = new TaskYxChatFollowAnchor(hotChannel.getYunXinRId(), accId,
						viewer.getName(), viewer.getFaceSmallUrl(), viewer.getVip(), viewer.getVipDeadLine(),
						viewer.getGuardLevel(), viewer.getGuardDeadLine(), viewer.getRichScore(), viewer.getScore(),
						viewer.getFansClub(), viewer.getClubName(), viewer.getClubDeadLine(), "关注了主播");
				myThreadPool.getYunxinPool().execute(taskFollowAnchor);
			}
		}
		rds.setData("关注成功，快来加入粉丝团");
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet giveHeart(String accId, String cId) throws IOException {
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
			TaskYxChatGiveHeart taskGiveHeart = new TaskYxChatGiveHeart(hotChannel.getYunXinRId(), accId);
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
}
