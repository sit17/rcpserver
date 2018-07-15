package com.i5i58.secondary.dao.channel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelId;

public interface ChannelIdSecDao extends PagingAndSortingRepository<ChannelId, String> {

//	@Query("select c from ChannelId c where c.used=false and c.channelId >= (rand() * ((select max(c.channelId) from ChannelId) - (select min(c.channelId) from ChannelId)) + (select min(c.channelId) from ChannelId)) limit 1")
//	@Query("select c from ChannelId c order by c.channelId limit 1")
//	ChannelId findRandomOne();

	@Query("select c from ChannelId c where c.channelId = (select max(channelId) from ChannelId)")
	ChannelId findMaxOne();
}
