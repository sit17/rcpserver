package com.i5i58.primary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ChannelWatchingRecord;

@Transactional
public interface ChannelWatchingRecordPriDao extends CrudRepository<ChannelWatchingRecord, String> {
//	ChannelWatchingRecord findByCIdAndAccIdAndDateTime(String cId, String accId, long dateTime);
//	
//	@Query("select max(dateTime) from ChannelWatchingRecord")
//	long findNearestDateTime(String cId, String accId);
}
