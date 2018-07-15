package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ChannelGuard;

@Transactional
public interface ChannelGuardSecDao extends PagingAndSortingRepository<ChannelGuard, Long> {

//	Page<ChannelGuard> findByAccId(String accId, Pageable pageable);

	ChannelGuard findByAccIdAndCIdAndDeadLineGreaterThan(String accId, String cId, long nowDate);

//	Page<ChannelGuard> findByCId(String cId, Pageable pageable);

	Page<ChannelGuard> findByCIdAndDeadLineGreaterThan(String cId, long nowTime, Pageable pageable);
	
	int countByCIdAndDeadLineGreaterThan(String cId, long nowTime);
	
	List<ChannelGuard> findByAccIdAndDeadLineGreaterThan(String accId,long nowTime, Pageable pageable);
	
	List<ChannelGuard> findByAccIdAndDeadLineGreaterThan(String accId,long nowTime);
	
	@Query("select c from ChannelGuard c where c.cId = ?1 and c.buyTime >= ?2 and c.buyTime <= ?3 order by c.buyTime DESC")
	List<ChannelGuard> findByCIdAndBuyTime(String cId, long fromTime, long toTime);
}
