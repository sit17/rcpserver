package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelRightInfo;

@Transactional
public interface ChannelRightInfoPriDao extends CrudRepository<ChannelRightInfo, Integer> {

}