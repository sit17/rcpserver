package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelRecord;

@Transactional
public interface ChannelRecordSecDao extends PagingAndSortingRepository<ChannelRecord,Long>{
	Page<ChannelRecord> findByAccId(String accId, Pageable pageable);
	Page<ChannelRecord> findByAccIdAndGoodsType(String accId, int goodsType, Pageable pageable);
}
