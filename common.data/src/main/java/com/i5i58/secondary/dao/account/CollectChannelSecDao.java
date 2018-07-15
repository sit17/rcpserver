package com.i5i58.secondary.dao.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.CollectChannel;

public interface CollectChannelSecDao extends PagingAndSortingRepository<CollectChannel, Long> {

	CollectChannel findByAccIdAndCId(String accId, String cId);

	Page<CollectChannel> findByAccId(String accId, Pageable pageable);

}
