package com.i5i58.primary.dao.record;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.RecordPay;

@Transactional
public interface RecordPayPriDao extends PagingAndSortingRepository<RecordPay, String> {

	Page<RecordPay> findByAccId(String accId, Pageable pageable);

	List<RecordPay> findByFromSourceAndStatusAndAccId(String code, int i, String accId);

}
