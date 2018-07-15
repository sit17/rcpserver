package com.i5i58.schedule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.i5i58.Videocloud163.Videocloud163;
import com.i5i58.clubTask.ClubTaskUtils;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.HotChannel;
import com.i5i58.data.group.AnchorContract;
import com.i5i58.data.group.AnchorContractStatus;
import com.i5i58.primary.dao.channel.ChannelPriDao;
import com.i5i58.primary.dao.channel.ChannelStatus;
import com.i5i58.primary.dao.group.AnchorContractPriDao;
import com.i5i58.redis.all.HotChannelDao;
import com.i5i58.redis.all.HotChannelHeartUserDao;
import com.i5i58.redis.all.HotDailyHeartDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.util.AnchorUtils;
import com.i5i58.util.ConfigUtils;
import com.i5i58.util.Constant;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JedisUtils;
import com.i5i58.yunxin.Utils.YXResultSet;

/**
 * @author Administrator
 *
 */
@Component
public class ScheduledTasks {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	ChannelPriDao channelPriDao;

	@Autowired
	ChannelSecDao channelSecDao;

	@Autowired
	HotChannelDao hotChannelDao;

	@Autowired
	HotDailyHeartDao hotDailyHeartDao;

	@Autowired
	HotChannelHeartUserDao hotChannelHeartUserDao;

	@Autowired
	JedisUtils jedisUtils;

	@Autowired
	ClubTaskUtils clubTaskUtils;

	@Autowired
	AnchorUtils anchorUtils;

	@Autowired
	ConfigUtils configUtils;

	@Autowired
	AnchorContractPriDao anchorContractPriDao;

	// @Scheduled(fixedRate = 30000)
	public void reportCurrentTime() {
		System.out.println("The time is now " + dateFormat.format(new Date()));
	}

	/**
	 * 每天6点刷频道点星
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void clearHotDailyHeart() {
		System.out.println("clear HotDailyHeart time is " + dateFormat.format(new Date()));
		hotDailyHeartDao.deleteAll();
		hotChannelHeartUserDao.deleteAll();
		Iterable<Channel> channels = channelSecDao.findAll();
		for (Channel ch : channels) {
			HotChannel hotChannel = hotChannelDao.findOne(ch.getcId());
			if (hotChannel != null) {
				hotChannel.setBrightness(0);
				hotChannel.setHeartCount(0);
				hotChannel.setHeartUserCount(0);
				hotChannelDao.save(hotChannel);
			}
		}
	}

	/**
	 * 每周一6点刷新周榜
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 ? * MON")
	public void clearWeekOffer() {
		System.out.println("clear WeekOffer time is " + dateFormat.format(new Date()));
		Iterable<Channel> channels = channelSecDao.findAll();
		for (Channel ch : channels) {
			jedisUtils.del(Constant.HOT_CHANNEL_WEEKOFFER_SSET_KEY + ch.getcId());
			jedisUtils.del(Constant.HOT_CHANNEL_WEEKOFFER_HSET_KEY + ch.getcId());
			HotChannel hotChannel = hotChannelDao.findOne(ch.getcId());
			if (hotChannel != null) {
				hotChannel.setWeekOffer(new Long(0));
				hotChannelDao.save(hotChannel);
			}
		}
	}

	// @Scheduled(fixedRate = 1000)
	public void updateChannelOnlineUserCount() {
		System.out.println("updateChannelOnlineUserCount " + dateFormat.format(new Date()));
	}

	/**
	 * 每天6点刷新用户的积分
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void freshUserTaskScore() {
		clubTaskUtils.freshClubUserTaskScore();
	}

	/**
	 * 每月一号6:00刷新粉丝团等级
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 1 * ?")
	public void freshFansClubLevel() {
		clubTaskUtils.freshFansClubLevel();
	}

	/**
	 * 每天刷新粉丝团
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void fansClubDailyFresh() {
		clubTaskUtils.freshFansClubMemberCount();
	}

	/**
	 * 每天更新合同状态 请求解约，若对方不回复，过期后，状态设为agreed
	 * 
	 * @author songfl
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void recoverContractStatus() {
		List<AnchorContract> contracts = anchorContractPriDao
				.findByStatusAndEndDateLessThan(AnchorContractStatus.REQUEST_CANCEL.getValue(), DateUtils.getNowTime());
		if (contracts == null || contracts.size() == 0)
			return;
		for (AnchorContract ac : contracts) {
			ac.setStatus(AnchorContractStatus.AGREED.getValue());
		}
		anchorContractPriDao.save(contracts);
	}

	/**
	 * @author songfl 刷新频道时长 频道状态判断，没有调用closePush的主播，如果云信频道不在开播状态,认为频道已关闭 误差5分钟
	 */
	@Scheduled(fixedRate = 10000)
	public void checkChannelState() {
		try {
			if (configUtils.getPlatformConfig(Constant.CHANNEL_STATUS_CHECK).equals("true")) {
				// 频道状态（0：空闲； 1：直播； 2：禁用； 3：直播录制）
				List<HotChannel> hotChannels = (List<HotChannel>) hotChannelDao.findAll();
				if (hotChannels == null || hotChannels.size() == 0)
					return;

				// List<HotChannel> closedChannels = new
				// ArrayList<HotChannel>();
				List<Channel> changedChannels = new ArrayList<Channel>();

				for (HotChannel hotChannel : hotChannels) {
					Channel channel = channelPriDao.findOne(hotChannel.getId());
					if (channel != null) {
						YXResultSet ret;
						try {
							ret = Videocloud163.channelstatsChannel(hotChannel.getYunXinCId());
							if (ret.getCode().equals("200")) {
								String status = ret.getMap("ret").get("status").toString();
								if (status.equals("0") || status.equals("2") || status.equals("3")) {
									if (channel.getStatus() != ChannelStatus.Close.getValue()) {
										channel.setStatus(ChannelStatus.Close.getValue());
										changedChannels.add(channel);
										// closedChannels.add(hotChannel);
										anchorUtils.anchorClose(channel.getOwnerId(), channel.getcId());
									}
								} else {
									if (channel.getStatus() != ChannelStatus.OPEN.getValue()) {
										channel.setStatus(ChannelStatus.OPEN.getValue());
										changedChannels.add(channel);
										// closedChannels.add(hotChannel);
										anchorUtils.anchorStart(channel.getOwnerId(), channel.getcId());
									}
								}
							}
						} catch (IOException e) {
							logger.error("", e);
						}
					}
				}

				if (changedChannels.size() > 0) {
					channelPriDao.save(changedChannels);
				}
				// if (closedChannels.size() > 0) {
				// hotChannelDao.save(closedChannels);
				// }
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}