package com.i5i58.redis.all;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotChannelGift;

public interface HotChannelGiftDao extends PagingAndSortingRepository<HotChannelGift, Integer> {

	HotChannelGift findByMainId(int id);

}
