package com.i5i58.primary.dao.group;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.group.ForceCancelContract;

@Transactional
public interface ForceCancelContractPriDao extends PagingAndSortingRepository<ForceCancelContract, Long> {
	public Page<ForceCancelContract> findByStatusAndRequestedDateTimeGreaterThan(int status, long date, Pageable pageable);

	public ForceCancelContract findByCtId(String ctId);
	
	public ForceCancelContract findByCtIdAndStatus(String ctId,int status);
	

	
}
