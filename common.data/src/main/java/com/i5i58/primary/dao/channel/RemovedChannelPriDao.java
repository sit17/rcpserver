package com.i5i58.primary.dao.channel;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.RemovedChannel;

@Transactional
public interface RemovedChannelPriDao extends PagingAndSortingRepository<RemovedChannel, String> {
	
}