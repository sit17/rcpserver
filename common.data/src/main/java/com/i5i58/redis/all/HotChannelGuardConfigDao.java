package com.i5i58.redis.all;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.HotChannelGuardConfig;

public interface HotChannelGuardConfigDao extends CrudRepository<HotChannelGuardConfig, String> {

	HotChannelGuardConfig findByLevelAndMonth(int level, int month);

}
