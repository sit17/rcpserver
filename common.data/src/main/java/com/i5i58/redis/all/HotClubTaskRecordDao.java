package com.i5i58.redis.all;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.HotClubTaskRecord;

@Transactional
public interface HotClubTaskRecordDao extends PagingAndSortingRepository<HotClubTaskRecord, String> {
	List<HotClubTaskRecord> findByAccIdAndCId(String accId, String cId);
}
