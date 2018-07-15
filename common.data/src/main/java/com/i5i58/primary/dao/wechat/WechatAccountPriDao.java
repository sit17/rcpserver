package com.i5i58.primary.dao.wechat;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.i5i58.data.wechat.WechatAccount;

public interface WechatAccountPriDao extends PagingAndSortingRepository<WechatAccount, String> {

	public WechatAccount findByOpenIdAndSelected(String openId, boolean selected);

	public WechatAccount findByOpenIdAndAccId(String openId, String accId);

	public List<WechatAccount> findByOpenId(String openId);

	public WechatAccount findAccIdByOpenId(String openId);
	
	public List<WechatAccount> findByAccId(String accId);
	
	@Query(value="select w from WechatAccount w where  w.accId in(select a.accId from Account a where a.openId like %?1% or a.nickName like %?1% or a.phoneNo like %?1%)")
	public Page<WechatAccount> findWechatAccountByParam(String param,Pageable pageable);
}