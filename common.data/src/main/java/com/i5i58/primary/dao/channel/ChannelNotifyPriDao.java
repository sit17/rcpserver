package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelNotify;

@Transactional
public interface ChannelNotifyPriDao extends CrudRepository<ChannelNotify, String> {

	public ChannelNotify findByCId(String cId);

}
