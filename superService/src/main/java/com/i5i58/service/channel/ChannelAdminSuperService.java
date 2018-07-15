package com.i5i58.service.channel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
import com.i5i58.apis.platform.IPlatformChannelAdmin;
import com.i5i58.data.account.Account;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelGuard;
import com.i5i58.data.channel.ChannelRightInfo;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelRightInfo;
import com.i5i58.data.channel.HotNewLotAnchorChannel;
import com.i5i58.data.channel.NewLotAnchorChannel;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.primary.dao.account.AccountPriDao;
import com.i5i58.primary.dao.channel.ChannelGuardPriDao;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelRightInfoPriDao;
import com.i5i58.primary.dao.channel.NewLotAnchorChannelPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelRightInfoDao;
import com.i5i58.redis.all.HotNewLotAnchorChannelDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelGuardSecDao;
import com.i5i58.secondary.dao.channel.ChannelRightInfoSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.secondary.dao.channel.NewLotAnchorChannelSecDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.MyPageUtils;
import com.i5i58.util.ServerCode;
import com.i5i58.util.StringUtils;
import com.i5i58.util.SuperAdminUtils;
import com.i5i58.yunxin.YunxinIM;
import com.i5i58.yunxin.Utils.CodeToString;
import com.i5i58.yunxin.Utils.YXResultSet;

@Service(protocol = "dubbo")
public class ChannelAdminSuperService implements IPlatformChannelAdmin {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	AccountPriDao accountPriDao;

	@Autowired
	AccountSecDao accountSecDao;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	ChannelGuardPriDao channelGuardPriDao;

	@Autowired
	ChannelGuardSecDao channelGuardSecDao;

	@Autowired
	ChannelRightInfoPriDao channelRightInfoPriDao;

	@Autowired
	ChannelRightInfoSecDao channelRightInfoSecDao;

	@Autowired
	HotChannelRightInfoDao hotChannelRightInfoDao;

	@Autowired
	NewLotAnchorChannelPriDao newLotAnchorChannelPriDao;

	@Autowired
	NewLotAnchorChannelSecDao newLotAnchorChannelSecDao;

	@Autowired
	HotNewLotAnchorChannelDao hotNewLotAnchorChannelDao;

	@Autowired
	SuperAdminUtils superAdminUtils;

