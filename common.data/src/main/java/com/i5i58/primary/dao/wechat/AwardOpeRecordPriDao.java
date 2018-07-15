package com.i5i58.primary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.wechat.AwardOpeRecord;
@Transactional
public interface AwardOpeRecordPriDao extends PagingAndSortingRepository<AwardOpeRecord, Integer> {

	

}