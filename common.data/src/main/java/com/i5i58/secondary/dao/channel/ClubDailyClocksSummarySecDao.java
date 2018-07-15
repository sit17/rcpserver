package com.i5i58.secondary.dao.channel;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.channel.ClubDailyClocksSummary;


@Transactional
public interface ClubDailyClocksSummarySecDao extends PagingAndSortingRepository<ClubDailyClocksSummary, String> {
	public ClubDailyClocksSummary findByAccIdAndCId(String accId, String cId);
	public Page<ClubDailyClocksSummary> findByCId(String cId, Pageable pageable);
}