package com.i5i58.primary.dao.social;

import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.social.FansInfo;

@Transactional
public interface FansInfoPriDao extends CrudRepository<FansInfo, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<FansInfo> findByAccId(String accId);

	@Query("select fansInfo from FansInfo fansInfo where fansInfo.accId=?1")
	List<FansInfo> queryByAccId(String accId);
}
