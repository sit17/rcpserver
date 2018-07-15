package com.i5i58.primary.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.CollectChannel;

public interface CollectChannelPriDao extends PagingAndSortingRepository<CollectChannel, Long> {

	CollectChannel findByAccIdAndCId(String accId, String cId);

	Page<CollectChannel> findByAccId(String accId, Pageable pageable);

}
