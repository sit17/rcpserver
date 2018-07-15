package com.i5i58.redis.all;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.HotChannelHeartUser;

public interface HotChannelHeartUserDao extends CrudRepository<HotChannelHeartUser, String> {

	HotChannelHeartUser findByCIdAndAccId(String cId, String accId);
}
