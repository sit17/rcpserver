package com.i5i58.secondary.dao.wechat;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.wechat.AwardOpeRecord;
@Transactional
public interface AwardOpeRecordSecDao extends PagingAndSortingRepository<AwardOpeRecord,String> {
	
	public List<AwardOpeRecord> findByAccId(String AccId);
}