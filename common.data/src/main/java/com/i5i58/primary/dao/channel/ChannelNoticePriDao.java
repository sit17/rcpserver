package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelNotice;

@Transactional
public interface ChannelNoticePriDao extends PagingAndSortingRepository<ChannelNotice, Long> {

	ChannelNotice findByAccIdAndCId(String accId, String cId);

}
