package com.i5i58.primary.dao.social;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.social.FollowInfo;

@Transactional
public interface FollowInfoPriDao extends PagingAndSortingRepository<FollowInfo, Long> {

	@Query("select followInfo.accId from FollowInfo followInfo where followInfo.fansId=?1")
	Page<String> findFollow(String accId, Pageable pageable);

	@Query("select followInfo.fansId from FollowInfo followInfo where followInfo.accId=?1")
	Page<String> findFans(String accId, Pageable pageable);

	FollowInfo findByAccIdAndFansId(String accId, String fansId);

	@Query("select Count(followInfo.accId) from FollowInfo followInfo where followInfo.fansId=?1")
	int countFollowByAccId(String accId);

	@Query("select Count(followInfo.fansId) from FollowInfo followInfo where followInfo.accId=?1")
	int countFansByAccId(String accId);
}
