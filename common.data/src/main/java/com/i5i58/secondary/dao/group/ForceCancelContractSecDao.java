package com.i5i58.secondary.dao.group;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.group.ForceCancelContract;

@Transactional
public interface ForceCancelContractSecDao extends PagingAndSortingRepository<ForceCancelContract, Long> {
	public Page<ForceCancelContract> findByStatus(int status, Pageable pageable);

	public ForceCancelContract findByCtId(String ctId);
	
	public List<ForceCancelContract> findByCtIdAndStatus(String ctId, int status);
}
