package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelGuardConfig;

@Transactional
public interface ChannelGuardConfigPriDao extends PagingAndSortingRepository<ChannelGuardConfig, Integer> {

}
