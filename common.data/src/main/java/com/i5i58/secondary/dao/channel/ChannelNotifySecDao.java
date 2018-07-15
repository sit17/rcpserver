package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelNotify;

@Transactional
public interface ChannelNotifySecDao extends CrudRepository<ChannelNotify, String> {

	public ChannelNotify findByCId(String cId);

}
