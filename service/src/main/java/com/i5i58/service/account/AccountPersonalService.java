package com.i5i58.service.account;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import com.i5i58.apis.account.IAccountPersonal;
import com.i5i58.apis.constants.ResponseData;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.HotAccount;
import com.i5i58.data.account.HotAccount1;
import com.i5i58.data.account.MajiaAccount;
import com.i5i58.data.account.MountSelectRequest;
import com.i5i58.data.account.MountStore;
import com.i5i58.data.account.Wallet;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelMount;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.record.GoodsType;
import com.i5i58.data.record.MoneyFlow;
import com.i5i58.data.record.MoneyFlowType;
import com.i5i58.data.record.RecordConsumption;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.account.MountStorePriDao;
import com.i5i58.primary.dao.account.WalletPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.primary.dao.record.MoneyFlowPriDao;
import com.i5i58.primary.dao.record.RecordConsumptionPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelMountDao;
import com.i5i58.redis.all.HotChannelViewerDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.account.MountStoreSecDao;
import com.i5i58.secondary.dao.account.WalletSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.AnchorContractSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.social.FollowInfoSecDao;
import com.i5i58.userTask.TaskUtil;
import com.i5i58.util.AccountUtils;
import com.i5i58.util.CheckUtils;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.HelperFunctions;
import com.i5i58.util.JsonUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.utils.VersionUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.RefreshTokenResult;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * @author Lee
 *
 */
@Service(protocol = "dubbo")
public class AccountPersonalService implements IAccountPersonal {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	FollowInfoSecDao followInfoSecDao;

	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;

	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;

	@Autowired
	WalletPriDao walletPriDao;

	@Autowired
	WalletSecDao walletSecDao;

	@Autowired
	MountStorePriDao mountStorePriDao;

	@Autowired
	MountStoreSecDao mountStoreSecDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	RecordConsumptionPriDao recordConsumptionPriDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;
	
	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	HotChannelMountDao hotChannelMountDao;

	@Autowired
	JsonUtils jsonUtils;

	@Autowired
	HotChannelDao hotChannelDao;

	// @Autowired
	// MajiaAccountDao majiaAccountDao;

	@Autowired
	EntityManager entityManager;

	@Autowired
	HotChannelViewerDao hotChannelViewerDao;

	@Autowired
	AccountUtils accountUtils;

	@Autowired
	ConfigUtils configUtils;

	@Autowired
	TaskUtil taskUtil;

	@Autowired
	MoneyFlowPriDao moneyFlowPriDao;
	
	@Autowired
	AnchorContractSecDao anchorContractSecDao;

	/**
	 * 修改小头像
	 */
	@Override
	public ResultDataSet setIconSmall(String accId, String iconUrl) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(iconUrl)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		account.setFaceSmallUrl(iconUrl);
		account.setFaceUseInGame(true);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setFaceSmallUrl(account.getFaceSmallUrl());
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		taskUtil.performTaskOnSetFaceImage(accId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	/**
	 * 修改高清头像
	 */
	@Override
	public ResultDataSet setIconOrg(String accId, String iconUrl) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(iconUrl)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		account.setFaceOrgUrl(iconUrl);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setFaceOrgUrl(account.getFaceOrgUrl());
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		taskUtil.performTaskOnSetFaceImage(accId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	/**
	 * 修改昵称
	 */
	@Override
	public ResultDataSet setNickName(String accId, String nickName) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if (nickName == null || "".equals(nickName)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		if (nickName.length() > 20){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("昵称不能超过20个字符");
			return rds;
		}
		try {
			YXResultSet ResultR = YunxinIM.updateUinfoAccount(account.getAccId(), nickName);
			if (ResultR.getCode().equals("200")) {
				account.setNickName(nickName);
				account.setVersion(VersionUtils.updateVersion(account.getVersion()));
				accountPriDao.save(account);
				/*
				 * HotAccount hotAcc =
				 * hotAccountDao.findOne(account.getAccId()); if (hotAcc !=
				 * null) { hotAcc.setName(account.getNickName());
				 * hotAcc.setVersion(account.getVersion());
				 * hotAccountDao.save(hotAcc); }
				 */
				System.out.println(nickName);
				rds.setCode(ResultCode.SUCCESS.getCode());
				rds.setMsg("修改成功！");
				return rds;
			} else {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ResultR.getString("msg"));
				return rds;
			}
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("server_ex");
			return rds;
		}
	}

	/**
	 * 修改艺名
	 */
	@Override
	public ResultDataSet setStageName(String accId, String stageName) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(stageName)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		account.setStageName(stageName);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setStageName(stageName);
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");

		return rds;
	}

