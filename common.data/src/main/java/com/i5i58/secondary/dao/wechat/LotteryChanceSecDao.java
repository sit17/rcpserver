package com.i5i58.secondary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.wechat.LotteryChance;
@Transactional
public interface LotteryChanceSecDao extends PagingAndSortingRepository<LotteryChance, String>{

	public LotteryChance findByAccId(String accId);
	
}
