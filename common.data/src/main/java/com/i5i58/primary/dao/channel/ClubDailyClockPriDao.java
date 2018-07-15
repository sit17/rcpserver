package com.i5i58.primary.dao.channel;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.channel.ClubDailyClock;

@Transactional
public interface ClubDailyClockPriDao extends CrudRepository<ClubDailyClock, Long> {

	ClubDailyClock findByCIdAndAccIdAndClockDate(String cId, String accId, long time);

	@Query("select c from ClubDailyClock c where c.cId = ?1 and c.accId = ?2 and (c.clockDate >= ?3 and c.clockDate <= ?4) order by c.clockDate")
	public List<ClubDailyClock> findByTime(String cId, String accId, long fromTime, long toTime);
}
