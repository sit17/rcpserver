package com.i5i58.primary.dao.channel;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelMajiaer;

public interface ChannelMajiaerPriDao extends PagingAndSortingRepository<ChannelMajiaer, Long> {

	List<ChannelMajiaer> findByCId(String cId);

	ChannelMajiaer findByCIdAndAccId(String cId, String accId);
}