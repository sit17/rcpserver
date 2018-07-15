package com.i5i58.primary.dao.anchor;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.CommissionByGuardConfig;
@Transactional
public interface CommissionByGuardConfigPriDao extends PagingAndSortingRepository<CommissionByGuardConfig, Integer> {

}