	@Override
	public ResultDataSet enableChannel(String superAccid, String cId, boolean enable) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		if (enable) {
			YXResultSet ret = Videocloud163.pauseChannel(channel.getYunXinCId());
			if (ret.getCode().equals("200")) {
				channel.setNullity(enable);
				channelPriDao.save(channel);
				rds.setCode(ResultCode.SUCCESS.getCode());
			} else {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(ret.getString("msg"));
			}
		} else {
			YXResultSet ret = Videocloud163.resumeChannel(channel.getYunXinCId());
			if (ret.getCode().equals("200")) {
				channel.setNullity(enable);
				channelPriDao.save(channel);
				rds.setCode(ResultCode.SUCCESS.getCode());
			} else {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(ret.getString("msg"));
			}
		}
		return rds;
	}

	@Override
	public ResultDataSet getChannelInfo(String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ResponseData response = new ResponseData();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel != null) {
			Account acc = accountSecDao.findOne(channel.getOwnerId());
			response.put("channel", channel);
			response.put("owner", acc);
			if (!StringUtils.StringIsEmptyOrNull(channel.getgId())) {
				ChannelGroup group = channelGroupPriDao.findByGId(channel.getgId());
				if (group != null) {
					if (StringUtils.StringIsEmptyOrNull(group.getParentId())) {
						response.put("topGroup", group);
					} else {
						ChannelGroup topGroup = channelGroupSecDao.findByGId(group.getParentId());
						if (topGroup != null) {
							response.put("topGroup", topGroup);
							response.put("group", group);
						}
					}
				}
			}
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(response);
		return rds;
	}

	@Override
	public ResultDataSet queryChannelList(String param, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "createDate");
		Sort sort1 = new Sort(Direction.fromString("desc"), "ownerId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort.and(sort1));
		Page<Channel> channels = channelSecDao.findByParam(param, pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(channels));
		return rds;
	}

	@Override
	public ResultDataSet getChannelGuardlist(String cId, int pageSize, int pageNum) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("desc"), "guardLevel");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<ChannelGuard> channelGuards = channelGuardSecDao.findByCIdAndDeadLineGreaterThan(cId,
				DateUtils.getNowTime(), pageable);
		rds.setCode(ResultCode.SUCCESS.getCode());
		rds.setData(MyPageUtils.getMyPage(channelGuards));
		return rds;
	}

	@Override
	public ResultDataSet addChannelRightInfo(String superAccid, int id, String action, String target, String imgUrl,
			String name, String params) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelRightInfo channelRightInfo = new ChannelRightInfo();
		channelRightInfo.setId(id);
		channelRightInfo.setAction(action);
		channelRightInfo.setImgUrl(imgUrl);
		channelRightInfo.setName(name);
		channelRightInfo.setParams(params);
		channelRightInfo.setTarget(target);
		channelRightInfoPriDao.save(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet updateChannelRightInfo(String superAccid, int id, String action, String target, String imgUrl,
			String name, String params) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelRightInfo channelRightInfo = channelRightInfoPriDao.findOne(id);
		if (channelRightInfo == null) {
			rds.setMsg("没有该频道右侧信息");
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		}
		channelRightInfo.setId(id);
		channelRightInfo.setAction(action);
		channelRightInfo.setImgUrl(imgUrl);
		channelRightInfo.setName(name);
		channelRightInfo.setParams(params);
		channelRightInfo.setTarget(target);
		channelRightInfoPriDao.save(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteChannelRightInfo(String superAccid, int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelRightInfo channelRightInfo = channelRightInfoPriDao.findOne(id);
		channelRightInfoPriDao.delete(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getChannelRightInfo(int id) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		ChannelRightInfo channelRightInfo = channelRightInfoSecDao.findOne(id);
		rds.setData(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryChannelRightInfo() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Iterable<ChannelRightInfo> channelRightInfo = channelRightInfoSecDao.findAll();
		rds.setData(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncChannelRightInfo(String superAccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Iterable<ChannelRightInfo> channelRightInfo = channelRightInfoSecDao.findAll();
		List<HotChannelRightInfo> hotChannelRightInfo = new ArrayList<HotChannelRightInfo>();
		for (ChannelRightInfo cri : channelRightInfo) {
			HotChannelRightInfo hotInfo = new HotChannelRightInfo();
			hotInfo.setId(cri.getId());
			hotInfo.setAction(cri.getAction());
			hotInfo.setImgUrl(cri.getImgUrl());
			hotInfo.setName(cri.getName());
			hotInfo.setParams(cri.getParams());
			hotInfo.setTarget(cri.getTarget());
			hotChannelRightInfo.add(hotInfo);
		}
		hotChannelRightInfoDao.save(hotChannelRightInfo);
		rds.setData(channelRightInfo);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet getNewLotChannelList(String superAccid, int pageNum, int pageSize) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Sort sort = new Sort(Direction.fromString("asc"), "sortId");
		Pageable pageable = new PageRequest(pageNum, pageSize, sort);
		Page<NewLotAnchorChannel> channels = newLotAnchorChannelSecDao.findAll(pageable);
		rds.setData(MyPageUtils.getMyPage(channels));
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet addNewLotChannel(String superAccid, String cId, int sortId, int newLot) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		NewLotAnchorChannel ch = new NewLotAnchorChannel();
		ch.setcId(channel.getcId());
		ch.setChannelId(channel.getChannelId());
		ch.setOwnerId(channel.getOwnerId());
		ch.setChannelName(channel.getChannelName());
		ch.setTitle(channel.getTitle());
		ch.setType(channel.getType());
		ch.setStatus(channel.getStatus());
		ch.setCoverUrl(channel.getCoverUrl());
		ch.setgId(channel.getgId());
		ch.setChannelNotice(channel.getChannelNotice());
		ch.setYunXinRId(channel.getYunXinRId());
		ch.setHttpPullUrl(channel.getHttpPullUrl());
		ch.setHlsPullUrl(channel.getHlsPullUrl());
		ch.setRtmpPullUrl(channel.getRtmpPullUrl());
		ch.setNewLot(newLot);
		ch.setSortId(sortId);
		newLotAnchorChannelPriDao.save(ch);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet deleteNewLotChannel(String superAccid, String cId) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		newLotAnchorChannelPriDao.deleteByCId(cId);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncNewLotChannel(String superAccid) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		hotNewLotAnchorChannelDao.deleteAll();
		Iterable<NewLotAnchorChannel> channels = newLotAnchorChannelSecDao.findAll();
		List<HotNewLotAnchorChannel> hotChannels = new ArrayList<HotNewLotAnchorChannel>();
		for (NewLotAnchorChannel channel : channels) {
			HotNewLotAnchorChannel ch = new HotNewLotAnchorChannel();
			ch.setcId(channel.getcId());
			ch.setChannelId(channel.getChannelId());
			ch.setOwnerId(channel.getOwnerId());
			ch.setChannelName(channel.getChannelName());
			ch.setTitle(channel.getTitle());
			ch.setType(channel.getType());
			ch.setStatus(channel.getStatus());
			ch.setCoverUrl(channel.getCoverUrl());
			ch.setgId(channel.getgId());
			ch.setChannelNotice(channel.getChannelNotice());
			ch.setYunXinRId(channel.getYunXinRId());
			ch.setHttpPullUrl(channel.getHttpPullUrl());
			ch.setHlsPullUrl(channel.getHlsPullUrl());
			ch.setRtmpPullUrl(channel.getRtmpPullUrl());
			ch.setNewLot(channel.getNewLot());
			ch.setSortId(channel.getSortId());
			hotChannels.add(ch);
		}
		hotNewLotAnchorChannelDao.save(hotChannels);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet editChannelType(String superAccid, String cId, int type) throws IOException {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelPriDao.findByCId(cId);
		if (channel == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		if (StringUtils.StringIsEmptyOrNull(channel.getOwnerId())) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该频道没有绑定主播");
			return rds;
		}
		Account acc = accountPriDao.findOne(channel.getOwnerId());
		if (acc == null) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg("该频道绑定的主播不存在");
			return rds;
		}
		channel.setType(type);
		channelPriDao.save(channel);
		HotChannel hotChannel = hotChannelDao.findOne(cId);
		if (hotChannel != null) {
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
			hotChannel.setLocation(acc.getLocation());
			hotChannel.setWeekOffer(new Long(0));
			hotChannel.setBrightness(0);
			hotChannel.setClubLevel(channel.getClubLevel());
			hotChannel.setClubScore(channel.getClubScore());
			hotChannelDao.save(hotChannel);
		} else {
			HotChannel newHotChannel = new HotChannel();
			newHotChannel.setId(channel.getcId());
			newHotChannel.setChannelId(channel.getChannelId());
			newHotChannel.setOwnerId(channel.getOwnerId());
			newHotChannel.setChannelName(channel.getChannelName());
			newHotChannel.setTitle(channel.getTitle());
			newHotChannel.setgId(channel.getgId());
			newHotChannel.setStatus(channel.getStatus());
			newHotChannel.setType(channel.getType());
			newHotChannel.setYunXinCId(channel.getYunXinCId());
			newHotChannel.setYunXinRId(channel.getYunXinRId());
			newHotChannel.setHlsPullUrl(channel.getHlsPullUrl());
			newHotChannel.setHttpPullUrl(channel.getHttpPullUrl());
			newHotChannel.setPushUrl(channel.getPushUrl());
			newHotChannel.setRtmpPullUrl(channel.getRtmpPullUrl());
			newHotChannel.setLocation(acc.getLocation());
			newHotChannel.setWeekOffer(new Long(0));
			newHotChannel.setBrightness(0);
			newHotChannel.setClubLevel(channel.getClubLevel());
			newHotChannel.setClubScore(channel.getClubScore());
			hotChannelDao.save(newHotChannel);
		}
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@CacheEvict(value = "hotchannelcache", allEntries = true)
	@Override
	public ResultDataSet clearHotPageCache() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet syncChannelData() throws IOException {
		ResultDataSet rds = new ResultDataSet();
		List<HotChannel> hotChannels = (List<HotChannel>) hotChannelDao.findAll();
		if (hotChannels == null || hotChannels.size() == 0) {
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
			return rds;
		}
		List<Channel> channels = new ArrayList<Channel>();
		for (HotChannel ch : hotChannels) {
			try {
				Channel channel = channelPriDao.findByCId(ch.getId());
				if (channel == null)
					continue;
				// channel.setStatus(ch.getStatus());
				channel.setBrightness(ch.getBrightness());
				channel.setPlayerCount(ch.getPlayerCount());
				channel.setPlayerTimes(ch.getPlayerTimes());
				channel.setWeekOffer(ch.getWeekOffer());
				channel.setHeartCount(ch.getHeartCount());
				channel.setHeartUserCount(ch.getHeartUserCount());
				channels.add(channel);
			} catch (Throwable e) {

			}
		}
		channelPriDao.save(channels);
		rds.setCode(ResultCode.SUCCESS.getCode());
		return rds;
	}

	@Override
	public ResultDataSet queryAchorGuardList(String accId, int pageNum, int pageSize) {
		ResultDataSet rds = new ResultDataSet();
		Channel channel = channelSecDao.findByOwnerId(accId);

		if (channel == null) {
			rds.setMsg(ServerCode.NO_CHANNEL.getCode());
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
		Page<ChannelGuard> guard = channelGuardSecDao.findByCIdAndDeadLineGreaterThan(channel.getcId(), nowDate,
				pageable);
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

		try {
			YXResultSet yxResultSet = YunxinIM.setChatRoomMemberRole(channel.getYunXinRId(), channel.getCreatorId(),
					accId, "-2", optValue, "");
			if (!yxResultSet.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(CodeToString.getString(yxResultSet.getCode()));
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (IOException e) {
			logger.error(e);
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
		if (duration > DateUtils.dayMilliSecond / 1000) {
			rds.setMsg("最长禁言24小时");
			rds.setCode(ResultCode.PARAM_INVALID.getCode());
			return rds;
		}

		try {
			YXResultSet yxResultSet = YunxinIM.setChatRoomTemporaryMute(channel.getYunXinRId(), channel.getCreatorId(),
					accId, duration.toString(), "false", "");

			if (!yxResultSet.getCode().equals("200")) {
				rds.setCode(ResultCode.SERVICE_ERROR.getCode());
				rds.setMsg(CodeToString.getString(yxResultSet.getCode()));
				return rds;
			}
			rds.setCode(ResultCode.SUCCESS.getCode());
			return rds;
		} catch (IOException e) {
			logger.error(e);
			rds.setMsg("设置发生异常");
			rds.setCode(ResultCode.SERVICE_ERROR.getCode());
			return rds;
		}
	}
}
