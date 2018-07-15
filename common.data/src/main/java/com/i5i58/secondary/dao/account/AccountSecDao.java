package com.i5i58.secondary.dao.account;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.Account;

/**
 * AccountDao DB Operations
 * 
 * @author frank
 *
 */
@Transactional
public interface AccountSecDao extends PagingAndSortingRepository<Account, String> {

	public Account findByOpenIdAndPassword(String openId, String password);

	public Account findByPhoneNoAndPassword(String phoneNo, String password);

	public Account findByPhoneNo(String phoneNo);

	public Account findByEmailAddress(String emailAddress);

	public Account findByAccIdAndPassword(String accId, String password);

	public Account findByOpenId(String openId);

	@Query(value = "select a from Account a where a.isAndroid=0 and (a.openId like %?1% or a.phoneNo like %?1% or a.emailAddress like %?1% or a.nickName like %?1% or a.stageName like %?1% or a.location like %?1% or a.signature like %?1% or a.personalBrief like %?1%)")
	public Page<Account> findByParam(String param, Pageable pageable);

	@Query(value = "select a from Account a where a.anchor = true and (a.openId like %?1% or a.phoneNo like %?1% or a.emailAddress like %?1% or a.nickName like %?1% or a.stageName like %?1% or a.location like %?1% or a.signature like %?1% or a.personalBrief like %?1% or a.accId in (select accId from AnchorAuth where realName like %?1% ))")
	public Page<Account> findAnchorByParam(String param, Pageable pageable);

	@Query(value = "select a from Account a where a.accId in (select ac.accId from AnchorContract ac where ac.gId=?1)")
	public Page<Account> findAnchorInTopGroup(String gId, Pageable pageable);

	@Query(value = "select a from Account a where a.accId in (select ac.accId from AnchorContract ac where ac.gId=?1 and (ac.status = 1 or ac.status = 3)) and (select count(*) from Channel c where c.ownerId = a.accId) = 0")
	public Page<Account> findAnchorNoChannelInTopGroup(String gId, Pageable pageable);

	@Query(value = "select a from Account a where a.isAndroid=0 and (a.openId like %?1% or a.nickName like %?1%)")
	public Page<Account> findByParamWithOpenIdOrNickName(String param, Pageable pageable);

	@Query(value = "select a from Account a where a.isAndroid=0 and a.anchor = true and (a.openId like %?1% or a.nickName like %?1% or a.stageName like %?1%)")
	public Page<Account> findAnchorByParamWithOpenIdOrNickNameOrStageName(String param, Pageable pageable);

	public Page<Account> findByNullityAndIsAndroid(boolean nullity, boolean isAndroid, Pageable pageable);
}
