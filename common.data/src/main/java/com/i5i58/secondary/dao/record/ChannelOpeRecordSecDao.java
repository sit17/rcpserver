package com.i5i58.secondary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.ChannelOpeRecord;

@Transactional
public interface ChannelOpeRecordSecDao extends PagingAndSortingRepository<ChannelOpeRecord, Long> {
	
	Page<ChannelOpeRecord> findByTopGId(String topGId, Pageable pageable);
	
}
