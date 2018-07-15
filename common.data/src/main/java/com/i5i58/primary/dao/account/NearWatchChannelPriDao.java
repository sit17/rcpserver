package com.i5i58.primary.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.NearWatchChannel;

public interface NearWatchChannelPriDao extends PagingAndSortingRepository<NearWatchChannel, Long> {

	NearWatchChannel findByAccIdAndCId(String accId, String cId);

	@Query(value = "select count(n.id) from NearWatchChannel n where n.accId=?1 and n.cId=?2")
	int CountByAccIdAndCId(String accId, String cId);

	Page<NearWatchChannel> findByAccId(String accId, Pageable pageable);

}
