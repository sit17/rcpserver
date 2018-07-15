package com.i5i58.secondary.dao.account;

import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.AccountProperty;

/**
 * AccountDao DB Operations
 * 
 * @author frank
 *
 */
@Transactional
public interface AccountPropertySecDao extends CrudRepository<AccountProperty, String> {

	public AccountProperty findByAccId(String accId);
	
	public AccountProperty findByAccIdAndVip(String accId,Integer VipId);

	public List<AccountProperty> findAll(Sort sort);
}
