package com.i5i58.primary.dao.anchor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.AnchorPushRecord;

@Transactional
public interface AnchorPushRecordPriDao extends PagingAndSortingRepository<AnchorPushRecord, Long> {
	//查找距离当前时间最近的未结束开播记录--开播记录的结束点可能没有记录到，忽略历史的记录
	//select * FROM anchor_open_record as a where (a.acc_id = 'a1' and (a.close_time = 0 or a.close_time is NULL)) ORDER BY a.open_time DESC limit 1
//	@Query("select a from AnchorPushRecord a where (a.accId = ?1 and a.cId = ?2 and (a.closeTime is null or a.closeTime = 0)) order by a.open_time DESC limit 1")
//	AnchorPushRecord findNearestUnClosedOpenRecord(String accId, String cId);
//	
	@Query("select a from AnchorPushRecord a where (a.accId = ?1 and a.cId = ?2 and (a.ignored = false or a.ignored is null) and (a.closeTime is null or a.closeTime = 0))")
	List<AnchorPushRecord> findUnClosedOpenRecord(String accId, String cId);
	
	@Query("select a from AnchorPushRecord a where (a.accId = ?1 and a.cId = ?2 and a.openTime<?3 and (a.ignored = false or a.ignored is null) and (a.closeTime is null or a.closeTime = 0))")
	List<AnchorPushRecord> findUnClosedOpenRecordBefore(String accId, String cId, long closeTime);
}
