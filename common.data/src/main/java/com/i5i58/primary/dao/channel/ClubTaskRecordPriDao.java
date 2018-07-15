package com.i5i58.primary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ClubTaskRecord;

@Transactional
public interface ClubTaskRecordPriDao extends PagingAndSortingRepository<ClubTaskRecord, Integer> {
	List<ClubTaskRecord> findByCIdAndAccIdAndCompleteDateGreaterThanAndCompleteDateLessThan(String cId,
			String accId, long firstTime, long lastTime);

	List<ClubTaskRecord> findByAccIdAndCId(String accId, String cId);
}
