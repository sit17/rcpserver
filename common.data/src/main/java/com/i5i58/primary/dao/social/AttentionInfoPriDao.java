package com.i5i58.primary.dao.social;

import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.social.AttentionInfo;

@Transactional
public interface AttentionInfoPriDao extends CrudRepository<AttentionInfo, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<AttentionInfo> findByAccId(String accId);

	@Query("select attentionInfo from AttentionInfo attentionInfo where attentionInfo.accId=?1")
	List<AttentionInfo> queryByAccId(String accId);
}
