package com.i5i58.secondary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.TakeHeartRecord;

@Transactional
public interface TakeHeartRecordSecDao extends PagingAndSortingRepository<TakeHeartRecord, Long> {
	public TakeHeartRecord findByAccIdAndTakeDate(String accId, long takeDate);
	public List<TakeHeartRecord> findByAccIdAndTakeDateGreaterThanAndTakeDateLessThan(String accId, long fromTime, long toTime);
}
