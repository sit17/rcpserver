package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelMount;

@Transactional
public interface ChannelMountSecDao extends PagingAndSortingRepository<ChannelMount, Integer> {

	List<ChannelMount> findByNullity(boolean nullity);

}
