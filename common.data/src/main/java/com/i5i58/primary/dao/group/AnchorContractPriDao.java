package com.i5i58.primary.dao.group;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.group.AnchorContract;

@Transactional
public interface AnchorContractPriDao extends PagingAndSortingRepository<AnchorContract, String> {

	public Page<AnchorContract> findByGIdAndStatus(String gId, int status, Pageable pageable);

	@Query("select a from AnchorContract a where a.gId=?1 and a.status=1 and a.endDate>?2 and a.endDate<?3")
	public Page<AnchorContract> findByGIdAndExpire(String gId, long now, long expire, Pageable pageable);
	
	public List<AnchorContract> findByGIdAndStatusAndEndDateGreaterThan(String gId, int status, long now);

	public Page<AnchorContract> findByAccId(String accId, Pageable pageable);
	public List<AnchorContract> findByAccId(String accId);

	public AnchorContract findByAccIdAndStatus(String accId, int status);

	public AnchorContract findByAccIdAndStatusAndEndDateGreaterThan(String accId, int status, long date);

	public Page<AnchorContract> findByGId(String gId, Pageable pageable);

	public Page<AnchorContract> findByStatus(int status, Pageable pageable);
	
	public Page<AnchorContract> findByStatusAndEndDateGreaterThan(int status, long date, Pageable pageable);
	
	public List<AnchorContract> findByStatusAndEndDateLessThan(int status, long date);
}
