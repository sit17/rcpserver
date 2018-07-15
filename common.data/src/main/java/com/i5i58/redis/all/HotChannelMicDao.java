package com.i5i58.redis.all;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotChannelMic;

public interface HotChannelMicDao extends PagingAndSortingRepository<HotChannelMic, String> {

	public HotChannelMic findByCIdAndIndexId(String cId, int index);

	public Page<HotChannelMic> findByCId(String cId, Pageable pageable);
}
