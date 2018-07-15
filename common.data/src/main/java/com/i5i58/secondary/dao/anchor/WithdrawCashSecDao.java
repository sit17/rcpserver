package com.i5i58.secondary.dao.anchor;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.anchor.WithdrawCash;

/**
 * AccountDao DB Operations
 * 
 * @author frank
 *
 */
@Transactional
public interface WithdrawCashSecDao extends PagingAndSortingRepository<WithdrawCash, String> {

	Page<WithdrawCash> findByAccId(String accId, Pageable pageable);
	
	Page<WithdrawCash> findByStatus(int status, Pageable pageable);

}
