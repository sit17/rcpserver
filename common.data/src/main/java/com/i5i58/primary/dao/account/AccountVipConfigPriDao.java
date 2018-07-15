package com.i5i58.primary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.AccountVipConfig;

@Transactional
public interface AccountVipConfigPriDao extends PagingAndSortingRepository<AccountVipConfig, Integer> {

}
