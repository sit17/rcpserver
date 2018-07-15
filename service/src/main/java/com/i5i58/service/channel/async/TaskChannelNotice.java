package com.i5i58.service.channel.async;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.i5i58.config.RabbitMqBroadcastSender;
import com.i5i58.data.account.Account;
import com.i5i58.data.account.AccountConfig;
import com.i5i58.data.channel.Channel;
import com.i5i58.data.channel.ChannelNotice;
import com.i5i58.rabbit.RabbitMessage;
import com.i5i58.secondary.dao.account.AccountConfigSecDao;
import com.i5i58.secondary.dao.account.AccountSecDao;
import com.i5i58.secondary.dao.channel.ChannelNoticeSecDao;
import com.i5i58.secondary.dao.channel.ChannelSecDao;
import com.i5i58.util.DateUtils;
import com.i5i58.util.JedisUtils;

public class TaskChannelNotice implements Runnable {
	private long openTime;
	private AccountSecDao accountSecDao;
	private ChannelSecDao channelSecDao;
	private ChannelNoticeSecDao channelNoticeSecDao;
	private AccountConfigSecDao accountConfigSecDao;
	private String cId;
	private RabbitMqBroadcastSender sender;
	private JedisUtils jedisUtils;
	
	public TaskChannelNotice(long openTime, AccountSecDao accountSecDao, ChannelSecDao channelSecDao, 
			ChannelNoticeSecDao channelNoticeSecDao, AccountConfigSecDao accountConfigSecDao,
			String cId, RabbitMqBroadcastSender sender,JedisUtils jedisUtils){
		this.openTime = openTime;
		this.accountSecDao = accountSecDao;
		this.channelSecDao = channelSecDao;
		this.channelNoticeSecDao = channelNoticeSecDao;
		this.accountConfigSecDao = accountConfigSecDao;
		this.cId = cId;
		this.sender = sender;
		this.jedisUtils = jedisUtils;
	}
	
	private boolean checkNoticeRate(){
		String key = "channelnotice_time" + cId;
		if (jedisUtils.exist(key)){
			return false;
		}
		Long time = DateUtils.getNowTime();
		jedisUtils.set(key, time.toString(), "NX", "EX", 1800);
		return true;
	}
	
	@Override
	public void run() {
		if (!checkNoticeRate()){
			return;
		}
		Channel channel = channelSecDao.findByCId(cId);
		if (channel == null){
			return;
		}
		Account accOwner = accountSecDao.findOne(channel.getOwnerId());
		if (accOwner == null){
			return;
		}
		List<ChannelNotice> toUsers = channelNoticeSecDao.findByCId(cId);
		if (toUsers == null || toUsers.size() == 0)
			return;
		List<ChannelNotice> notices = new ArrayList<>();
		for (ChannelNotice user : toUsers){
			AccountConfig config = accountConfigSecDao.findByAccId(user.getAccId());
			//没有配置，或者设置了直播时通知标志，且没有设置免打扰模式
			if (config == null || (config.isEnableNoticedOnLive() && !config.isEnableNoDisturb())){
				notices.add(user);
			}
		}
		final int maxUserCount = 500;
		for (int i=0; i<notices.size(); i += maxUserCount){
			int toIndex = (i + maxUserCount) > notices.size() ? notices.size() : (i + maxUserCount);
			List<String> toAccIds = new ArrayList<String>();
			
			for (int j=i; j<toIndex; j++){
				toAccIds.add(notices.get(j).getAccId());
			}
			
			RabbitMessage message = new RabbitMessage();
			message.setCmd("channelNotice");
			JSONObject ob = new JSONObject();
			ob.put("cId", cId);
			ob.put("channelId", channel.getChannelId());
			ob.put("channelTitle", channel.getTitle());
			ob.put("yunXinRId", channel.getYunXinRId());
			ob.put("channelName", channel.getChannelName());
			ob.put("coverUrl", channel.getCoverUrl());
			ob.put("httpPullUrl", channel.getHttpPullUrl());
			ob.put("playerCount", channel.getPlayerCount());
			ob.put("playerTimes", channel.getPlayerTimes());
			ob.put("owner", channel.getOwnerId());
			ob.put("ownerName", accOwner.getNickName());
			ob.put("stageName",accOwner.getStageName());
			ob.put("toAccIds", toAccIds);
			ob.put("openTime", openTime);
			message.setData(ob.toJSONString());
			sender.sendYunXinMessage(message);
		}
	}

}
