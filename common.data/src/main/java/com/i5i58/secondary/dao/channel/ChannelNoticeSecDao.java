package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelNotice;

@Transactional
public interface ChannelNoticeSecDao extends PagingAndSortingRepository<ChannelNotice, Long> {

	ChannelNotice findByAccIdAndCId(String accid, String cId);

	List<ChannelNotice> findByAccId(String accId);
	List<ChannelNotice> findByCId(String cId);
}