package com.i5i58.redis.all;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotClubTaskConfig;

@Transactional
public interface HotClubTaskConfigDao extends PagingAndSortingRepository<HotClubTaskConfig, Integer> {

}
