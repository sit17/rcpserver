package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.channel.RemovedChannel;

@Transactional
public interface RemovedChannelSecDao extends PagingAndSortingRepository<RemovedChannel, String> {
	
}