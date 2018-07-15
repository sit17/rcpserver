package com.i5i58.primary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.ContractOpeRecord;

@Transactional
public interface ContractOpeRecordPriDao extends PagingAndSortingRepository<ContractOpeRecord, Long> {

}
