package com.i5i58.primary.dao.account;

import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.i5i58.data.account.Account;
import com.i5i58.data.account.Wallet;

@Transactional
public interface WalletPriDao extends CrudRepository<Wallet, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Wallet findByAccId(String accId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value="select w from Wallet w where w.accId = ?1 or w.accId = ?2")
	List<Wallet> findMineAndAnchor(String myAccId, String anchorAccId);
	
//	@Query(value = "select w from Wallet w where w.accId = :accId for update")
//	Wallet findByAccIdForUpdate(@Param("accId") String accId);
	
	@Modifying
	@Query(value = "update Wallet w set w.iGold=w.iGold + ?2 where w.accId = ?1")
	void updateIGold(String accId, long iGold);
	
	@Modifying
	@Query(value = "update Wallet w set w.diamond=w.diamond + ?2 where w.accId = ?1")
	void updateDiamond(String accId, long diamond);
	
	@Modifying
	@Query(value = "update Wallet w set w.commission=w.commission + ?2 where w.accId = ?1")
	void updateCommission(String accId, long commission);
	
	@Modifying
	@Query(value = "update Wallet w set w.giftTicket=w.giftTicket + ?2 where w.accId = ?1")
	void updateGiftTicket(String accId, long giftTicket);
	
	@Query(value = "select w from Wallet w where w.accId in ( select a.accId from Account a where a.anchor = true)")
	public Page<Wallet> findAnchorWallet(Pageable pageable);
	
}
