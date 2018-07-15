package com.i5i58.secondary.dao.record;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.record.GroupOpeRecord;

@Transactional
public interface GroupOpeRecordSecDao extends PagingAndSortingRepository<GroupOpeRecord, Long> {
	Page<GroupOpeRecord> findByGId(String gId, Pageable pageable);
	Page<GroupOpeRecord> findByParentId(String parentId, Pageable pageable);
}
