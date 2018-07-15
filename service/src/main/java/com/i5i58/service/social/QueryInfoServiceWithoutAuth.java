package com.i5i58.service.social;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.dubbo.config.annotation.Service;
import com.i5i58.apis.constants.ResultCode;
import com.i5i58.apis.constants.ResultDataSet;
import com.i5i58.apis.social.IQueryInfoWithoutAuth;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountProperty;
import com.i5i58.data.account.HotAccount;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelFansClub;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelMajiaer;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.primary.dao.account.AccountPropertyPriDao;
import com.i5i58.primary.dao.channel.ChannelFansClubPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.social.FollowInfoPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelMajiaerDao;
import com.i5i58.secondary.dao.account.AccountPropertySecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelFansClubSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.social.FollowInfoSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;

/**
 * 
 * @author songfl
 *
 */

@Service(protocol = "dubbo")
public class QueryInfoServiceWithoutAuth implements IQueryInfoWithoutAuth {

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	FollowInfoPriDao followInfoPriDao;

	@Autowired
	FollowInfoSecDao followInfoSecDao;

	@Autowired
	AccountPropertyPriDao accountPropertyPriDao;

	@Autowired
	AccountPropertySecDao accountPropertySecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	ChannelFansClubPriDao channelFansClubPriDao;

	@Autowired
	ChannelFansClubSecDao channelFansClubSecDao;

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	HotChannelMajiaerDao hotChannelMajiaerDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	EntityManager entityManager;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

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
		response.put("account", htAcc);
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(0, 1, sort);
		List<ChannelGuard> guard = channelGuardSecDao.findByAccIdAndDeadLineGreaterThan(accId, DateUtils.getNowTime(),
				pageable);
		if (guard != null && guard.size() > 0) {
			response.put("guard", guard.get(0).getGuardLevel());
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
			ChannelFansClub fansClub = channelFansClubSecDao.findByCIdAndAccIdAndEndDateGreaterThan(clubCId, 
					accId, today);
			if (fansClub == null)
				break;
			response.put("clubScore", hotChannel.getClubScore());
			response.put("clubLevel", hotChannel.getClubLevel());
			response.put("clubTitle", hotChannel.getClubTitle());
			response.put("clubName", hotChannel.getClubName());
			response.put("clubPersonalScore", fansClub.getPersonalScore());
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
	public ResultDataSet getFollowByAccId(String ta, Integer pageNum) {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "id");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		Page<String> follows = followInfoSecDao.findFollow(ta, pageable);
		rds.setData(MyPageUtils.getMyPage(follows));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelsByMajia(String accId, int pageSize, int pageNum) {
		ResultDataSet rds = new ResultDataSet();

		Account account = accountSecDao.findOne(accId);
		if (account == null) {
			rds.setMsg(ServerCode.NO_ACCOUNT.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		Sort sort = new Sort(Direction.fromString("desc"), "majia", "guardLevel");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<HotChannelMajiaer> hotChannelMajiaers = hotChannelMajiaerDao.findByAccId(accId, pageable);

		rds.setData(MyPageUtils.getMyPage(hotChannelMajiaers));
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
	public ResultDataSet getChannelInfo(String cId) {
		ResultDataSet rds = new ResultDataSet();
		HashMap<String, Object> response = new HashMap<String, Object>();
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}
		response.put("channel", hotChannel);
		if (!StringUtils.StringIsEmptyOrNull(hotChannel.getOwnerId())) {
			Account account = accountSecDao.findOne(hotChannel.getOwnerId());
			response.put("account", account);
		}
		rds.setData(response);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getGroupInfo(String gId) {
		ResultDataSet rds = new ResultDataSet();
		ChannelGroup channelGroup = channelGroupSecDao.findByGId(gId);
		if (channelGroup == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("没有该频道组");
			return rds;
		}
		ChannelGroup foundGroup = new ChannelGroup();
		foundGroup.setGroupId(channelGroup.getGroupId());
		foundGroup.setName(channelGroup.getName());
		foundGroup.setDescription(channelGroup.getDescription());
		foundGroup.setFaceUrl(channelGroup.getFaceUrl());
		rds.setData(foundGroup);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;

	}
}
