package com.i5i58.secondary.dao.account;



import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.AccountConfig;

/**
 * AccountConfigDao DB Operations
 * @author hexiaoming
 *
 */
@Transactional
public interface AccountConfigSecDao extends CrudRepository<AccountConfig, String>{

	public AccountConfig findByAccId(String accId);
}
