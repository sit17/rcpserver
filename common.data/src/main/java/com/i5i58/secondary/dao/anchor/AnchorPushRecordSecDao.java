package com.i5i58.secondary.dao.anchor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.AnchorPushRecord;

@Transactional
public interface AnchorPushRecordSecDao extends PagingAndSortingRepository<AnchorPushRecord, Long> {
	
	@Query("select a from AnchorPushRecord a where a.accId = ?1 and a.openTime >= ?2 and a.openTime <= ?3 and (a.ignored = false or a.ignored is null)")
	List<AnchorPushRecord> findByTime(String accId, long fromTime, long toTime);
	
//	@Query("select new com.i5i58.data.anchor.AnchorPushTime(SUM(a.closeTime-a.openTime) as t) from AnchorPushRecord a where (a.accId = ?1 and a.closeTime > a.openTime and a.openTime >= ?2 and a.openTime <= ?3 and (a.ignored = false or a.ignored is null)) GROUP BY FROM_UNIXTIME(a.openTime/1000, '%Y-%m-%d')")
//	List<AnchorPushTime> getAnchorTime(String accId, long fromTime, long toTime);
	
//	@Query("select SUM(a.closeTime-a.openTime) as t from AnchorPushRecord a where (a.accId = ?1 and a.closeTime > a.openTime and a.openTime >= ?2 and a.openTime <= ?3 and (a.ignored = false or a.ignored is null)) GROUP BY FROM_UNIXTIME(a.openTime/1000, '%Y-%m-%d')")
//	List<Long> getAnchorTime(String accId, long fromTime, long toTime);
}
