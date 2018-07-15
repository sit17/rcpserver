package com.i5i58.primary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.GroupOpeRecord;

@Transactional
public interface GroupOpeRecordPriDao extends PagingAndSortingRepository<GroupOpeRecord, Long> {

}
