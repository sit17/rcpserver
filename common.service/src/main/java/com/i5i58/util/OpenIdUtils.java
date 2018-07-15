package com.i5i58.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.i5i58.data.account.OpenId;
import com.i5i58.data.channel.ChannelId;
import com.i5i58.primary.dao.account.OpenIdPriDao;
import com.i5i58.primary.dao.channel.ChannelIdPriDao;

@Component
public class OpenIdUtils {

//	@Qualifier("entityManagerPrimary")
	@Autowired
	EntityManager entityManager;

	@Autowired
	OpenIdPriDao openIdPriDao;

	@Autowired
	ChannelIdPriDao channelIdPriDao;
	
	public String getRandomOpenId() {
		while (true) {
			Query query = entityManager
					.createNativeQuery("SELECT open_id FROM open_ids WHERE used=0 ORDER by rand() LIMIT 1");
			@SuppressWarnings("rawtypes")
			List resultList = query.getResultList();
			if (resultList == null || resultList.size() == 0) {
				return null;
			}
			String randId = (String) resultList.get(0);
			OpenId openId = openIdPriDao.findOne(randId);
			if (openId.getUsed() == 0) {
				openId.setUsed((byte) 1);
				openId.setUsedDate(DateUtils.getNowTime());
				openIdPriDao.save(openId);
				return openId.getOpenId();
			}
		}
	}
	
	public String getRandomChannelId(boolean consume) {
//		while (true) {
			Query query = entityManager
					.createNativeQuery("SELECT channel_id FROM channel_ids WHERE used=0 ORDER by rand() LIMIT 1");
			@SuppressWarnings("rawtypes")
			List resultList = query.getResultList();
			if (resultList == null || resultList.size() == 0) {
				return null;
			}
			String randId = (String) resultList.get(0);
			if (!consume || randId == null)
				return randId;
			ChannelId channelId = channelIdPriDao.findOne(randId);
			if (channelId != null && !channelId.isUsed()) {
				channelId.setUsed(true);
				channelId.setUsedDate(DateUtils.getNowTime());
				channelIdPriDao.save(channelId);
				return channelId.getChannelId();
			}
			return null;
//		}
	}
}
