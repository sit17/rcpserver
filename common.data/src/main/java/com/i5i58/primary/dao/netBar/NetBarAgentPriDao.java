package com.i5i58.primary.dao.netBar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.netBar.NetBarAgent;

public interface NetBarAgentPriDao extends PagingAndSortingRepository<NetBarAgent, String> {

	Page<NetBarAgent> findByNullity(boolean b, Pageable pageable);

	NetBarAgent findByOwnerId(String accId);
	
	NetBarAgent findByNullityAndOwnerId(boolean b, String accId);

}