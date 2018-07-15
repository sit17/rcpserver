package com.i5i58.redis.all;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotChannelMajiaer;

public interface HotChannelMajiaerDao extends PagingAndSortingRepository<HotChannelMajiaer, String> {

	Page<HotChannelMajiaer> findByCId(String cId, Pageable pageable);

	HotChannelMajiaer findByCIdAndAccId(String cId, String accId);
	
	Page<HotChannelMajiaer> findByAccId(String accId, Pageable pageable);
}