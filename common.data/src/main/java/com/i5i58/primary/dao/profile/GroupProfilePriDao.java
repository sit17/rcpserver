package com.i5i58.primary.dao.profile;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.profile.GroupProfile;

@Transactional
public interface GroupProfilePriDao extends PagingAndSortingRepository<GroupProfile, String> {

	public List<GroupProfile> findByAccId(String accId);

	public GroupProfile findByAccIdAndFId(String accId, String groupFileId);
	
	@Query("select p from GroupProfile p where (p.name like %?1% or p.accId in(select a.accId from Account a where a.openId like %?1%) or p.description like %?1% or p.address like %?1% or p.legalPerson like %?1%  or p.fixedPhone like %?1%) and status=?2")
	public Page<GroupProfile> findByParamWithStatus(String param, int status, Pageable pageable);
	
	@Query("select p from GroupProfile p where p.name like %?1% or p.accId in(select a.accId from Account a where a.openId like %?1%) or p.description like %?1% or p.address like %?1% or p.legalPerson like %?1%  or p.fixedPhone like %?1%")
	public Page<GroupProfile> findByParam(String param, Pageable pageable);

	public GroupProfile findByFId(String fId);
}
