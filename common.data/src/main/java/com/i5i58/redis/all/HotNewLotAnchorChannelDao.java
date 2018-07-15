package com.i5i58.redis.all;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotNewLotAnchorChannel;

public interface HotNewLotAnchorChannelDao extends PagingAndSortingRepository<HotNewLotAnchorChannel, String> {

	Page<HotNewLotAnchorChannel> findByNewLot(int newLot, Pageable pageable);

}
