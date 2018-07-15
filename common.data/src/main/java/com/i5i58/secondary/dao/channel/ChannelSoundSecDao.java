package com.i5i58.secondary.dao.channel;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelSound;

public interface ChannelSoundSecDao extends PagingAndSortingRepository<ChannelSound, String> {
	List<ChannelSound> findByName(String name);
}
