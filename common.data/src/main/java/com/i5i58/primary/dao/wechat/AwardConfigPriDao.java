package com.i5i58.primary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.wechat.AwardConfig;
@Transactional
public interface AwardConfigPriDao extends PagingAndSortingRepository<AwardConfig, Integer> {
	


}