package com.i5i58.secondary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.wechat.AwardConfig;
@Transactional
public interface AwardConfigSecDao extends PagingAndSortingRepository<AwardConfig,Integer> {

	

}