package com.i5i58.redis.all;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotChannel;

/**
 * DB Operations
 * 
 * @author frank
 *
 */
@Transactional
public interface HotChannelDao extends PagingAndSortingRepository<HotChannel, String> {
	// find rooms by type with page - able
	Page<HotChannel> findHotChannelByType(int type, Pageable pageable);

	HotChannel findByOwnerId(String accId);

	Page<HotChannel> findByHot(int hot, Pageable pageable);

	List<HotChannel> findByGId(String parentId);

	Page<HotChannel> findTop20ByHot(int i, Pageable pageable);
	
	List<HotChannel> findByStatus(int status);
	
	HotChannel findByYunXinCId(String yunXinCId);
}
