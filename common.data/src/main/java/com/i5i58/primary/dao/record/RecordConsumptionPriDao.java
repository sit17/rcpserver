package com.i5i58.primary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.RecordConsumption;

@Transactional
public interface RecordConsumptionPriDao extends PagingAndSortingRepository<RecordConsumption, String> {

	Page<RecordConsumption> findByAccId(String accId, Pageable pageable);

	Page<RecordConsumption> findByAccIdAndGoodsType(String accId, int goodsType, Pageable pageable);

}
