package com.i5i58.secondary.dao.record;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.RecordConsumption;

@Transactional
public interface RecordConsumptionSecDao extends PagingAndSortingRepository<RecordConsumption, String> {

	Page<RecordConsumption> findByAccId(String accId, Pageable pageable);

	Page<RecordConsumption> findByAccIdAndGoodsType(String accId, int goodsType, Pageable pageable);

	Page<RecordConsumption> findByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(String channelId, int goodsType, long fromTime, long toTime, Pageable pageable);
	List<RecordConsumption> findByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(String channelId, int goodsType, long fromTime, long toTime);
	
	@Query("select count(DISTINCT  r.accId) from RecordConsumption r where r.channelId=?1 and r.goodsType = ?2 and r.date >= ?3 and r.date <= ?4")
	int countByChannelIdAndGoodsTypeAndDateGreaterThanAndDateLessThan(String channelId, int goodsType, long fromTime, long toTime);
}
