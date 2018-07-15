package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.Wallet;

@Transactional
public interface WalletSecDao extends CrudRepository<Wallet, String> {

	Wallet findByAccId(String accId);
	
	@Query(value = "select w from Wallet w where w.accId in ( select a.accId from Account a where a.anchor = true)")
	public Page<Wallet> findAnchorWallet(Pageable pageable);
}