	/**
	 * 修改性别
	 */
	@Override
	public ResultDataSet setGender(String accId, String gender) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(gender)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		try {
			account.setGender(Byte.parseByte(gender));
			account.setVersion(VersionUtils.updateVersion(account.getVersion()));
			accountPriDao.save(account);
			/*
			 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
			 * (hotAcc != null) { hotAcc.setGender(account.getGender());
			 * hotAcc.setVersion(account.getVersion());
			 * hotAccountDao.save(hotAcc); }
			 */
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("修改失败！");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");

		return rds;
	}

	@Override
	public ResultDataSet setSignature(String accId, String signature) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(signature)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		try {
			account.setSignature(signature);
			account.setVersion(VersionUtils.updateVersion(account.getVersion()));
			accountPriDao.save(account);
			/*
			 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
			 * (hotAcc != null) { hotAcc.setVersion(account.getVersion());
			 * hotAccountDao.save(hotAcc); }
			 */
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("修改失败！");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");

		return rds;
	}

	/**
	 * 修改生日
	 */
	@Override
	public ResultDataSet setBirth(String accId, long brith) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		account.setBirthDate(brith);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setBirthDate(account.getBirthDate());
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	/**
	 * 修改地址
	 */
	@Override
	public ResultDataSet setAddress(String accId, String address) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(address)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		HotChannel h = hotChannelDao.findByOwnerId(accId);
		if (h != null) {
			h.setLocation(address);
			hotChannelDao.save(h);
		}
		account.setLocation(address);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		Channel channel = channelPriDao.findByOwnerId(account.getAccId());
		if (channel != null) {
			channel.setLocation(address);
			channelPriDao.save(channel);
		}
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setLocation(address);
		 * hotAcc.setVersion(account.getVersion()); hotAccountDao.save(hotAcc);
		 * }
		 */
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	/**
	 * 修改个性签名
	 */
	@Override
	public ResultDataSet setPersonalBrief(String accId, String personalBrief) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(personalBrief)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		account.setPersonalBrief(personalBrief);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setVersion(account.getVersion());
		 * hotAccountDao.save(hotAcc); }
		 */
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	/**
	 * 获取我的个人信息
	 */
	@Override
	public ResultDataSet getMyPersonal(String accId) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountSecDao.findOne(accId);
		if (null == acc) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		if (!StringUtils.StringIsEmptyOrNull(acc.getBindMobile())) {
			acc.setBindMobile(StringUtils.addMask(acc.getBindMobile(), '*', 3, 4));
		}
		rds.setData(acc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyInfo(String accId) {
		ResultDataSet rds = new ResultDataSet();
		HotAccount htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			if (htAcc.getVip() > 0 && htAcc.getVipDeadLine() < DateUtils.getNowDate()) {
				htAcc.setVip(0);
			}
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		response.put("account", htAcc);
		String qrCode = "";
		try {
			qrCode = configUtils.getPlatformConfig(Constant.APP_SOCIAL_ACC_URL);
		} catch (Exception e) {
			rds.setMsg("No qrCode Url");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		response.put("qrCode", qrCode + htAcc.getId());
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(0, 1, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		if (guard != null && guard.size() > 0) {
			response.put("guard", guard.get(0).getGuardLevel());
			response.put("getDeadLine", guard.get(0).getDeadLine());  // 待机移除
			response.put("guardDeadLine", guard.get(0).getDeadLine());
		}
		int followCount = followInfoSecDao.countFollowByAccId(accId);
		int fansCount = followInfoSecDao.countFansByAccId(accId);
		response.put("fansCount", fansCount);
		response.put("followCount", followCount);
		do {
			long today = 0L;
			try {
				today = DateUtils.getNowDate();
			} catch (ParseException e) {
				break;
			}
			AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
			if (accountProperty == null) {
				break;
			}
			String clubCId = accountProperty.getClubCid();
			if (StringUtils.StringIsEmptyOrNull(clubCId))
				break;
			HotChannel hotChannel = hotChannelDao.findOne(clubCId);
			if (hotChannel == null) {
				break;
			}
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(clubCId, accId,
					today);
			if (fansClub == null)
				break;
			response.put("clubScore", hotChannel.getClubScore());
			response.put("clubLevel", hotChannel.getClubLevel());
			response.put("clubTitle", hotChannel.getClubTitle());
			response.put("clubName", hotChannel.getClubName());
			response.put("clubPersonalScore", fansClub.getPersonalScore());
			response.put("clubDeadLine", fansClub.getEndDate());
		} while (false);

		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel != null) {
			response.put("cId", channel.getcId());
			response.put("channelId", channel.getChannelId());
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyInfo1(String accId) {
		ResultDataSet rds = new ResultDataSet();
		HotAccount1 htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount1(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount1) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			if (htAcc.getVip() > 0 && htAcc.getVipDeadLine() < DateUtils.getNowDate()) {
				htAcc.setVip(0);
			}
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		response.put("account", htAcc);
		String qrCode = "";
		try {
			qrCode = configUtils.getPlatformConfig(Constant.APP_SOCIAL_ACC_URL);
		} catch (Exception e) {
			rds.setMsg("No qrCode Url");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		response.put("qrCode", qrCode + htAcc.getId());
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(0, 1, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		if (guard != null && guard.size() > 0) {
			response.put("guard", guard.get(0).getGuardLevel());
			response.put("getDeadLine", guard.get(0).getDeadLine()); // 待机移除
			response.put("guardDeadLine", guard.get(0).getDeadLine());
		}
		int followCount = followInfoSecDao.countFollowByAccId(accId);
		int fansCount = followInfoSecDao.countFansByAccId(accId);
		response.put("fansCount", fansCount);
		response.put("followCount", followCount);
		do {
			long today = 0L;
			try {
				today = DateUtils.getNowDate();
			} catch (ParseException e) {
				break;
			}
			AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
			if (accountProperty == null) {
				break;
			}
			String clubCId = accountProperty.getClubCid();
			if (StringUtils.StringIsEmptyOrNull(clubCId))
				break;
			HotChannel hotChannel = hotChannelDao.findOne(clubCId);
			if (hotChannel == null) {
				break;
			}
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(clubCId, accId,
					today);
			if (fansClub == null)
				break;
			response.put("clubScore", hotChannel.getClubScore());
			response.put("clubLevel", hotChannel.getClubLevel());
			response.put("clubTitle", hotChannel.getClubTitle());
			response.put("clubName", hotChannel.getClubName());
			response.put("clubPersonalScore", fansClub.getPersonalScore());
			response.put("clubDeadLine", fansClub.getEndDate());
		} while (false);
		
		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel != null) {
			response.put("cId", channel.getcId());
			response.put("channelId", channel.getChannelId());
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTAInfo(String accId) {
		ResultDataSet rds = new ResultDataSet();
		HotAccount htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			if (htAcc.getVip() > 0 && htAcc.getVipDeadLine() < DateUtils.getNowDate()) {
				htAcc.setVip(0);
			}
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		response.put("account", htAcc);
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(0, 1, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		if (guard != null && guard.size() > 0) {
			response.put("guard", guard.get(0).getGuardLevel());
			response.put("guardDeadLine", guard.get(0).getDeadLine());
		}
		int followCount = followInfoSecDao.countFollowByAccId(accId);
		int fansCount = followInfoSecDao.countFansByAccId(accId);
		response.put("fansCount", fansCount);
		response.put("followCount", followCount);

		do {
			long today = 0L;
			try {
				today = DateUtils.getNowDate();
			} catch (ParseException e) {
				break;
			}
			AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
			if (accountProperty == null) {
				break;
			}
			String clubCId = accountProperty.getClubCid();
			if (StringUtils.StringIsEmptyOrNull(clubCId))
				break;
			HotChannel hotChannel = hotChannelDao.findOne(clubCId);
			if (hotChannel == null) {
				break;
			}
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(clubCId, accId,
					today);
			if (fansClub == null)
				break;
			response.put("clubScore", hotChannel.getClubScore());
			response.put("clubLevel", hotChannel.getClubLevel());
			response.put("clubTitle", hotChannel.getClubTitle());
			response.put("clubName", hotChannel.getClubName());
			response.put("clubPersonalScore", fansClub.getPersonalScore());
			response.put("clubDeadLine", fansClub.getEndDate());
		} while (false);

		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel != null) {
			response.put("cId", channel.getcId());
			response.put("channelId", channel.getChannelId());
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getTAInfo1(String accId) {
		ResultDataSet rds = new ResultDataSet();
		HotAccount1 htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount1(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount1) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		try {
			if (htAcc.getVip() > 0 && htAcc.getVipDeadLine() < DateUtils.getNowDate()) {
				htAcc.setVip(0);
			}
		} catch (ParseException e1) {
			logger.error("", e1);
		}
		response.put("account", htAcc);
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(0, 1, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		if (guard != null && guard.size() > 0) {
			response.put("guard", guard.get(0).getGuardLevel());
			response.put("guardDeadLine", guard.get(0).getDeadLine());
		}
		int followCount = followInfoSecDao.countFollowByAccId(accId);
		int fansCount = followInfoSecDao.countFansByAccId(accId);
		response.put("fansCount", fansCount);
		response.put("followCount", followCount);

		do {
			long today = 0L;
			try {
				today = DateUtils.getNowDate();
			} catch (ParseException e) {
				break;
			}
			AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
			if (accountProperty == null) {
				break;
			}
			String clubCId = accountProperty.getClubCid();
			if (StringUtils.StringIsEmptyOrNull(clubCId))
				break;
			HotChannel hotChannel = hotChannelDao.findOne(clubCId);
			if (hotChannel == null) {
				break;
			}
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(clubCId, accId,
					today);
			if (fansClub == null)
				break;
			response.put("clubScore", hotChannel.getClubScore());
			response.put("clubLevel", hotChannel.getClubLevel());
			response.put("clubTitle", hotChannel.getClubTitle());
			response.put("clubName", hotChannel.getClubName());
			response.put("clubPersonalScore", fansClub.getPersonalScore());
			response.put("clubDeadLine", fansClub.getEndDate());
		} while (false);

		Channel channel = channelSecDao.findByOwnerId(accId);
		if (channel != null) {
			response.put("cId", channel.getcId());
			response.put("channelId", channel.getChannelId());
		}
		List<AnchorContract> contract=anchorContractSecDao.findByAccId(accId);
		for(AnchorContract con:contract){
			if(con.getStatus()!=AnchorContractStatus.AGREED.getValue()){
			response.put("groupName",null);
			}else{
			ChannelGroup group=channelGroupSecDao.findByGId(con.getgId());
			response.put("groupName",group.getName());
			}
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet selectMount(String accId, String selectJson) {
		ResultDataSet rds = new ResultDataSet();
		Account acc = accountPriDao.findOne(accId);
		if (acc == null) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			return rds;
		}
		MountSelectRequest request;
		int mountId = 0;
		String mountName = "";

		try {
			request = jsonUtils.toObject(selectJson, MountSelectRequest.class);
		} catch (IOException e) {
			logger.error("", e);
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("json_error");
			return rds;
		}

		mountId = request.getFansVipNorMount();
		if (mountId != 0) {
			HotChannelMount chMount = hotChannelMountDao.findOne(request.getFansVipNorMount());
			if (chMount == null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg(ServerCode.NO_MOUNT.getCode());
				return rds;
			}
			if (chMount.isForGuard()) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("不能使用守护坐骑");
				return rds;
			}
			// 验证坐骑是否已经购买
			MountStore mountStore = mountStoreSecDao.findByAccIdAndMountsId(accId, mountId);
			if (mountStore == null) {
				rds.setMsg("无法使用未购买的坐骑");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}

			mountName = chMount.getName();
		}

		AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
		accountProperty.setMountsId(mountId);
		accountProperty.setMountsName(mountName);
		accountPropertyPriDao.save(accountProperty);
		/*
		 * hotAcc.setMountsId(mountId); hotAcc.setMountsName(mountName);
		 * hotAccountDao.save(hotAcc);
		 */
		long nowDate = 0L;
		try {
			nowDate = DateUtils.getNowDate();
		} catch (ParseException e) {
			rds.setMsg("parse error");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
		// 下面是守护坐骑
		for (Entry<String, Integer> mount : request.getGuardMounts().entrySet()) {
			ChannelGuard guard = channelGuardSecDao.findByAccIdAndCIdAndDeadLineGreaterThan(accId, mount.getKey(),
					nowDate);
			if (guard == null) {
				rds.setMsg("频道守护未开通");
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				return rds;
			}
			int guardMountId = mount.getValue();
			String gName = "";
			if (guardMountId != 0) {
				HotChannelMount channelMount = hotChannelMountDao.findOne(guardMountId);
				if (channelMount == null) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg(ServerCode.NO_MOUNT.getCode());
					return rds;
				}
				if (!channelMount.isForGuard()) {
					rds.setMsg("只能使用守护专用坐骑");
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
				}
				// 验证坐骑是否已经购买
				MountStore mntStore = mountStoreSecDao.findByAccIdAndMountsIdAndCId(accId, guardMountId,
						mount.getKey());
				if (mntStore == null) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("无法使用未购买的坐骑");
					return rds;
				}
				gName = channelMount.getName();
			}

			guard.setMountsId(guardMountId);
			guard.setMountsName(gName);
			channelGuardPriDao.save(guard);
			HotChannelViewer hotChViewer = hotChannelViewerDao.findOne(mount.getKey() + "_" + accId);
			if (hotChViewer != null) {
				hotChViewer.setMountsId(guardMountId);
				hotChViewer.setMountsName(gName);
				hotChannelViewerDao.save(hotChViewer);
			}
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	@Transactional
	public ResultDataSet buyMount(String accId, int mountId, String clientIP, int month) {
		ResultDataSet rds = new ResultDataSet();
		// String serverKey = UniqueOperationCheck.getLoggedInServerKey(accId);
		// if (serverKey != null && !serverKey.isEmpty()){
		// Map<String, String> headers = new HashMap<>();
		// Map<String, String> params = new HashMap<>();
		// headers.put("accId", accId);
		//
		// params.put("mountId", new Integer(mountId).toString());
		// params.put("clientIP", clientIP);
		// params.put("month", new Integer(month).toString());
		//
		// rds = UniqueOperationCheck.httpPost(serverKey, "/account/buyMount",
		// headers, params);
		// return rds;
		// }

		Wallet wallet = walletPriDao.findByAccId(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		HotChannelMount mount = hotChannelMountDao.findOne(mountId);
		if (mount == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_MOUNT.getCode());
			return rds;
		}

		// 验证坐骑是否是vip专属
		long vipDeadLine = -1;
		if (mount.isForVip()) {
			try {
				long nowDate = DateUtils.getNowDate();
				AccountProperty accountProperty = accountPropertyPriDao.findOne(accId);
				if (accountProperty == null) {
					rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					return rds;
				}
				if (accountProperty.getVip() == 0) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("非vip用户不能购买该坐骑");
					return rds;
				}
				if (accountProperty.getVipDeadline() < nowDate) {
					rds.setCode(ResultCode.PARAM_INVALID.getCode());
					rds.setMsg("vip已到期");
					return rds;
				}
				vipDeadLine = accountProperty.getVipDeadline();
			} catch (ParseException e) {
				logger.error("", e);
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("date_error");
				return rds;
			}
		}
		// 当前接口不能用来购买守护坐骑
		if (mount.isForGuard()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该坐骑需要开通频道守护");
			return rds;
		}
		if (mount.getPrice() > wallet.getiGold()) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.IGOLD_NOT_ENOUGH.getCode());
			return rds;
		}
		long dateTime = DateUtils.getNowTime();
		MoneyFlow moneyFlow = new MoneyFlow();
		moneyFlow.setAccId(accId);
		moneyFlow.setDateTime(dateTime);
		moneyFlow.setIpAddress(clientIP);
		HelperFunctions.setMoneyFlowType(MoneyFlowType.BuyMount, moneyFlow);
		HelperFunctions.setMoneyFlowSource(wallet, moneyFlow);

		wallet.setiGold(wallet.getiGold() - mount.getPrice());
		walletPriDao.save(wallet);

		HelperFunctions.setMoneyFlowTarget(wallet, moneyFlow);
		moneyFlowPriDao.save(moneyFlow);

		double price = month * mount.getPrice();
		// int addDate = mount.getValidity() * month;
		long date = 0;
		long deadline = 0;

		MountStore mountStore = mountStorePriDao.findByAccIdAndMountsId(accId, mountId);

		try {
			date = DateUtils.getDate(new Date());

			if (mountStore != null) {
				if (vipDeadLine > 0) {
					deadline = vipDeadLine;
				} else {
					if (mountStore.getEndTime() >= date) {
						deadline = DateUtils.AddMonth(mountStore.getEndTime(), month);
					} else {
						deadline = DateUtils.AddMonth(date, month);
					}
				}
				mountStore.setEndTime(deadline);
				mountStorePriDao.save(mountStore);
			} else {
				if (vipDeadLine > 0) {
					deadline = vipDeadLine;
				} else {
					deadline = DateUtils.AddMonth(date, month);
				}
				MountStore newMountstore = new MountStore();
				newMountstore.setId(StringUtils.createUUID());
				newMountstore.setAccId(accId);
				newMountstore.setMountsId(mountId);
				newMountstore.setStartTime(date);
				newMountstore.setEndTime(deadline);
				mountStorePriDao.save(newMountstore);
			}
		} catch (ParseException e) {
			logger.error("", e);
		}
		RecordConsumption record = new RecordConsumption();
		record.setId(StringUtils.createUUID());
		record.setAccId(accId);
		record.setChannelId("");
		record.setAmount(price);
		record.setClientIp(clientIP);
		record.setDate(dateTime);
		record.setDeadline(deadline);
		record.setDescribe("");
		record.setGoodsId(String.valueOf(mountId));
		record.setGoodsType(GoodsType.BUY_MOUNT.getValue());
		record.setGoodsNumber(1);

		recordConsumptionPriDao.save(record);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getWallet(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Wallet wallet = walletSecDao.findOne(accId);
		if (wallet == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_WALLET.getCode());
			return rds;
		}
		rds.setData(wallet);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyMount(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "id");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<MountStore> mountStore = mountStoreSecDao.findByAccId(accId, pageable);
		rds.setData(MyPageUtils.getMyPage(mountStore));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setMyInfo(String accId, String nickName, long brith, String address, byte gender,
			String location, String signature, String personalBrief, String stageName) {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		if ("".equals(brith)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不能为空");
			return rds;
		}
		if (nickName != null && nickName.length() > 20){
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("昵称不能超过20个字符");
			return rds;
		}
		account.setNickName(nickName);
		account.setBirthDate(brith);
		account.setGender(gender);
		account.setPersonalBrief(personalBrief);
		account.setLocation(location);
		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		account.setSignature(signature);
		account.setPersonalBrief(personalBrief);
		accountPriDao.save(account);

		Channel channel = channelPriDao.findByOwnerId(account.getAccId());
		if (channel != null) {
			channel.setLocation(location);
			channelPriDao.save(channel);
		}
		/*
		 * HotAccount hotAcc = hotAccountDao.findOne(account.getAccId()); if
		 * (hotAcc != null) { hotAcc.setName(account.getNickName());
		 * hotAcc.setBirthDate(account.getBirthDate());
		 * hotAcc.setLocation(account.getLocation());
		 * hotAcc.setGender(account.getGender());
		 * hotAcc.setSignature(signature);
		 * hotAcc.setPersonalBrief(personalBrief); hotAccountDao.save(hotAcc); }
		 */
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	@Override
	public ResultDataSet getMyAccount(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		HotAccount htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(htAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyAccount1(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		HotAccount1 htAcc;
		try {
			Query htAccQuery = entityManager.createQuery(
					"SELECT new com.i5i58.data.account.HotAccount1(c.accId, c.openId, c.phoneNo,c.nickName, c.stageName, c.anchor, c.gender, c.birthDate, c.faceSmallUrl, c.faceOrgUrl , c.version ,  g.vip , g.vipDeadline, g.richScore, g.score, g.mountsId, g.mountsName,g.clubCid, g.clubName,g.fansCount, g.focusCount, g.essayCount ,  g.medals,  c.location, c.signature, c.personalBrief) FROM Account c ,AccountProperty g WHERE c.accId= g.accId AND c.accId='"
							+ accId + "'");
			htAcc = (HotAccount1) htAccQuery.getSingleResult();
		} catch (Exception e) {
			htAcc = null;
		}
		if (htAcc == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		rds.setData(htAcc);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMyGuard(String accId, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (pageNum < 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("PageNum must not be less than zero!");
			return rds;
		}
		if (pageSize < 1) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("PageSize must not be less than one!");
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		List<ChannelGuard> guard1 = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime());
		int count = guard1.size();
		ResponseData result = new ResponseData();
		List<ResponseData> guardsInfo = new ArrayList<ResponseData>();
		for (ChannelGuard g : guard) {
			ResponseData rp = new ResponseData();
			rp.put("accId", g.getAccId());
			rp.put("guardLevel", g.getGuardLevel());
			rp.put("deadLine", g.getDeadLine());
			rp.put("startLine", g.getStartLine());
			rp.put("mountsId", g.getMountsId());
			rp.put("mountsName", g.getMountsName());
			rp.put("cId", g.getcId());
			guardsInfo.add(rp);
		}
		result.put("count", count);
		result.put("size", pageSize);
		result.put("pages", Math.ceil(((double) count) / pageSize));
		result.put("content", guardsInfo);
		rds.setData(result);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getMajiaAccounts(List<String> accIds) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		if (accIds.size() > 0 && accIds.size() < 50) {
			String sql = "select new com.i5i58.data.account.MajiaAccount(a.accId, a.openId,"
					+ " a.nickName, a.signature, a.faceSmallUrl, p.vip, p.vipDeadline, p.score, p.richScore)"
					+ " from Account a, AccountProperty p where a.accId=p.accId and (a.accId='";
			int index = 0;
			for (String accId : accIds) {
				// MajiaAccount majiaAccount =
				// majiaAccountDao.findByAccId(accId);
				if (index == 0) {
					sql += accId + "'";
				} else {
					sql += " or a.accId = '" + accId + "'";
				}
				index++;
			}
			sql += ")";
			Query query = entityManager.createQuery(sql);
			@SuppressWarnings("unchecked")
			List<MajiaAccount> majiaAccountList = query.getResultList();
			rds.setData(majiaAccountList);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet setBindMobile(String accId, String phoneNo, String password, String verifyCode)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		String accPhoneNo = account.getPhoneNo();
		if (!StringUtils.StringIsEmptyOrNull(accPhoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("手机号注册的账号不能绑定手机");
			return rds;
		}
		YXResultSet rs = YunxinIM.verifySmscode(phoneNo, verifyCode);
		if (!rs.getCode().equals("200")) {
			System.out.println(rs.getError());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("验证码错误");
			return rds;
		}
		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}
		password = password.trim();
		if (!CheckUtils.IsPasswLength(password)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("密码长度为8-20个字符");
			return rds;
		}
		if (!CheckUtils.IsPassword(password)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("密码只能为字母和数字,且必须同时包含字母和数字");
			return rds;
		}

		try {
			password = StringUtils.getMd5(password);
		} catch (NoSuchAlgorithmException e1) {
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("md5_error");
			return rds;
		}
		try {
			account.setPassword(password);
			account.setBindMobile(phoneNo);
			account.setVersion(VersionUtils.updateVersion(account.getVersion()));
			accountPriDao.save(account);

			RefreshTokenResult refreshTokenResult = YunxinIM.refreshTokenAccount(account.getAccId());
			if (refreshTokenResult == null) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:logid null");
				return rds;
			}
			if (!refreshTokenResult.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:" + CodeToString.getString(refreshTokenResult.getCode()) + " desc:"
						+ refreshTokenResult.getString("desc"));
				return rds;
			}
			if (!refreshTokenResult.getInfo().getAccid().equals(account.getAccId())) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg("yx_exception:accid dif");
				return rds;
			}
			String token = refreshTokenResult.getInfo().getToken();
			accountUtils.setToken(account.getAccId(), token);
			// accountUtils.loadHotAccount(account);

			ResponseData rp = new ResponseData();
			rp.put("token", token);
			rds.setData(rp);
		} catch (Exception e) {
			logger.error("", e);
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			rds.setMsg("设置失败！");
			return rds;
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet modifyBindMobile(String accId, String password, String phoneNo, String verifyCode)
			throws IOException {
		ResultDataSet rds = new ResultDataSet();

		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}

		if (!account.getPassword().equals(password)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("密码错误");
			return rds;
		}

		YXResultSet rs = YunxinIM.verifySmscode(account.getBindMobile(), verifyCode);
		if (!rs.getCode().equals("200")) {
			System.out.println(rs.getError());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("验证码错误");
			return rds;
		}

		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}

		String mainPhoneNo = account.getPhoneNo();
		if (mainPhoneNo != null && mainPhoneNo.equals(account.getBindMobile())) {
			Account acc = accountPriDao.findByPhoneNo(phoneNo);
			if (acc != null) {
				rds.setCode(ResultCode.PARAM_INVALID.getCode());
				rds.setMsg("该手机号已经注册");
				return rds;
			}
			account.setPhoneNo(phoneNo);
			account.setBindMobile(phoneNo);
		} else {
			account.setBindMobile(phoneNo);
		}

		account.setVersion(VersionUtils.updateVersion(account.getVersion()));
		accountPriDao.save(account);

		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setMsg("修改成功！");
		return rds;
	}

	@Override
	public ResultDataSet getBindMobile(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountSecDao.findOne(accId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		ResponseData rp = new ResponseData();
		if (!StringUtils.StringIsEmptyOrNull(account.getBindMobile())) {
			account.setBindMobile(StringUtils.addMask(account.getBindMobile(), '*', 3, 4));
		}
		rp.put("bindMobile", account.getBindMobile());
		rds.setData(rp);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet sendBindingMobileVerifyCode(String accId, String phoneNo) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Account account = accountPriDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		String accPhoneNo = account.getPhoneNo();
		if (!StringUtils.StringIsEmptyOrNull(accPhoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("手机号注册的账号不能绑定手机");
			return rds;
		}

		if (!CheckUtils.validPhoneNum("0", phoneNo)) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("请输入正确手机号");
			return rds;
		}
		YXResultSet rs = YunxinIM.sendSmsCode(phoneNo, phoneNo, "3056622");
		if (!rs.getCode().equals("200")) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(rs.getError());
			return rds;
		}
		System.out.println("sendid:" + rs.getString("msg") + "; code:" + rs.getString("obj"));
		ResponseData responseData = new ResponseData();
		String maskPhoneNo = StringUtils.addMask(phoneNo, '*', 2, 2);
		responseData.put("phoneNo", maskPhoneNo);
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFaceUrlByAccId(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData responseData = new ResponseData();
		Account account = accountSecDao.findOne(accId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + accId);
			return rds;
		}
		responseData.put("faceOrgUrl", account.getFaceOrgUrl());
		responseData.put("faceSmallUrl", account.getFaceSmallUrl());
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getFaceUrlByOpenId(String openId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData responseData = new ResponseData();
		Account account = accountSecDao.findByOpenId(openId);
		if (account == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("不存在此账号" + openId);
			return rds;
		}
		responseData.put("faceOrgUrl", account.getFaceOrgUrl());
		responseData.put("faceSmallUrl", account.getFaceSmallUrl());
		responseData.put("isUseInGame", account.isFaceUseInGame());
		rds.setData(responseData);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getAccountProperty(String accId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		AccountProperty accountProperty = accountPropertySecDao.findByAccId(accId);
		rds.setData(accountProperty);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}
}
