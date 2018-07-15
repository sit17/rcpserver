package com.i5i58.secondary.dao.account;

import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.AccountAuth;


public interface AuthUserSecDao extends CrudRepository<AccountAuth, String> {

	public AccountAuth findByAccId(String accId);
}
