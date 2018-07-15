package com.i5i58.redis.all;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.HotAccountVipConfig;

public interface HotAccountVipConfigDao extends CrudRepository<HotAccountVipConfig, Integer> {

	HotAccountVipConfig findByLevelAndMonth(int level, int month);

}
