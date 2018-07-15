package com.i5i58.secondary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.ContractOpeRecord;

@Transactional
public interface ContractOpeRecordSecDao extends PagingAndSortingRepository<ContractOpeRecord, Long> {

	Page<ContractOpeRecord> findByGId(String gId, Pageable pageable);
}
