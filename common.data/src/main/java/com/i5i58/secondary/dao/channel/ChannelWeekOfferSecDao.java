package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelWeekOffer;

@Transactional
public interface ChannelWeekOfferSecDao extends CrudRepository<ChannelWeekOffer, Long> {

}
