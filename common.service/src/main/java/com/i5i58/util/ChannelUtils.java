package com.i5i58.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelAdminor;
import com.i5i58.data.channel.ChannelAuth;
import com.i5i58.data.channel.ChannelWatchingRecord;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.channel.HotChannelMic;
import com.i5i58.data.channel.HotChannelViewer;
import com.i5i58.data.channel.WeekOffer;
import com.i5i58.data.group.ChannelGroup;
import com.i5i58.data.group.GroupAdminor;
import com.i5i58.data.profile.GroupProfile;
import com.i5i58.data.social.HotSongs;
import com.i5i58.data.superAdmin.SuperAdmin;
import com.i5i58.primary.dao.channel.ChannelAdminorPriDao;
import com.i5i58.primary.dao.channel.ChannelWatchingRecordPriDao;
import com.i5i58.primary.dao.group.ChannelGroupPriDao;
import com.i5i58.primary.dao.group.GroupAdminorPriDao;
import com.i5i58.primary.dao.superAdmin.SuperAdminPriDao;
import com.i5i58.redis.all.HotChannelMicDao;
import com.i5i58.secondary.dao.group.ChannelGroupSecDao;
import com.i5i58.secondary.dao.profile.GroupProfileSecDao;

@Component
public class ChannelUtils {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelAdminorPriDao channelAdminorPriDao;

	@Autowired
	AuthVerify<ChannelAuth> channelAuthVerify;

	@Autowired
	ChannelGroupPriDao channelGroupPriDao;

	@Autowired
	ChannelGroupSecDao channelGroupSecDao;

	@Autowired
	GroupAdminorPriDao groupAdminorPriDao;

	@Autowired
	SuperAdminPriDao superAdminPriDao;

	@Autowired
	GroupProfileSecDao groupProfileSecDao;

	@Autowired
	HotChannelMicDao hotChannelMicDao;

	@Autowired
	ChannelWatchingRecordPriDao channelWatchingRecordPriDao;
	
	public int getPermission(ChannelAuth... authortys) {
		return channelAuthVerify.getPermission(authortys);
	}

	public int getChannelAdminPermission() {
		return channelAuthVerify.getPermission(ChannelAuth.PROHIBIT_SPEAK, ChannelAuth.KICKOUT_ROOM,
				ChannelAuth.ASSIGN_CHANNEL_USER_HONOR_AUTHORITY, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY,
				ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY);
	}

	/*public int getGroupAdminPermission() {
		return channelAuthVerify.getPermission(ChannelAuth.PROHIBIT_SPEAK, ChannelAuth.KICKOUT_ROOM,
				ChannelAuth.ASSIGN_ADMIN_AUTHORITY, ChannelAuth.ASSIGN_OW_AUTHORITY,
				ChannelAuth.ASSIGN_CHANNEL_USER_HONOR_AUTHORITY, ChannelAuth.ASSIGN_EDIT_INFO_AUTHORITY,
				ChannelAuth.OPERATE_USER_MIC_SEQUENCE_AUTHORITY);
	}*/

