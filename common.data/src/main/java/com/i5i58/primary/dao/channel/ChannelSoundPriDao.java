package com.i5i58.primary.dao.channel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelSound;

public interface ChannelSoundPriDao extends PagingAndSortingRepository<ChannelSound, String> {

}
