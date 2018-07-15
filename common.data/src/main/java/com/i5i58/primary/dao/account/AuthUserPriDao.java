package com.i5i58.primary.dao.account;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.AccountAuth;


public interface AuthUserPriDao extends CrudRepository<AccountAuth, String> {

	public AccountAuth findByAccId(String accId);
}
