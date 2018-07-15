package com.i5i58.primary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.i5i58.data.wechat.LotteryChance;
@Transactional
public interface LotteryChancePriDao extends PagingAndSortingRepository<LotteryChance, String> {

	public LotteryChance findByAccId(String accId);

}