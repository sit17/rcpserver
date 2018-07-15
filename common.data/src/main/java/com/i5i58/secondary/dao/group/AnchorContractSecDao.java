package com.i5i58.secondary.dao.group;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.group.AnchorContract;

@Transactional
public interface AnchorContractSecDao extends PagingAndSortingRepository<AnchorContract, String> {

	public Page<AnchorContract> findByGIdAndStatus(String gId, int status, Pageable pageable);

	@Query("select a from AnchorContract a where a.gId=?1 and a.status=0 and a.endDate>?2 and a.endDate<?3")
	public Page<AnchorContract> findByGIdAndExpire(String gId, long now, long expire, Pageable pageable);
	
	public List<AnchorContract> findByGIdAndStatus(String gId, int status);

	public Page<AnchorContract> findByAccId(String accId, Pageable pageable);
	
	public List<AnchorContract> findByAccId(String accId);
	
	public AnchorContract findByAccIdAndStatus(String accId, int status);
	
	@Query("select a from AnchorContract a where a.gId=?1 and (a.status=?2 or a.status=?3)")
	public List<AnchorContract> findByGIdAndTwoStatus(String gId, int status1, int status2);
	
//	public AnchorContract findByAccIdAndStatusAndEndDateGreaterThan(String accId, int status, long date);

	public Page<AnchorContract> findByGId(String gId, Pageable pageable);

	public Page<AnchorContract> findByStatus(int status, Pageable pageable);
	
	@Query("select a from AnchorContract a where a.gId=?1 and ((a.status=0 and a.direction =?2) or (a.status=3 and a.cancelDirection = ?2))")
	public Page<AnchorContract> findByGIdAndRequested(String gId, int direction, Pageable pageable);
	 
}
