package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.NewLotAnchorChannel;

@Transactional
public interface NewLotAnchorChannelSecDao extends PagingAndSortingRepository<NewLotAnchorChannel, String> {

	void deleteByCId(String cId);

}
