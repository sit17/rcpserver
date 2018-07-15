package com.i5i58.primary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelMount;

@Transactional
public interface ChannelMountPriDao extends PagingAndSortingRepository<ChannelMount, Integer> {

	List<ChannelMount> findByNullity(boolean nullity);

}