	/**
	 * 驗證頻道權限
	 * 
	 * @param channel
	 * @param accId
	 * @param auth
	 * @return
	 */
	public boolean verifyChannelAuth(Channel channel, String accId, ChannelAuth auth) {
		if (auth != ChannelAuth.ASSIGN_OW_AUTHORITY) {
			if (channel.getOwnerId().equals(accId)) {
				System.out.println("驗證為本頻道owner");
				return true;
			}
			if (auth != ChannelAuth.ASSIGN_ADMIN_AUTHORITY) {
				ChannelAdminor channelAdminor = channelAdminorPriDao.findByCIdAndAccId(channel.getcId(), accId);
				if (channelAdminor != null) {
					if (channelAuthVerify.Verify(auth, channelAdminor.getAdminRight())) {
						System.out.println("驗證為本頻道管理員");
						return true;
					}
				}
			}
		}
		if (!StringUtils.StringIsEmptyOrNull(channel.getgId())) {
			ChannelGroup group = channelGroupPriDao.findByGId(channel.getgId());
			if (group != null) {
				if (group.getOwnerId().equals(accId)) {
					System.out.println("驗證為上級組owner");
					return true;
				}
				GroupAdminor groupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, channel.getgId());
				if (groupAdminor != null) {
					if (channelAuthVerify.Verify(auth, groupAdminor.getAdminRight())) {
						System.out.println("驗證為上級組管理員");
						return true;
					}
				}
				ChannelGroup topGroup = channelGroupPriDao.findByGId(group.getParentId());
				if (topGroup != null) {
					if (topGroup.getOwnerId().equals(accId)) {
						System.out.println("驗證為上上級組owner");
						return true;
					}
					GroupAdminor topGroupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, group.getParentId());
					if (topGroupAdminor != null) {
						if (channelAuthVerify.Verify(auth, topGroupAdminor.getAdminRight())) {
							System.out.println("驗證為上上級組管理員");
							return true;
						}
					}
				}
			}
		}
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}

	/**
	 * 驗證頻道權限
	 * 
	 * @param channel
	 * @param accId
	 * @param auth
	 * @return
	 */
	public boolean verifyHotChannelAuth(HotChannel hotChannel, String accId, ChannelAuth auth) {

		if (auth != ChannelAuth.ASSIGN_OW_AUTHORITY) {
			if (hotChannel.getOwnerId().equals(accId)) {
				System.out.println("驗證為本頻道owner");
				return true;
			}
			if (auth != ChannelAuth.ASSIGN_ADMIN_AUTHORITY) {
				ChannelAdminor channelAdminor = channelAdminorPriDao.findByCIdAndAccId(hotChannel.getId(), accId);
				if (channelAdminor != null) {
					if (channelAuthVerify.Verify(auth, channelAdminor.getAdminRight())) {
						System.out.println("驗證為本頻道管理員");
						return true;
					}
				}
			}
		}
		if (!StringUtils.StringIsEmptyOrNull(hotChannel.getgId())) {
			ChannelGroup group = channelGroupPriDao.findByGId(hotChannel.getgId());
			if (group != null) {
				if (group.getOwnerId().equals(accId)) {
					System.out.println("驗證為上級組owner");
					return true;
				}
				GroupAdminor groupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, hotChannel.getgId());
				if (groupAdminor != null) {
					if (channelAuthVerify.Verify(auth, groupAdminor.getAdminRight())) {
						System.out.println("驗證為上級組管理員");
						return true;
					}
				}
				ChannelGroup topGroup = channelGroupPriDao.findByGId(group.getParentId());
				if (topGroup != null) {
					if (topGroup.getOwnerId().equals(accId)) {
						System.out.println("驗證為上上級組owner");
						return true;
					}
					GroupAdminor topGroupAdminor = groupAdminorPriDao.findByAdminIdAndGId(accId, group.getParentId());
					if (topGroupAdminor != null) {
						if (channelAuthVerify.Verify(auth, topGroupAdminor.getAdminRight())) {
							System.out.println("驗證為上上級組管理員");
							return true;
						}
					}
				}
			}
		}
		SuperAdmin superAdmin = superAdminPriDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}

	/**
	 * 驗證組權限
	 * 
	 * @param group
	 * @param accId
	 * @param auth
	 * @return
	 */
	/*public boolean verifyGroupAuth(ChannelGroup group, String accId, ChannelAuth auth) {
		if (group != null) {
			if (auth != ChannelAuth.ASSIGN_OW_AUTHORITY) {
				if (group.getOwnerId().equals(accId)) {
					return true;
				}
				if (auth != ChannelAuth.ASSIGN_ADMIN_AUTHORITY) {
					GroupAdminor groupAdminor = groupAdminorDao.findByAdminIdAndGId(accId, group.getgId());
					if (groupAdminor != null) {
						if (channelAuthVerify.Verify(auth, groupAdminor.getAdminRight())) {
							return true;
						}
					}
				}
				if (StringUtils.StringIsEmptyOrNull(group.getProfileId())) {
					GroupProfile groupProfile = groupProfileDao.findByFId(group.getProfileId());
					if (groupProfile != null && groupProfile.getAccId().equals(accId)) {
						return true;
					}
				}
			}
			ChannelGroup topGroup = channelGroupDao.findByGId(group.getParentId());
			if (topGroup != null) {
				if (topGroup.getOwnerId().equals(accId)) {
					return true;
				}
				GroupAdminor topGroupAdminor = groupAdminorDao.findByAdminIdAndGId(accId, group.getParentId());
				if (topGroupAdminor != null) {
					if (channelAuthVerify.Verify(auth, topGroupAdminor.getAdminRight())) {
						return true;
					}
				}
				if (StringUtils.StringIsEmptyOrNull(group.getProfileId())) {
					GroupProfile groupProfile = groupProfileDao.findByFId(group.getProfileId());
					if (groupProfile != null && groupProfile.getAccId().equals(accId)) {
						return true;
					}
				}
			}
		}
		SuperAdmin superAdmin = superAdminDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}*/

	/*public boolean verifyTopGroupMember(ChannelGroup group, String accId) {
		if (group != null) {
			if (group.getOwnerId().equals(accId)) {
				return true;
			}
			GroupAdminor topGroupAdminor = groupAdminorDao.findByAdminIdAndGId(accId, group.getgId());
			if (topGroupAdminor != null) {
				return true;
			}
		}
		SuperAdmin superAdmin = superAdminDao.findOne(accId);
		if (superAdmin != null) {
			return true;
		}
		return false;
	}*/

	public boolean checkChannelCount(String gId) {
		ChannelGroup channelGroup = channelGroupSecDao.findOne(gId);
		if (channelGroup == null) {
			return false;
		}
		return checkChannelCount(channelGroup);
	}

	public boolean checkChannelCount(ChannelGroup channelGroup) {
		int count = 0;
		GroupProfile groupProfile = groupProfileSecDao.findOne(channelGroup.getProfileId());
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			ChannelGroup topChannelGroup = channelGroupSecDao.findOne(channelGroup.getParentId());
			count = topChannelGroup.getChannelCount();
		} else {
			count = channelGroup.getChannelCount();
		}
		if (count < groupProfile.getCreateChannelCount()) {
			return true;
		}
		return false;
	}

	public void updateChannelCount(String gId) {
	}

	public void updateChannelCount(ChannelGroup channelGroup, int delta) {
		channelGroup.setChannelCount(channelGroup.getChannelCount() + delta);
		channelGroupPriDao.save(channelGroup);
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			ChannelGroup topChannelGroup = channelGroupPriDao.findOne(channelGroup.getParentId());
			topChannelGroup.setChannelCount(topChannelGroup.getChannelCount() + delta);
			channelGroupPriDao.save(topChannelGroup);
		}
	}
	
	/**
	 * 获取公会剩余可创建频道数
	 * @param channelGroup
	 * @return
	 */
	public int getTopGroupUsableChannelCount(ChannelGroup channelGroup){
		GroupProfile groupProfile = groupProfileSecDao.findOne(channelGroup.getProfileId());
		if(groupProfile==null){
			return 0;
		}
		int usedCount = 0;
		if (!StringUtils.StringIsEmptyOrNull(channelGroup.getParentId())) {
			ChannelGroup topChannelGroup = channelGroupSecDao.findOne(channelGroup.getParentId());
			usedCount = topChannelGroup.getChannelCount();
		} else {
			usedCount = channelGroup.getChannelCount();
		}
		return groupProfile.getCreateChannelCount() - usedCount;
	}
