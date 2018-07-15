package com.i5i58.primary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelGuard;

@Transactional
public interface ChannelGuardPriDao extends PagingAndSortingRepository<ChannelGuard, Long> {

//	Page<ChannelGuard> findByAccId(String accId, Pageable pageable);

	ChannelGuard findByAccIdAndCId(String accId, String cId);
	
//	ChannelGuard findByAccIdAndCIdAndDeadLineGreaterThan(String accId, String cId, long nowDate);

//	Page<ChannelGuard> findByCId(String cId, Pageable pageable);

//	Page<ChannelGuard> findByCIdAndDeadLineGreaterThan(String cId, long nowTime, Pageable pageable);
	
//	List<ChannelGuard> findByAccIdAndDeadLineGreaterThan(String accId,long nowTime, Pageable pageable);
}
