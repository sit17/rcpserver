package com.i5i58.secondary.dao.wechat;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.account.Account;
import com.i5i58.data.wechat.WechatAccount;
@Transactional
public interface WechatAccountSecDao extends PagingAndSortingRepository<WechatAccount, String>{

	//public Account findByOpenIdAndSelected(String openId, boolean selected);
	
	public Page<WechatAccount> findBySelected(boolean selected,Pageable pageable);
	
	@Query(value="select a from WechatAccount a where a.accId like %?1% or a.openId like %?1% or a.bindDate like %?1%")
	public  Page<WechatAccount> findByParam(String param, Pageable pageable);
	
}
