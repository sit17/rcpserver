package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ClubTaskRecord;

@Transactional
public interface ClubTaskRecordSecDao extends PagingAndSortingRepository<ClubTaskRecord, Integer> {
	List<ClubTaskRecord> findByCIdAndAccIdAndCompleteDateGreaterThanAndCompleteDateLessThan(String cId,
			String accId, long firstTime, long lastTime);

	List<ClubTaskRecord> findByAccIdAndCId(String accId, String cId);
	
	List<ClubTaskRecord> findByAccIdAndCIdAndCompleteDate(String accId, String cId, long dateTime);
	
	@Query("select Count(c) from ClubTaskRecord c where c.cId=?1 and c.taskId =?2 and c.completeDate = ?3")
	int findCountByCIdAndTaskIdAndEndDate(String cId,int taskId, long endData);

	@Query("select Count(c) from ClubTaskRecord c where c.cId=?1 and c.taskId =?2 and c.completeDate >= ?3")
	int findCountByCIdAndTaskIdAndEndDateGreaterThan(String cId, int i, long weekStartTime);

	ClubTaskRecord findByAccIdAndCIdAndTaskIdAndCompleteDateGreaterThanEqual(String accId, String cId, int taskId,
			long weekStartTime);
}