/*
	public boolean checkGroupCount(ChannelGroup group) {
		GroupProfile groupProfile = groupProfileDao.findOne(group.getProfileId());
		List<ChannelGroup> ChannelGroups = channelGroupDao.findByParentId(group.getgId());
		if (groupProfile.getCreateSubGroupCount() > ChannelGroups.size()) {
			return true;
		}
		return false;
	}*/

	/*public boolean checkTopGroupCount(GroupProfile groupProfile) {
		List<ChannelGroup> ChannelGroups = channelGroupDao.findTopGroupByFId(groupProfile.getfId());
		if (groupProfile.getCreateTopGroupCount() > ChannelGroups.size()) {
			return true;
		}
		return false;
	}
*/
	public boolean checkFirstMic(String accId, String cId) {
		HotChannelMic hotChannelMic = hotChannelMicDao.findByCIdAndIndexId(cId, 0);
		if (hotChannelMic != null && hotChannelMic.getAccId().equals(accId)) {
			return true;
		}
		return false;
	}

	// [start] static
	public void addViewer(String cId, HotChannelViewer hotViewer) {
		double score = new Double(hotViewer.getVip()) * Constant.VIP_SCORE_RATE
				+ new Double(hotViewer.getGuardLevel()) * Constant.GUARD_SCORE_RATE;
		new JedisUtils().zadd(Constant.HOT_CHANNEL_VIEWER_SET_KEY + cId, score, hotViewer.getAccId());
	}

	public Set<String> getViewer(String cId, int offset) {
		return new JedisUtils().zrevrangebyscore(Constant.HOT_CHANNEL_VIEWER_SET_KEY + cId,
				Double.toString(Double.MAX_VALUE), "0", offset * Constant.SOFA_PAGE_SIZE, Constant.SOFA_PAGE_SIZE);
	}

	public long getViewerCount(String cId) {
		return new JedisUtils().zcount(Constant.HOT_CHANNEL_VIEWER_SET_KEY + cId, 0, Double.MAX_VALUE);
	}

	public long getIndexInViewer(String cId, HotChannelViewer hotViewer) {
		return new JedisUtils().zrank(Constant.HOT_CHANNEL_VIEWER_SET_KEY + cId, hotViewer.getAccId());
	}

	public void removeViewer(String cId, String accId) {
		if (StringUtils.StringIsEmptyOrNull(cId) || StringUtils.StringIsEmptyOrNull(accId)) {
			return;
		}
		new JedisUtils().zrem(Constant.HOT_CHANNEL_VIEWER_SET_KEY + cId, accId);
	}

	public void addRichman(String cId, HotChannelViewer hotViewer) {
		double score = new Double(hotViewer.getRichScore()) * Constant.VIP_SCORE_RATE
				+ new Double(hotViewer.getGuardLevel()) * Constant.GUARD_SCORE_RATE;
		new JedisUtils().zadd(Constant.HOT_CHANNEL_RICHER_SET_KEY + cId, score, hotViewer.getAccId());
	}

	public Set<String> getRicher(String cId, Integer pageNum) {
		return new JedisUtils().zrevrangebyscore(Constant.HOT_CHANNEL_RICHER_SET_KEY + cId,
				Double.toString(Double.MAX_VALUE), Double.toString(Constant.VIP_SCORE_RATE),
				pageNum * Constant.SOFA_PAGE_SIZE, Constant.SOFA_PAGE_SIZE);
	}

	public long getIndexInRicher(String cId, HotChannelViewer hotViewer) {
		return new JedisUtils().zrank(Constant.HOT_CHANNEL_RICHER_SET_KEY + cId, hotViewer.getAccId());
	}

	public void removeRicher(String cId, String accId) {
		if (StringUtils.StringIsEmptyOrNull(cId) || StringUtils.StringIsEmptyOrNull(accId)) {
			return;
		}
		new JedisUtils().zrem(Constant.HOT_CHANNEL_RICHER_SET_KEY + cId, accId);
	}

	public void addNoticeLive(String cId, String accid) {
		new JedisUtils().saddByNewOne(Constant.NOTICE_LIVE_SET_KEY + cId, accid);
	}

	public void removeNoticeLive(String cId, String accid) {
		new JedisUtils().srem(Constant.NOTICE_LIVE_SET_KEY + cId, accid);
	}

	public boolean isNoticeLive(String cId, String accid) {
		return new JedisUtils().sismember(Constant.NOTICE_LIVE_SET_KEY + cId, accid);
	}

	public long addWeekOffer(String cId, String accId, final long amount, String name, String faceSmallUrl,
			final int vip, final int guardLevel, final long richScore) {
		long offer = 0;
		System.out.println(amount);
		JedisUtils jedisUtils = new JedisUtils();
		Double score = new JedisUtils().zscore(Constant.HOT_CHANNEL_WEEKOFFER_SSET_KEY + cId, accId);
		if (score == null) {
			offer = amount;
		} else {
			System.out.println(score);
			offer = score.longValue() + amount;
		}
		System.out.println(offer);
		jedisUtils.zadd(Constant.HOT_CHANNEL_WEEKOFFER_SSET_KEY + cId, offer, accId);// redis排序集合记录周榜排名
		// jedisUtils.hset(Constant.HOT_CHANNEL_WEEKOFFER_HSET_KEY + cId, accId,
		// Long.toString(offer));
		WeekOffer weekOffer = new WeekOffer();
		weekOffer.setAccId(accId);
		weekOffer.setOffer(offer);
		weekOffer.setName(name);
		weekOffer.setFaceSmallUrl(faceSmallUrl);
		weekOffer.setVip(vip);
		weekOffer.setGuardLevel(guardLevel);
		weekOffer.setRichScore(richScore);
		try {
			String value = new JsonUtils().toJson(weekOffer);
			System.out.println(value);
			jedisUtils.hset(Constant.HOT_CHANNEL_WEEKOFFER_HSET_KEY + cId, accId, value);// redis
																							// hash集合存放周榜用户对象json
		} catch (IOException e) {
			logger.error("", e);
		}
		return offer;
	}

	public List<WeekOffer> getWeekOffer(String cId) {
		JedisUtils jedisUtils = new JedisUtils();
		Set<String> result = jedisUtils.zrevrangebyscore(Constant.HOT_CHANNEL_WEEKOFFER_SSET_KEY + cId,
				Double.toString(Double.MAX_VALUE), "0", 0, 20);
		List<WeekOffer> ret = new ArrayList<WeekOffer>();
		for (String accId : result) {
			try {
				ret.add(new JsonUtils().toObject(jedisUtils.hget(Constant.HOT_CHANNEL_WEEKOFFER_HSET_KEY + cId, accId),
						WeekOffer.class));
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return ret;
	}
	// [end]

	public void addSongs(HotSongs hotSongs, String type) {
		double score = 0;
		switch (type) {
		case "1":
			score = new Double(hotSongs.getPlayCount());
			break;
		case "2":
			score = new Double(hotSongs.getPlayCount());
			break;

		default:
			break;
		}
		new JedisUtils().zadd(Constant.HOT_SHORT_FILMS_ZSET_KEY, score, hotSongs.getUuId());
	}

	public Set<String> getSongs(Integer pageNum, Integer pageSize) {
		return new JedisUtils().zrevrangebyscore(Constant.HOT_SHORT_FILMS_ZSET_KEY, Double.toString(Double.MAX_VALUE),
				"0", pageNum, pageSize);
	}

	public void channelWatchingStart(String cId, String accId) throws ParseException {
		long nowTime = DateUtils.getNowTime();

		ChannelWatchingRecord todayWatchingTime = channelWatchingRecordPriDao.findOne(cId + "_" + accId);
		if (todayWatchingTime == null) {
			todayWatchingTime = new ChannelWatchingRecord();
			todayWatchingTime.setId(cId + "_" + accId);
			todayWatchingTime.setAccId(accId);
			todayWatchingTime.setcId(cId);
			todayWatchingTime.setStartTime(nowTime);
		} else {
			long dayDiff = DateUtils.getDayInterval(todayWatchingTime.getStartTime(), nowTime);
			if (dayDiff != 0) {// 重置过期值
				todayWatchingTime.setStartTime(nowTime);
				todayWatchingTime.setFinishTime(0L);
				todayWatchingTime.setDuration(0L);
			}
			todayWatchingTime.setStartTime(nowTime);
		}
		channelWatchingRecordPriDao.save(todayWatchingTime);
	}

	public void channelWatchingFinish(String cId, String accId) throws ParseException {
		long nowDate = DateUtils.getNowDate();
		long nowTime = DateUtils.getNowTime();
		long duration = 0L;

		ChannelWatchingRecord todayWatchingTime = channelWatchingRecordPriDao.findOne(cId + "_" + accId);
		if (todayWatchingTime == null) {
			logger.warn("没有观看开始记录,无法计算观看时间");
			return;
		}

		long dayDiff = DateUtils.getDayInterval(todayWatchingTime.getStartTime(), nowDate);
		if (dayDiff < 0) {
			logger.warn("时间错误,无法计算观看时间");
			return;
		} else if (dayDiff > 0) {
			// 跨天不考虑，累计到退出时的那一天
		}

		duration = nowTime - todayWatchingTime.getStartTime() + todayWatchingTime.getDuration();
		todayWatchingTime.setFinishTime(nowTime);
		todayWatchingTime.setDuration(duration);

		channelWatchingRecordPriDao.save(todayWatchingTime);
	}
}
