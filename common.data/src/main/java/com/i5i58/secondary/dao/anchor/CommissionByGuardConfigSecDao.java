package com.i5i58.secondary.dao.anchor;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.CommissionByGuardConfig;
@Transactional
public interface CommissionByGuardConfigSecDao extends PagingAndSortingRepository<CommissionByGuardConfig, Integer> {
	List<CommissionByGuardConfig> findByGuardLevelOrderByMoneyOneMonth(int level);
}
