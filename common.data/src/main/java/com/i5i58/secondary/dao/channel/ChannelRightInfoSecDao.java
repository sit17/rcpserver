package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelRightInfo;

@Transactional
public interface ChannelRightInfoSecDao extends CrudRepository<ChannelRightInfo, Integer> {

}